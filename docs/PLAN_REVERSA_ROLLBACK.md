# Plan operacional de reversa (rollback) — C04

**Proyecto:** `tcsw-ventas`
**Candidato:** `v1.0-rc` (backend `1.0.0-RC1`, frontend `1.0.0-rc.1`)
**Línea base previa:** etiqueta Git `v0.3-web`
**Alcance:** API Spring Boot, cliente Angular y base PostgreSQL 16.

---

## 1. Disparadores de reversa

Iniciar la reversa cuando se confirme al menos una condición crítica que no pueda corregirse dentro de la ventana de atención acordada por el responsable
de liberación:

- La API no inicia o falla repetidamente las verificaciones de salud y endpoints esenciales (`GET /api/v1/productos`, `GET /api-docs`).
- Errores de venta, stock o persistencia producen pérdida, duplicación o inconsistencia de datos.
- El cliente Angular impide consultar el catálogo o registrar productos y el problema no se limita a una configuración corregible del servidor web/proxy.
- El artefacto desplegado no coincide con el commit o versión aprobados, o los checks requeridos de backend/frontend fallan.
- Se detecta un riesgo de seguridad crítico que requiere retirar la versión.

El responsable de liberación registra la incidencia, hora, versión/commit desplegados, impacto, decisión y responsable de ejecutar cada paso, si existe riesgo para la integridad de los datos, el DBA debe detener las escrituras antes de cambiar la aplicación.

## 2. Preparación y resguardo

1. Comunicar el inicio de la reversa y poner la aplicación en mantenimiento o detener temporalmente las escrituras.
2. Registrar el tag/commit y los identificadores de los artefactos actualmente desplegados y conservar logs de API, frontend, CI y PostgreSQL.
3. Antes de tocar la base, crear un respaldo consistente, para una instalación que publica PostgreSQL en el puerto `5434`, ejecutar desde una máquina con
   `pg_dump` instalado:

   ```sh
   pg_dump -Fc -h localhost -p 5434 -U ventas_user -d ventas_db \
     -f "ventas_db-pre-rollback.dump"
   ```

   `pg_dump` solicitará la contraseña de forma interactiva si no se configuró un mecanismo seguro de credenciales, es importante no incluir contraseñas en el comando, en Git ni en los logs.
4. Verificar que el archivo de respaldo exista, tenga tamaño no nulo y pueda inspeccionarse con `pg_restore --list ventas_db-pre-rollback.dump`. Guardar una copia protegida fuera del host que se está recuperando.

## 3. Volver a la versión previa

La etiqueta `v0.3-web` está publicada en el remoto. El procedimiento no reescribe `main` ni modifica el historial compartido:

```sh
git fetch origin --tags
git rev-parse v0.3-web
git worktree add ../tcsw-ventas-rollback-v0.3-web v0.3-web
```

El primer comando obtiene el tag si todavía no está en el clon local; el segundo registra el commit exacto usado para la reversa. Utilizar el worktree para preparar el despliegue sin cambiar la rama de trabajo principal.

### Artefactos

1. Si se conserva el artefacto de `v0.3-web` en el repositorio de artefactos operativo, desplegar esa copia verificada y registrar su checksum.
2. Si no se conserva, construir desde el worktree del tag con las versiones de herramientas requeridas por el `README.md` de esa revisión:

   ```sh
   mvn clean package spring-boot:repackage
   cd frontend-angular
   npm ci
   npm run build
   ```

   En `v0.3-web`, el plugin Spring Boot declara la clase principal pero no enlaza `repackage` automáticamente al ciclo `package`; por eso se invoca explícitamente para generar un JAR ejecutable. Como alternativa para
   ejecutar desde el código fuente, usar `mvn spring-boot:run` desde la raíz del worktree.

3. Registrar el nombre y checksum de cada artefacto generado, no sustituir los artefactos de la RC hasta verificar que el build previo terminó correctamente.
4. Detener o retirar las instancias de la RC y publicar el JAR y el directorio `dist/` de `v0.3-web` conforme al mecanismo de despliegue del entorno. Este repositorio no fija un servidor de producción ni un registro de artefactos; el operador debe usar los destinos configurados para el entorno.
5. Configurar el servidor web/proxy de modo que el frontend llegue a la API restaurada. El proxy `proxy.conf.json` de Angular corresponde al servidor de desarrollo y no sustituye la configuración de producción.

## 4. Restauración de PostgreSQL

La reversa normal es primero de aplicación. El PR C04 no introdujo una migración de esquema de base de datos; por ello, no restaurar una copia antigua automáticamente, ya que se perderían ventas y cambios válidos posteriores al respaldo.

Restaurar la base solo si el DBA determina que los datos/esquema quedaron corruptos o incompatibles y autoriza explícitamente la pérdida de los cambios posteriores al punto del respaldo, siempre que sea posible, recuperar primero en una base aislada para validar el resultado. Un DBA con permisos suficientes puede crear una base de recuperación y cargar el respaldo:

```sh
createdb -h localhost -p 5434 -U <usuario_admin> ventas_db_restore
pg_restore --no-owner --role=ventas_user \
  -h localhost -p 5434 -U <usuario_admin> \
  -d ventas_db_restore ventas_db-pre-rollback.dump
```

Validar la restauración antes de redirigir la aplicación. Si la política del entorno requiere restaurar en el nombre original, detener el tráfico y aplicar el respaldo con una ventana autorizada; `pg_restore --clean --if-exists` puede eliminar objetos existentes y no debe ejecutarse sobre la base activa sin
aprobación del DBA y un respaldo verificado.

## 5. Verificación posterior y cierre

Antes de reabrir el tráfico:

- Confirmar que backend y frontend corresponden al tag `v0.3-web` y a los checksums registrados.
- Consultar `GET /api/v1/productos` y verificar respuesta `200` con datos esperados; abrir el catálogo y comprobar que las rutas principales cargan.
- Ejecutar una verificación controlada de lectura y, con aprobación, una operación de escritura/venta; revisar que PostgreSQL persista los cambios y mantenga existencias consistentes.
- Revisar logs de API y PostgreSQL, errores HTTP, estado del proxy y salud del contenedor/servicio.
- Mantener observación durante la ventana acordada por el responsable de liberación y confirmar que no reaparecen los disparadores.
- Registrar resultado, impacto, respaldo utilizado (si aplica), commit/tag, artefactos y hora de reapertura. Mantener la RC y sus logs disponibles para análisis; no borrar ni reescribir commits.

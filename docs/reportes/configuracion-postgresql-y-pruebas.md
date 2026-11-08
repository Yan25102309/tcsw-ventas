# Configuración de PostgreSQL y ejecución de pruebas

## 1. Qué se hizo

Se corrigió la configuración de persistencia para que las credenciales de la
base de datos no estén embebidas en el repositorio. La aplicación y el
contenedor PostgreSQL ahora reciben la contraseña desde variables de entorno.

También se documentó y resolvió un conflicto de puertos: el puerto `5432` del
equipo estaba ocupado por otra instancia local de PostgreSQL. El contenedor de
este proyecto publica ahora el puerto `5434` del equipo y conserva el puerto
interno `5432`.

La configuración validada actualmente es:

| Parámetro | Valor |
|---|---|
| Base de datos | `ventas_db` |
| Usuario | `ventas_user` |
| Puerto del contenedor | `5432` |
| Puerto publicado en el equipo | `5434` |
| URL JDBC | `jdbc:postgresql://localhost:5434/ventas_db` |

La contraseña no se registra en este documento ni debe subirse al repositorio.
Cada integrante debe definirla localmente.

## 2. Cambios realizados

### `compose.yml`

- Se eliminó la contraseña escrita directamente.
- `POSTGRES_PASSWORD` ahora es obligatoria:

  ```yaml
  POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:?POSTGRES_PASSWORD must be set}
  ```

- El puerto publicado cambió de `5432:5432` a `5434:5432`.
- Se conserva el volumen `pgdata` para persistencia local.
- Se conserva el script `infrastructure/database/001_schema.sql` para
  inicializar una base nueva.

### `DatabaseConfig.java`

- `DB_PASSWORD` es obligatoria y debe ser no vacía.
- `DB_URL` permite configurar la URL; su valor predeterminado usa el puerto
  `5434`.
- `DB_USER` permite configurar el usuario; su valor predeterminado es
  `ventas_user`.
- La aplicación falla explícitamente si falta `DB_PASSWORD`, en vez de usar
  una contraseña predeterminada.

### `hibernate.cfg.xml` y repositorios Hibernate

- Se eliminó la contraseña del XML.
- `ProductoHibernateRepository` y `VentaHibernateRepository` reciben URL,
  usuario y contraseña desde `DatabaseConfig`.
- Hibernate y JDBC utilizan la misma configuración externa.

### Justificación

Estos cambios evitan que una contraseña quede expuesta en el código, en el
archivo Compose o en la configuración de Hibernate. También hacen que todos
los colaboradores puedan utilizar credenciales locales distintas sin modificar
archivos versionados. El puerto `5434` evita depender de la instancia
PostgreSQL que pueda existir en el puerto `5432` del equipo.

## 3. Migración desde el contenedor anterior

> **Advertencia:** `docker compose down -v` elimina el volumen `pgdata` de este
> proyecto y, por tanto, los datos locales almacenados en él. No ejecutar este
> comando si se necesitan esos datos sin realizar antes un respaldo.

Desde la raíz del proyecto, detener y eliminar el contenedor y volumen
anteriores:

```powershell
docker compose down -v
```

Si existe un contenedor antiguo creado fuera de este Compose, identificarlo:

```powershell
docker ps -a --filter "name=tcsw_ventas_db"
```

Si corresponde a este proyecto y ya no se necesita, detenerlo y eliminarlo por
su nombre:

```powershell
docker stop tcsw_ventas_db
docker rm tcsw_ventas_db
```

No detener ni eliminar contenedores de otros proyectos.

## 4. Inicialización de un entorno nuevo

Cada integrante debe abrir una nueva ventana de PowerShell y definir una
contraseña local. No usar necesariamente la contraseña de otra persona y no
guardarla en archivos versionados:

```powershell
$env:POSTGRES_PASSWORD = "<contraseña-local-segura>"
$env:DB_PASSWORD = $env:POSTGRES_PASSWORD
$env:DB_URL = "jdbc:postgresql://localhost:5434/ventas_db"
```

Después, iniciar PostgreSQL:

```powershell
docker compose up -d
```

El esquema se carga automáticamente cuando el volumen se crea por primera vez.
Si el volumen ya existe, el script de inicialización no se vuelve a ejecutar.

## 5. Comprobaciones obligatorias

### Estado del contenedor

```powershell
docker ps --filter "name=tcsw_ventas_db"
```

Debe aparecer el contenedor con estado `healthy` y el mapeo:

```text
0.0.0.0:5434->5432/tcp
```

### Conexión a PostgreSQL

```powershell
docker exec -e PGPASSWORD=$env:POSTGRES_PASSWORD `
  tcsw_ventas_db psql -h 127.0.0.1 -U ventas_user -d ventas_db `
  -c "SELECT current_user, current_database();"
```

La salida debe indicar:

```text
current_user | current_database
-------------+----------------
ventas_user  | ventas_db
```

### Validación de Compose

```powershell
docker compose config
```

Este comando debe terminar sin errores de interpolación de variables.

## 6. Ejecución de pruebas

Con el contenedor saludable y las variables definidas en la misma ventana de
PowerShell:

```powershell
mvn clean test
```

El resultado esperado es:

```text
Tests run: 72, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Las variables `$env:` solo duran mientras permanezca abierta esa ventana de
PowerShell. En una nueva sesión deben definirse nuevamente. Si el contenedor ya
está iniciado y las variables siguen disponibles, no es necesario ejecutar
`docker compose up -d` otra vez.

## 7. Diagnóstico rápido

### Error de autenticación

```text
password authentication failed
```

Comprobar que `DB_PASSWORD` coincide con la contraseña usada al crear el
volumen actual. Si es un entorno local descartable y no se requieren sus datos,
reinicializarlo:

```powershell
docker compose down -v
docker compose up -d
mvn clean test
```

### Error de conexión al puerto

Comprobar que el contenedor publica `5434` y que `DB_URL` usa ese mismo puerto:

```powershell
docker ps --filter "name=tcsw_ventas_db"
$env:DB_URL
```

Si otro proceso ocupa `5434`, detener únicamente el proceso autorizado o elegir
otro puerto publicado y actualizar `DB_URL` y `compose.yml` de forma coordinada.

### Falta `DB_PASSWORD`

Definirla antes de ejecutar Maven:

```powershell
$env:DB_PASSWORD = $env:POSTGRES_PASSWORD
mvn clean test
```

Nunca sustituirla por una contraseña escrita en `DatabaseConfig.java`,
`hibernate.cfg.xml`, `compose.yml` o cualquier otro archivo versionado.

## 8. Ejecución de la ventana Swing

La aplicación principal es `adapter.swing.SwingApp`. La ventana necesita
conectarse a PostgreSQL durante el arranque para crear los repositorios
Hibernate. Por ello, las variables de entorno deben estar definidas en la
misma sesión desde la que se inicia Java.

```powershell
$env:POSTGRES_PASSWORD = "ventas_user"
$env:DB_PASSWORD = $env:POSTGRES_PASSWORD
$env:DB_URL = "jdbc:postgresql://localhost:5434/ventas_db"
docker compose up -d
mvn compile
```

Después, desde VS Code, ejecutar la clase `adapter.swing.SwingApp` con **Run
Java** o **Debug Java**. Si se usa una terminal de PowerShell:

```powershell
mvn exec:java "-Dexec.mainClass=adapter.swing.SwingApp"
```

Si la ventana no aparece, verificar primero:

```powershell
docker ps --filter "name=tcsw_ventas_db"
$env:DB_PASSWORD
$env:DB_URL
```

El contenedor debe estar `healthy` y `DB_URL` debe apuntar al puerto `5434`.
Si falta la contraseña o la base no está disponible, `SwingApp` registra el
error y muestra un diálogo visible con las variables que deben revisarse, en
vez de terminar silenciosamente en el hilo de eventos de Swing.

## 8. Mejoras recientes de cobertura y pruebas

### Qué se hizo

Se ampliaron las pruebas automatizadas para superar el umbral de cobertura
requerido por SonarQube. La suite pasó de 69 a 72 pruebas y alcanzó >84% de
cobertura de instrucciones en el informe JaCoCo local.

También se corrigieron pruebas asíncronas que dependían de `Thread.sleep`.
Ahora esperan señales explícitas de finalización mediante `CountDownLatch`,
evitando resultados intermitentes y el incumplimiento de la regla de SonarQube
que prohíbe `Thread.sleep()` en pruebas.

### Dónde se hizo

#### `src/test/java/adapter/swing/VentaPanelTest.java`

Se creó esta nueva clase de pruebas para cubrir `VentaPanel`, que anteriormente
tenía 0% de cobertura. Se probaron:

- Inicialización de controles y valores por defecto.
- Exposición de botones y área de resumen.
- Actualización del estado visual.
- Lectura de entradas.
- Limpieza de entradas y resumen mediante `limpiarEntradasDetalle()` y
  `limpiarTodo()`.

#### `src/test/java/adapter/swing/VentaControllerTest.java`

Se ampliaron las pruebas del controlador para cubrir:

- Procesamiento asíncrono exitoso.
- Manejo de errores de persistencia.
- Campos vacíos.
- Valores no numéricos.
- Productos inexistentes.
- Ausencia del repositorio de productos.
- Estados de la vista durante el procesamiento.

La sincronización utiliza un repositorio falso con `CountDownLatch` y una vista
de prueba que notifica cuando la operación termina. Esto reemplaza esperas
basadas en tiempo y hace que las pruebas sean deterministas.

### Justificación

Las pruebas nuevas cubren comportamiento observable de la interfaz, no líneas
artificiales creadas únicamente para aumentar el porcentaje. Cubrir
`VentaPanel` permite validar una clase completa que estaba sin pruebas, mientras
que los casos adicionales del controlador verifican rutas exitosas y de error.

La sincronización explícita es preferible a `Thread.sleep()` porque espera el
evento real de finalización, reduce la duración de las pruebas y evita fallos
dependientes de la velocidad del equipo o del entorno de CI/CD.

### Resultado validado

```text
Tests run: 72, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Cobertura JaCoCo:

```text
Instrucciones: 87%
```

El informe se genera en:

```text
target/site/jacoco/index.html
```

Antes de enviar cambios, cada colaborador debe ejecutar:

```powershell
mvn clean test
```

y confirmar que no existan fallos, errores ni usos de `Thread.sleep()` dentro
de `src/test`.

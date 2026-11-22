# tcsw-ventas — Sistema de Ventas (Arquitectura Hexagonal, API REST y Cliente Angular)

**Equipo:** TCSW-E05  
**Integrantes:**
- **Integrante 1:** Victor Gustavo Reyes Vidal (`Novaxll`)
- **Integrante 2:** Ana María Reyes Hernández (`Yan25102309`)

**Experiencia Educativa:** Tecnologías para la Construcción de Software (NRC 19234)  
**Docente:** Dr. Gabriel Rodríguez Vásquez  
**Corte actual:** C04 — Candidato de liberación (`v1.0-rc` · Backend `1.0.0-RC1` · Frontend `1.0.0-rc.1`)

---

## 1. Descripción General de la Arquitectura

`tcsw-ventas` implementa un sistema de gestión de productos y confirmación de ventas bajo **Arquitectura Hexagonal (Puertos y Adaptadores)**, desacoplando por completo la lógica de negocio de los detalles de persistencia e interfaz de usuario:

- **Dominio (`domain`) y Aplicación (`application`):** Entidades, reglas de stock, políticas de descuento y casos de uso transaccionales (`ProductoService`, `ConfirmarVentaService`) libres de dependencias de frameworks.
- **Adaptadores de Salida (`adapter.hibernate`, `adapter.jdbc`, `adapter.memory`):** Persistencia en **PostgreSQL 16** mediante Hibernate 5 / JDBC parametrizado.
- **Adaptador Web REST (`adapter.rest` + `infrastructure.RestApp`):** API REST con **Spring Boot 2.7.18**, validación con Bean Validation, documentación **OpenAPI 3 / Swagger UI** y manejo global de errores conforme a **RFC 9457 (Problem Details)**.
- **Cliente Web (`frontend-angular`):** SPA en **Angular 16** (componentes *standalone*, enrutamiento *lazy*, formularios reactivos, tabla accesible WAI-ARIA y proxy inverso en desarrollo hacia `/api`).
- **Adaptador Escritorio (`adapter.swing`):** Cliente Java Swing heredado del Corte 2.

---

## 2. Requisitos Previos

Asegúrate de contar con las siguientes herramientas instaladas y disponibles en tu `PATH`:

| Herramienta | Versión recomendada | Comando de verificación |
| :--- | :--- | :--- |
| **Java Development Kit (JDK)** | 11 | `java -version` y `javac -version` |
| **Apache Maven** | 3.9+ | `mvn -version` (debe reportar Java 11) |
| **Docker & Docker Compose** | Docker Desktop / Engine con Compose v2 | `docker --version` y `docker compose version` |
| **Node.js y npm** | Node 18 LTS o 20 LTS | `node -v` y `npm -v` |
| **Git** | 2.40+ | `git --version` |

---

## 3. Configuración de Variables de Entorno

Las credenciales nunca se almacenan en el repositorio. Antes de levantar PostgreSQL o el backend, crea tu archivo `.env` a partir de la plantilla `.env.example`:

```bash
cp .env.example .env
```

Edita `.env` y define una contraseña segura en `POSTGRES_PASSWORD` y `DB_PASSWORD` (ambas deben coincidir):

```dotenv
POSTGRES_DB=ventas_db
POSTGRES_USER=ventas_user
POSTGRES_PASSWORD=tu_password_segura
DB_URL=jdbc:postgresql://localhost:5434/ventas_db
DB_USER=ventas_user
DB_PASSWORD=tu_password_segura
```

> **Nota:** `compose.yml` expone PostgreSQL 16 en el puerto **`5434`** del host (mapeado al `5432` del contenedor) y ejecuta automáticamente `infrastructure/database/001_schema.sql` al inicializar el volumen. Si `POSTGRES_PASSWORD` o `DB_PASSWORD` no están definidas, Docker Compose y `DatabaseConfig` fallarán explícitamente.

---

## 4. Guía de Ejecución Completa (Base de Datos + API REST + Angular)

### Paso 1: Levantar PostgreSQL 16 con Docker Compose

Desde la raíz del repositorio:

```bash
docker compose up -d
```

Verifica que el contenedor `tcsw_ventas_db` esté saludable (`healthy`):

```bash
docker compose ps
```

### Paso 2: Exportar variables de entorno e iniciar la API REST (Spring Boot)

En **macOS / Linux (bash/zsh)**:

```bash
export $(cat .env | xargs) && mvn spring-boot:run
```

En **Windows (PowerShell)**:

```powershell
Get-Content .env | ForEach-Object {
  if ($_ -match '^([^=]+)=(.*)$') { [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process') }
}
mvn spring-boot:run
```

La API quedará disponible en `http://localhost:8080`:
- **Swagger UI:** `http://localhost:8080/swagger-ui.html` (redirige a `http://localhost:8080/swagger-ui/index.html`)
- **OpenAPI JSON:** `http://localhost:8080/api-docs`
- **Endpoints principales:**
  - `GET /api/v1/productos`
  - `GET /api/v1/productos/{id}`
  - `POST /api/v1/productos`
  - `POST /api/v1/ventas`

### Paso 3: Instalar dependencias e iniciar el cliente Angular 16

En una segunda terminal, desde la raíz del repositorio:

```bash
cd frontend-angular
npm install
npm start
```

El servidor de desarrollo de Angular se iniciará en **`http://localhost:4200`** con el proxy `frontend-angular/proxy.conf.json` reenviando `/api/*` hacia `http://localhost:8080`.

Rutas disponibles en la aplicación web:
- `http://localhost:4200/productos` — Catálogo de productos (`ProductosPageComponent`)
- `http://localhost:4200/productos/nuevo` — Alta de producto con formulario reactivo (`ProductoFormComponent`)

---

## 5. Pruebas Automatizadas y Compilación

### Backend Java / Maven

Ejecutar toda la suite de pruebas (dominio, aplicación, arquitectura ArchUnit, persistencia y controladores REST con `MockMvc`):

```bash
mvn test
```

Ejecutar únicamente la verificación de arquitectura hexagonal y controladores REST:

```bash
mvn test -Dtest=ArchitectureTest,ProductoRestControllerTest,VentaRestControllerTest
```

> **Nota:** Las pruebas de integración JDBC/Hibernate que se conectan a PostgreSQL requieren que el contenedor `tcsw_ventas_db` esté activo y las variables de `.env` exportadas. `ArchitectureTest`, `ProductoRestControllerTest` y `VentaRestControllerTest` utilizan *fakes* y `MockMvc` sin requerir base de datos activa.

### Frontend Angular

Desde `frontend-angular/`:

```bash
# Instalación reproducible desde package-lock.json
npm ci

# Pruebas unitarias en Karma + ChromeHeadless (una sola ejecución)
npm test -- --watch=false --browsers=ChromeHeadless

# Compilación de producción (genera frontend-angular/dist/frontend-angular/)
npm run build
```

---

## 6. Empaquetado Reproducible del Candidato de Liberación (`v1.0-rc`)

Las versiones de ambos módulos están congeladas para el candidato de liberación (sin sufijos `-SNAPSHOT`):
- **Backend (`pom.xml`):** `1.0.0-RC1`
- **Frontend (`frontend-angular/package.json` y `package-lock.json`):** `1.0.0-rc.1`

### 6.1 Backend — JAR Ejecutable (*Fat JAR* con Spring Boot)

El plugin `spring-boot-maven-plugin` ejecuta el objetivo `repackage` durante la fase `package` tomando `infrastructure.RestApp` como clase principal.

1. Compilar, ejecutar la suite completa de pruebas y empaquetar desde limpio (con PostgreSQL activo y variables de `.env` cargadas):

```bash
# macOS / Linux (bash/zsh)
set -a && source .env && set +a
mvn clean package
```

```powershell
# Windows (PowerShell)
Get-Content .env | ForEach-Object {
  if ($_ -match '^([^=]+)=(.*)$') { [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process') }
}
mvn clean package
```

Esto genera el artefacto ejecutable **`target/tcsw-ventas-1.0.0-RC1.jar`**.

2. Ejecutar el paquete directamente con `java -jar` (sin necesidad de Maven; funciona igual en bash/zsh y PowerShell con las variables de `.env` cargadas en la sesión):

```bash
java -jar target/tcsw-ventas-1.0.0-RC1.jar
```

3. Verificar el servicio empaquetado (*smoke test*):

```bash
curl -i http://localhost:8080/api/v1/productos
# Esperado: HTTP/1.1 200 OK con el catálogo JSON de productos
```

### 6.2 Frontend — Build de Producción Angular (`dist/`)

Desde `frontend-angular/`:

```bash
npm ci
npm run build
```

El compilado optimizado con *output hashing* y división de *chunks lazy* se genera en **`frontend-angular/dist/frontend-angular/`** (con punto de entrada `index.html`), listo para ser servido por cualquier servidor web estático (Nginx, Caddy, Apache) con proxy inverso de `/api/*` hacia la API REST.

---

## 7. Integración Continua (GitHub Actions)

El repositorio incluye el flujo automatizado [`.github/workflows/ci.yml`](.github/workflows/ci.yml), el cual se ejecuta en cada `push` y `pull_request` hacia la rama `main` con dos *jobs* independientes:

1. **`backend` (Java 11 + Maven + PostgreSQL 16):**
   - Levanta un servicio efímero `postgres:16-alpine` con *healthcheck* (`pg_isready`) en el puerto `5434`.
   - Inicializa el esquema relacional aplicando `infrastructure/database/001_schema.sql` y `infrastructure/database/002_schema_venta.sql`.
   - Ejecuta `mvn -B clean test -Djava.awt.headless=true` (pruebas unitarias de dominio y aplicación, reglas hexagonales `ArchitectureTest` con ArchUnit, controladores REST `MockMvc`, pruebas Swing *headless* e integración real JDBC/Hibernate sobre PostgreSQL 16).
   - Empaqueta el JAR ejecutable con `mvn -B package -DskipTests`, resuelve dinámicamente la versión desde `pom.xml`, verifica la estructura reempaquetada de Spring Boot (`BOOT-INF/classes/infrastructure/RestApp.class`) y ejecuta un *smoke test* en vivo arrancando `java -jar` contra PostgreSQL 16 para validar `HTTP 200` en `GET /api/v1/productos` y `GET /api-docs`.
2. **`frontend` (Angular 16 + Karma ChromeHeadless):**
   - Instala dependencias reproducibles con `npm ci` en Node.js 20 LTS.
   - Ejecuta la suite de pruebas unitarias con `npm test -- --watch=false --browsers=ChromeHeadless`.
   - Valida el empaquetado de producción con `npm run build` verificando la generación de `dist/frontend-angular/index.html`.

---

## 8. Procedimiento de Reversa (*Rollback*) y Notas de Versión

En caso de detectar regresiones críticas tras desplegar el candidato de liberación `v1.0-rc`, la línea base estable anterior para reversa es la etiqueta **`v0.3-web`**:

```bash
# Referencia rápida para restaurar el código fuente a la versión estable previa
git fetch --tags
git checkout v0.3-web
```

Para el procedimiento completo de reversa (criterios de activación, respaldo/restauración de base de datos PostgreSQL, verificación posterior y responsables) y el registro de cambios del corte, consulta:
- `docs/PLAN_REVERSA_ROLLBACK.md` — Plan formal de reversa (*rollback*) del Corte C04.
- `CHANGELOG.md` — Historial de cambios por versión y corte.

---

## 9. Estructura del Proyecto

```text
tcsw-ventas/
├── .github/
│   └── workflows/
│       └── ci.yml                             # Pipeline CI en GitHub Actions (Backend + Frontend)
├── .env.example                               # Plantilla segura de variables de entorno
├── .gitignore                                 # Reglas de exclusión (permite !.env.example)
├── compose.yml                                # Servicio PostgreSQL 16 (puerto 5434)
├── pom.xml                                    # Descriptor Maven (1.0.0-RC1, Java 11, Spring Boot 2.7.18, repackage JAR)
├── README.md                                  # Guía principal del proyecto
├── infrastructure/
│   └── database/
│       ├── 001_schema.sql                     # DDL inicial de producto y datos semilla
│       └── 002_schema_venta.sql               # DDL de venta y detalle_venta
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── domain/                        # Entidades y reglas puras de negocio (Producto, Venta, descuentos)
│   │   │   ├── application/                   # Puertos y casos de uso (ProductoService, ConfirmarVentaService)
│   │   │   ├── adapter/
│   │   │   │   ├── rest/                      # Controladores REST, DTOs y ApiExceptionHandler (RFC 9457)
│   │   │   │   ├── hibernate/                 # Repositorios y entidades JPA/Hibernate
│   │   │   │   ├── jdbc/                      # Adaptador JDBC con PreparedStatement
│   │   │   │   ├── memory/                    # Repositorio en memoria
│   │   │   │   └── swing/                     # Cliente de escritorio Swing
│   │   │   └── infrastructure/                # Bootstrap RestApp, DatabaseConfig, PersistenceException
│   │   └── resources/                         # application.properties y hibernate.cfg.xml
│   └── test/java/                             # Pruebas unitarias, integración y ArchitectureTest
├── frontend-angular/
│   ├── angular.json                           # Configuración Angular CLI 16 (proxyConfig en serve)
│   ├── karma.conf.js                          # Configuración Karma / ChromeHeadless
│   ├── package.json                           # Dependencias y scripts npm (versión 1.0.0-rc.1)
│   ├── proxy.conf.json                        # Proxy /api -> http://localhost:8080
│   └── src/app/
│       ├── app.config.ts                      # Providers de router y HttpClient
│       ├── app.routes.ts                      # Rutas lazy (/productos, /productos/nuevo)
│       ├── models/                            # DTOs Producto, CrearProductoRequest y EstadoUi
│       ├── services/                          # ProductosService (manejo RFC 9457 y fallos de red/proxy)
│       └── components/
│           ├── productos-page/                # Contenedor inteligente con estados UI
│           ├── producto-list/                 # Tabla accesible WAI-ARIA de presentación
│           └── producto-form/                 # Formulario reactivo de alta de producto
└── docs/
    ├── openapi.yml                            # Contrato OpenAPI 3 de la API REST
    ├── pruebas_http.http                      # Colección de peticiones HTTP de verificación
    ├── P09_reporte_api_rest.md                # Reporte integrador de API REST (P09)
    ├── P10_reporte_cliente_angular.md         # Reporte integrador de cliente Angular (P10)
    ├── contribuciones/                        # Reportes individuales de contribución
    ├── evidencias/                            # Capturas de pruebas, PRs y revisiones
    └── reportes/                              # Reportes históricos de prácticas y cortes anteriores
```

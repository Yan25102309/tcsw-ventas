# Reporte de Contribución Individual — Integrante 1 (P09, P10, C03 y C04)

* **Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll` — Integrante 1)
* **Equipo:** TCSW-E05
* **Experiencia Educativa:** Tecnologías para la Construcción de Software (NRC 19234)
* **Docente:** Dr. Gabriel Rodríguez Vásquez
* **Corte / Hito actual:** C04 — Candidato de liberación (`v1.0-rc`)

---

## 1. Trazabilidad en Git (Issues, Ramas, PRs y Commits)

| Etapa | Issue / PR | Rama / Referencia | Commit(s) | Rol |
| :--- | :--- | :--- | :--- | :--- |
| **P09 — API REST** | Issue #58 / PR #59 | `feat/p09-api-exception-handler-ventas` | `3577704` (merge `8923909`) | Autor |
| **P09 — Revisión API Productos & Reporte** | PR #57, PR #61 (`v2.2.0`) | `feat/p09-spring-openapi-producto-api`, `docs/p09-reporte-api-rest-pruebas-http` | `7301675`, `10df65c` | Revisor y merge a `main` |
| **P10 — Cliente Angular (Servicio y Listado)** | Issue #62 / PR #64 | `feat/p10-angular-servicio-productos-listado` | `1ca5a74` (merge `2fa2ed3`) | Autor / Co-autor |
| **P10 — Revisión Formulario Reactivo y Rutas** | PR #66 | `feat/p10-angular-formulario-rutas-reporte` | `ad5e733` | Revisor y merge a `main` |
| **P10 — Documentación Individual** | — | `main` | `918f942` | Autor (`P10_contribucion_Estudiante1.md`, `P10_Seccion_IV_Victor_Reyes.docx`) |
| **C03 — Cierre de Integración Web** | Issue #67 / PR #68 (`v0.3-web`) | `feat/c03-integracion-web-resiliencia` | `075061d` (merge `7127580`) | Autor |
| **C04 — Pipeline CI en GitHub Actions** | Issue #71 / PR #73 (en revisión por `Yan25102309`) | `feat/c04-ci-empaquetado-rc` | `ba96031` | Autor |
| **C04 — Versiones RC, Empaquetado JAR y Docs** | Issue #72 / PR #73 (en revisión por `Yan25102309`) | `feat/c04-ci-empaquetado-rc` | `4d35bfe`, `4428c99` | Autor |

---

## 2. Contribuciones Técnicas por Etapa

### 2.1 Práctica P09 — API REST Spring Boot, Ventas y Problem Details (RFC 9457)
1. **Manejador Global de Excepciones (`src/main/java/adapter/rest/ApiExceptionHandler.java`):** Implementación con `@RestControllerAdvice` y el DTO `ErrorResponse.java` siguiendo **RFC 9457 (Problem Details)** para devolver respuestas sanitizadas (`400 Bad Request`, `409 Conflict`, `500 Internal Server Error`) sin filtrar trazas SQL ni detalles de infraestructura.
2. **Controlador y DTOs de Ventas (`VentaRestController.java`, `ConfirmarVentaRequest.java`, `DetalleVentaRequest.java`):** Exposición de `POST /api/v1/ventas` con validación declarativa `@Valid` (`@NotEmpty`, `@NotNull`, `@Min`) delegando en `ConfirmarVentaService`.
3. **Composition Root (`src/main/java/infrastructure/RestApp.java`):** Arranque `@SpringBootApplication` inyectando `ProductoHibernateRepository`, `VentaHibernateRepository`, `ProductoService` y `ConfirmarVentaService`.
4. **Pruebas HTTP Aisladas (`src/test/java/adapter/rest/VentaRestControllerTest.java`):** Pruebas con `MockMvc` y repositorios *fake* cubriendo `201 Created`, `400 Bad Request`, `409 Conflict` y `500 Internal Server Error`.

### 2.2 Práctica P10 — Cliente Angular 16 (Servicio HTTP, Modelos, Listado y Pruebas)
1. **Modelos y Contratos (`frontend-angular/src/app/models/`):**
   - `producto.model.ts`: Interfaces `Producto` y `CrearProductoRequest` alineadas a `docs/openapi.yml`.
   - `ui-state.model.ts`: Unión discriminada `EstadoUi` (`idle` | `loading` | `success` | `empty` | `error`).
2. **Servicio HTTP (`frontend-angular/src/app/services/productos.service.ts`):** Consumo de `/api/v1/productos` (`obtenerTodos()`, `obtenerPorId()`, `crearProducto()`) y mapeo de errores RFC 9457 (`detail`).
3. **Componentes y Rutas (`productos-page`, `producto-list`, `proxy.conf.json`, `karma.conf.js`):** Tabla semántica accesible WAI-ARIA, estados de carga/vacío/error con reintento, proxy de desarrollo `/api -> http://localhost:8080` y suite Jasmine/Karma en `ChromeHeadless`.
4. **Revisión e Integración:** Revisión y aprobación del PR #66 (`ProductoFormComponent`, rutas *lazy* `/productos` y `/productos/nuevo`, reporte `docs/P10_reporte_cliente_angular.md`).

### 2.3 Corte C03 — Endurecimiento de Integración Web y Documentación Reproducible
1. **Resiliencia ante caída del backend en `ProductosService` (`frontend-angular/src/app/services/productos.service.ts`):**
   - Ampliación de `manejadorError` para reconocer respuestas cuando la API REST está apagada detrás del proxy de desarrollo (`status === 0`, `status === 502`, `status === 504`, o `status === 500` con mensaje en texto plano del proxy `http-proxy-middleware` / `ECONNREFUSED`), devolviendo el mensaje claro `'No fue posible conectar con la API REST. Verifique su conexión o si el servidor backend está activo.'`.
   - Preservación intacta de errores reales `500` con `detail` RFC 9457 y del fallback genérico en respuestas `500` sin `detail`.
2. **Ampliación de Pruebas Unitarias (`frontend-angular/src/app/services/productos.service.spec.ts`):**
   - Incorporación de 6 casos nuevos en Jasmine (`HttpTestingController`) cubriendo `504 Gateway Timeout`, `502 Bad Gateway`, `500` de texto plano del proxy (`Error occurred while trying to proxy` y `Proxy error` / `ECONNREFUSED`), `500` con `detail` RFC 9457 y `500` JSON sin `detail`.
3. **Plantilla de Entorno y Documentación Reproducible:**
   - Creación de `.env.example` en la raíz con las variables requeridas (`POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`, `DB_URL`, `DB_USER`, `DB_PASSWORD`) sin credenciales reales, y excepción `!.env.example` en `.gitignore`.
   - Actualización integral de `README.md` con guía paso a paso para PostgreSQL 16 (`docker compose up -d`), API REST Spring Boot (`mvn spring-boot:run`), cliente Angular (`npm start`), Swagger UI y ejecución de pruebas.
   - Limpieza de archivo temporal no rastreado (`test_out.docx`).

### 2.4 Corte C04 — Candidato de Liberación (`v1.0-rc`: CI, Versiones Congeladas y Empaquetado)
1. **Pipeline de Integración Continua (`.github/workflows/ci.yml` — Issue #71):**
   - Configuración de GitHub Actions disparado en `push` y `pull_request` hacia `main` con permisos mínimos (`contents: read`).
   - Job `backend`: servicio PostgreSQL 16 (`postgres:16-alpine`) con *healthcheck* (`pg_isready`), inicialización automática de `infrastructure/database/001_schema.sql` y `infrastructure/database/002_schema_venta.sql`, JDK 11 (`temurin`) con caché Maven, ejecución de `mvn -B clean test -Djava.awt.headless=true` (unitarias, ArchUnit, MockMvc, Swing headless, JDBC/Hibernate) y empaquetado con `mvn -B package -DskipTests` verificando el *fat JAR* de Spring Boot (`BOOT-INF/classes/infrastructure/RestApp.class`).
   - Job `frontend`: Node.js 20 LTS con caché npm, instalación determinista `npm ci`, pruebas unitarias `npm test -- --watch=false --browsers=ChromeHeadless` y compilación de producción `npm run build` verificando `dist/frontend-angular/index.html`.
2. **Congelamiento de Versiones y Empaquetado Reproducible (`pom.xml`, `frontend-angular/package.json`, `frontend-angular/package-lock.json` — Issue #72):**
   - Congelamiento de la versión del artefacto Maven de `1.0-SNAPSHOT` a `1.0.0-RC1`.
   - Incorporación de la ejecución `<goal>repackage</goal>` en `spring-boot-maven-plugin` (`mainClass: infrastructure.RestApp`) para que `mvn clean package` genere el *fat JAR* ejecutable `target/tcsw-ventas-1.0.0-RC1.jar` ejecutable mediante `java -jar`.
   - Congelamiento de la versión del cliente Angular a `1.0.0-rc.1` en `package.json` y sincronización exacta en `package-lock.json`.
3. **Actualización de Documentación Técnica (`README.md` y `docs/contribuciones/integrante-1.md` — Issue #72):**
   - Incorporación en `README.md` de las guías reproducibles de empaquetado (`mvn clean package`, `java -jar target/tcsw-ventas-1.0.0-RC1.jar`, `npm ci && npm run build`), resumen del pipeline de GitHub Actions y referencia al plan de reversa (*rollback* hacia `v0.3-web`, delegando `CHANGELOG.md`, `docs/PLAN_REVERSA_ROLLBACK.md` y `docs/contribuciones/integrante-2.md` al PR del Integrante 2).

---

## 3. Verificación Ejecutada en C03

- **Frontend Angular — Pruebas Unitarias:** `cd frontend-angular && npm test -- --watch=false --browsers=ChromeHeadless` (**23/23 SUCCESS**)
- **Frontend Angular — Build de Producción:** `cd frontend-angular && npm run build` (compilación exitosa sin errores ni advertencias de presupuesto)
- **Backend Java — Suite Completa + ArchUnit + PostgreSQL:** `mvn test` con PostgreSQL 16 activo en Docker (**BUILD SUCCESS — 79/79 pruebas pasadas**, 0 fallos, 0 errores)
- **Recorrido Integrado E2E y Persistencia tras Reinicio de API:**
  - Registro de producto en `/productos/nuevo`, reinicio completo de `RestApp` (nueva JVM en puerto `8080`), verificación `200 OK` vía proxy Angular y consulta directa en PostgreSQL (`tcsw_ventas_db`).
  - Casos validados en vivo en navegador: catálogo con datos y alerta de stock bajo (`< 5`), estado vacío (`empty`: `No hay productos registrados en la base de datos.`), validaciones reactivas de formulario, Swagger UI (`/swagger-ui/index.html`) y error controlado con API apagada (`504` de proxy mapeado a mensaje amigable con botón `Reintentar`).
  - Respuestas RFC 9457 (`400 Bad Request` y `409 Conflict` en `POST /api/v1/ventas`) y `404` verificadas vía `curl`.

---

## 4. Verificación Ejecutada en C04

- **Backend — Compilación limpia, pruebas y empaquetado JAR:** `mvn -B clean package` contra PostgreSQL 16 en Docker (`tcsw_ventas_db`) -> **BUILD SUCCESS (79/79 pruebas pasadas, 0 fallos, 0 errores)** y generación del *fat JAR* reempaquetado `target/tcsw-ventas-1.0.0-RC1.jar` (~38 MB).
- **Backend — Arranque real con `java -jar`:** Ejecución directa de `java -jar target/tcsw-ventas-1.0.0-RC1.jar` con variables de `.env` exportadas; verificación con `curl`:
  - `GET http://localhost:8080/api/v1/productos` -> **`HTTP 200 OK`** devolviendo el catálogo JSON desde PostgreSQL.
  - `GET http://localhost:8080/api-docs` -> **`HTTP 200 OK`**.
- **Frontend — Instalación limpia, pruebas unitarias y build de producción:**
  - `cd frontend-angular && npm ci` -> instalación limpia sin errores (989 paquetes).
  - `npm test -- --watch=false --browsers=ChromeHeadless` -> **23/23 SUCCESS**.
  - `npm run build` -> compilación de producción exitosa (`dist/frontend-angular/`, bundle inicial 277.04 kB, 0 errores).
- **Integración Continua en GitHub Actions (PR #73):** Pipeline `CI - tcsw-ventas` ejecutado en verde ([Run #45065617457](https://github.com/Yan25102309/tcsw-ventas/actions/runs/45065617457)): `Backend (Java 11 + Maven + PostgreSQL 16)` ✓ y `Frontend (Angular 16 + Karma ChromeHeadless)` ✓.

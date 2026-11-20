# Reporte de Contribución Individual — Integrante 1 (P09, P10 y C03)

* **Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll` — Integrante 1)
* **Equipo:** TCSW-E05
* **Experiencia Educativa:** Tecnologías para la Construcción de Software (NRC 19234)
* **Docente:** Dr. Gabriel Rodríguez Vásquez
* **Corte / Hito:** C03 — Integración Web (`v0.3-web`)

---

## 1. Trazabilidad en Git (Issues, Ramas, PRs y Commits)

| Etapa | Issue / PR | Rama / Referencia | Commit(s) | Rol |
| :--- | :--- | :--- | :--- | :--- |
| **P09 — API REST** | Issue #58 / PR #59 | `feat/p09-api-exception-handler-ventas` | `3577704` (merge `8923909`) | Autor |
| **P09 — Revisión API Productos & Reporte** | PR #57, PR #61 (`v2.2.0`) | `feat/p09-spring-openapi-producto-api`, `docs/p09-reporte-api-rest-pruebas-http` | `7301675`, `10df65c` | Revisor y merge a `main` |
| **P10 — Cliente Angular (Servicio y Listado)** | Issue #62 / PR #64 | `feat/p10-angular-servicio-productos-listado` | `1ca5a74` (merge `2fa2ed3`) | Autor / Co-autor |
| **P10 — Revisión Formulario Reactivo y Rutas** | PR #66 | `feat/p10-angular-formulario-rutas-reporte` | `ad5e733` | Revisor y merge a `main` |
| **P10 — Documentación Individual** | — | `main` | `918f942` | Autor (`P10_contribucion_Estudiante1.md`, `P10_Seccion_IV_Victor_Reyes.docx`) |
| **C03 — Cierre de Integración Web** | Issue #67 / PR pendiente de revisión | `feat/c03-integracion-web-resiliencia` | En curso hacia `main` (`v0.3-web`) | Autor |

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

---

## 3. Verificación Ejecutada en C03

- **Frontend Angular — Pruebas Unitarias:** `cd frontend-angular && npm test -- --watch=false --browsers=ChromeHeadless` (**23/23 SUCCESS**)
- **Frontend Angular — Build de Producción:** `cd frontend-angular && npm run build` (compilación exitosa sin errores ni advertencias de presupuesto)
- **Backend Java — Suite Completa + ArchUnit + PostgreSQL:** `mvn test` con PostgreSQL 16 activo en Docker (**BUILD SUCCESS — 79/79 pruebas pasadas**, 0 fallos, 0 errores)
- **Recorrido Integrado E2E y Persistencia tras Reinicio de API:**
  - Registro de producto en `/productos/nuevo`, reinicio completo de `RestApp` (nueva JVM en puerto `8080`), verificación `200 OK` vía proxy Angular y consulta directa en PostgreSQL (`tcsw_ventas_db`).
  - Casos validados en vivo en navegador: catálogo con datos y alerta de stock bajo (`< 5`), estado vacío (`empty`: `No hay productos registrados en la base de datos.`), validaciones reactivas de formulario, Swagger UI (`/swagger-ui/index.html`) y error controlado con API apagada (`504` de proxy mapeado a mensaje amigable con botón `Reintentar`).
  - Respuestas RFC 9457 (`400 Bad Request` y `409 Conflict` en `POST /api/v1/ventas`) y `404` verificadas vía `curl`.

# Reporte de Contribución Individual — Integrante 1 (P10 / M13 / S37-S38)

* **Estudiante:** Integrante 1 (Ana María Reyes Hernández / `Yan25102309`)
* **Experiencia Educativa:** Tecnologías para la Construcción de Software (TCSW-19234)
* **Proyecto:** `tcsw-ventas`
* **Rama de Trabajo:** `feat/p10-angular-servicio-productos-listado`
* **Issue Relacionado:** #62 — `feat(frontend): configuracion de Angular, DTOs, ProductosService y ProductosPage`

---

## 1. Responsabilidades Asignadas y Resueltas (S37 / S38)

1. **Configuración del Entorno Frontend Angular 16:**
   - Inicialización estructurada del proyecto SPA bajo `frontend-angular` utilizando arquitectura basada en *Standalone Components* de Angular 16, TypeScript 5.1 y RxJS 7.8.
   - Definición de scripts npm (`build`, `test`, `start`) y optimización de dependencias sin acoplamiento a módulos heredados (NgModule).
   - Configuración de Karma con ejecución *headless* (`ChromeHeadless`) y reporte de cobertura integrado.

2. **Modelos DTO Tipados y Estado Asíncrono de UI:**
   - Creación de interfaces de transferencia `Producto` y `CrearProductoRequest` en `src/app/models/producto.model.ts`, alineadas estrictamente al contrato OpenAPI 3.1.0 (`/api/v1/productos`).
   - Definición del modelo discriminado `EstadoUi` y tipo `EstadoUiTipo` (`idle | loading | success | empty | error`) en `src/app/models/ui-state.model.ts` para el control determinista de estados asíncronos.

3. **Servicio HTTP Encapsulado (`ProductosService`):**
   - Implementación en `src/app/services/productos.service.ts` inyectando `HttpClient` mediante la inyección funcional `inject(HttpClient)`.
   - Exposición de flujos reactivos desacoplados: `obtenerTodos(): Observable<Producto[]>`, `obtenerPorId(id: number): Observable<Producto>` y `crearProducto(req: CrearProductoRequest): Observable<Producto>`.
   - Mapeador de excepciones `manejadorError` para interceptar códigos de estado (0 para pérdida de red, 404 para no encontrado, 409 con detalles estructurados RFC 9457 `detail`, y 500 genérico) protegiendo a los componentes de presentación contra la exposición de trazas HTTP o de infraestructura.

4. **Componente de Presentación Pura (`ProductoListComponent`):**
   - Implementación en `src/app/components/producto-list/producto-list.component.ts` con plantilla HTML semántica y accesible (`aria-label`, encabezados `th` con `scope="col"`).
   - Recepción de datos unidireccional vía `@Input({ required: true }) productos: Producto[]`.
   - Renderizado formateado de moneda (`currency:'MXN':'symbol':'1.2-2'`) y distintivo condicional de existencias críticas (`.alerta-stock` cuando `existencia < 5`).

5. **Componente Coordinador de Vista (`ProductosPageComponent`):**
   - Implementación en `src/app/components/productos-page/productos-page.component.ts` gestionando el ciclo de vida de la consulta (`ngOnInit`).
   - Control de los 4 estados de interfaz: indicador visual de carga (`loading`), alerta accesible con botón de reintento (`error`), mensaje informativo de catálogo vacío (`empty`), y delegación a `ProductoListComponent` al obtener datos (`success`).
   - Navegación accesible hacia el formulario de registro mediante enlace `routerLink="/productos/nuevo"`.

6. **Suite Exhaustiva de Pruebas Unitarias Automatizadas:**
   - Implementación de 14 pruebas unitarias independientes utilizando `HttpClientTestingModule` y `HttpTestingController` para simular llamadas GET, POST, errores de red y respuestas del estándar Problem Details.
   - Pruebas aisladas para el componente de presentación y el componente coordinador con espías (`createSpyObj`).
   - Cobertura de código alcanzada: **100% Statements, 100% Branches, 100% Functions, 100% Lines**.

---

## 2. Decisiones Técnicas, Arquitectura y Axiología

* **Arquitectura de Componentes (Separación Presentación / Coordinación):** Se cumple estrictamente el principio de responsabilidad única (SRP). Los componentes de presentación (`ProductoListComponent`) son puros, no inyectan servicios ni conocen endpoints REST. El componente contenedor (`ProductosPageComponent`) coordina el estado asíncrono y la navegación.
* **Encapsulamiento del Acceso a Datos:** `ProductosService` actúa como fachada sobre `HttpClient`. Ningún componente manipula encabezados HTTP, URLs ni errores de transporte directamente.
* **Seguridad y Cuidado de Datos (Axiología):** Sanitización total de errores antes de mostrarlos al usuario final en la interfaz gráfica; nunca se renderizan errores crudos de red, trazas o dialectos SQL.
* **Accesibilidad Web (A11y):** Inclusión de atributos ARIA (`aria-label`, `role="status"`, `role="alert"`), colores con contraste adecuado WCAG AA, y navegación por teclado en tablas y enlaces.

---

## 3. Evidencias de Comprobación y Resultados

### Ejecución de Pruebas Unitarias
Comando ejecutado en `frontend-angular`:
```bash
npm test -- --watch=false --code-coverage
```
Salida obtenida:
```
✔ Browser application bundle generation complete.
INFO [karma-server]: Karma v6.4.4 server started at http://localhost:9876/
INFO [launcher]: Starting browser Chrome
INFO [Chrome Headless 157.0.0.0 (Mac OS 10.15.7)]: Connected on socket ...
TOTAL: 14 SUCCESS

=============================== Coverage summary ===============================
Statements   : 100% ( 31/31 )
Branches     : 100% ( 9/9 )
Functions    : 100% ( 12/12 )
Lines        : 100% ( 28/28 )
================================================================================
```

### Compilación y Bundle de Producción
Comando ejecutado:
```bash
npm run build
```
Salida obtenida:
```
✔ Browser application bundle generation complete.
✔ Copying assets complete.
✔ Index html generation complete.

Initial Chunk Files           | Names         |  Raw Size | Estimated Transfer Size
main.e01f500e244e087d.js      | main          | 192.22 kB |                53.14 kB
polyfills.c6086f7132c2ae25.js | polyfills     |  33.04 kB |                10.64 kB
runtime.6b2d3b1dbfec6fe7.js   | runtime       | 910 bytes |               518 bytes
styles.ef46db3751d8e999.css   | styles        |   0 bytes |                       -

| Initial Total | 226.14 kB |                64.29 kB
Build at: 2026-11-15T01:46:46.771Z - Hash: fdef85d87e84fb4b - Time: 717ms
```

---

## 4. Trazabilidad Git

* **Rama:** `feat/p10-angular-servicio-productos-listado`
* **Issue:** `#62`
* **Commit:** `feat(frontend): configuracion de Angular, DTOs, ProductosService y ProductosPage (#62)`

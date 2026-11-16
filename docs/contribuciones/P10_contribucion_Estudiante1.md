# Reporte de Contribución Individual — Integrante 1 (Victor Gustavo Reyes Vidal)
**Módulo:** M13 — Angular Básico  
**Práctica:** P10 — Cliente Angular  
**Curso:** TCSW-19234 (Diseño y Construcción de Software)  
**Profesor:** Dr. Gabriel Rodríguez Vásquez  
**Equipo:** E05  
**Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll`)  

---

## 1. Identificación del Incremento y Metadatos de Git
- **Issue asignado:** [#62](https://github.com/Yan25102309/tcsw-ventas/issues/62) (`feat(frontend): configuracion de Angular, DTOs, ProductosService y ProductosPage`)
- **Rama de desarrollo:** `feat/p10-angular-servicio-productos-listado`
- **Pull Request:** [#64](https://github.com/Yan25102309/tcsw-ventas/pull/64)
- **Revisor (Code Reviewer):** Ana María Reyes Hernández (`Yan25102309`)
- **Estado en Git:** *Merged* en `main` (commit SHA-1: `1ca5a7454a08d5be198faefa98b495969404c63f`)

---

## 2. Artefactos Desarrollados

1. **`frontend-angular/src/app/models/producto.model.ts`**: Interfaces TypeScript `Producto` y `CrearProductoRequest` tipando formalmente el contrato expuesto por la API REST en OpenAPI 3.1.0.
2. **`frontend-angular/src/app/models/ui-state.model.ts`**: Tipo discriminado `EstadoUi` (`idle` | `loading` | `success` | `empty` | `error`) para gobernar deterministamente el renderizado condicional asíncrono.
3. **`frontend-angular/src/app/services/productos.service.ts`**: Servicio inyectable `@Injectable({ providedIn: 'root' })` con `HttpClient`, manejador de errores tipados conforme a RFC 9457 Problem Details (`detail`), y métodos reactivos `obtenerTodos()` y `obtenerPorId()`.
4. **`frontend-angular/src/app/components/producto-list/`**: Componente de presentación puro (*Dumb*) que renderiza la tabla semántica accesible WAI-ARIA, pipe de moneda `currency:'MXN'` y distintivos de stock bajo (`< 5`).
5. **`frontend-angular/src/app/components/productos-page/`**: Componente contenedor inteligente (*Smart*) que orquesta el ciclo de vida, transiciona entre estados UI y ofrece recuperación defensiva con botón de reintento.
6. **Suites de Pruebas Unitarias (`*.spec.ts`)**:
   - `productos.service.spec.ts`: Cobertura de llamadas GET, arreglos vacíos y respuestas HTTP 500 y fallo de red 0 con `HttpTestingController`.
   - `producto-list.component.spec.ts`: Enlace `@Input` unidireccional y renderizado DOM.
   - `productos-page.component.spec.ts`: Transiciones de estado asíncronas y re-ejecución del servicio.
7. **Infraestructura y Tooling**:
   - `frontend-angular/proxy.conf.json`: Proxy inverso hacia `http://localhost:8080/api`.
   - `frontend-angular/karma.conf.js`: Configuración headless de Chrome sobre Chromium.
   - `frontend-angular/tsconfig.json`: Soporte de tipos globales `jasmine` para el editor.

---

## 3. Verificación Automatizada
- **Compilación de producción:** `npm run build` completada sin advertencias ni errores de tipos.
- **Suite de pruebas unitarias:** `npm test -- --watch=false` ejecutando **14/14 pruebas exitosas (100% SUCCESS)** con cobertura completa de sentencias, ramas y funciones.

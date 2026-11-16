# 📄 Reporte de Integración Cliente Angular (P10 / M13)

**Experiencia Educativa:** Tecnologías para la Construcción de Software  
**Proyecto:** `tcsw-ventas` | **Módulo:** M13 — Angular Básico  

---

## 1. Árbol de Componentes y Separación de Responsabilidades

| Componente / Clase | Tipo | Responsabilidad Arquitectónica |
| :--- | :---: | :--- |
| **`ProductosPageComponent`** | Página (Coordinador) | Consume `ProductosService`, coordina la navegación y gestiona el ciclo de estados UI (`idle` -> `loading` -> `success` / `empty` / `error`). |
| **`ProductoListComponent`** | Presentación Pura | Recibe el arreglo `Producto[]` mediante `@Input()` y renderiza la tabla accesible, este no conoce endpoints HTTP. |
| **`ProductoFormComponent`** | Presentación / Formulario | Gestiona el formulario reactivo (`FormGroup`), aplica reglas de validación cliente y emite la petición de alta. |
| **`ProductosService`** | Servicio Inyectable | Centraliza la comunicación HTTP consumiendo la API `/api/v1/productos` mediante `HttpClient` y mapea errores. |

---

## 2. Matriz de Rutas de la Aplicación

| Ruta URL | Componente Asociado | Título Accesible | Regla de Ordenamiento |
| :--- | :--- | :--- | :--- |
| `/` | `RedirectTo: /productos` | N/A | Coincidencia exacta inicial |
| `/productos` | `ProductosPageComponent` | Catálogo de Productos - tcsw-ventas | Ruta de lista principal |
| `/productos/nuevo` | `ProductoFormComponent` | Registrar Producto - tcsw-ventas | Ruta específica (antes de parámetros) |
| `**` | `RedirectTo: /productos` | Error / Redirección | Comodín final |

---

## 3. Diagrama de Interacción Asíncrona

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario Web
    participant R as Router Angular
    participant P as ProductosPageComponent
    participant S as ProductosService
    participant H as HttpClient
    participant API as API REST (SpringBoot 8080)

    U->>R: Navega a /productos
    R->>P: Instancia ProductosPageComponent
    P->>P: Estado UI = 'loading'
    P->>S: obtenerTodos()
    S->>H: http.get('/api/v1/productos')
    H->>API: GET /api/v1/productos
    alt Respuesta Exitosa (200 OK)
        API-->>H: JSON Array [Productos]
        H-->>S: Observable<Producto[]>
        S-->>P: Suscripción recibe datos
        alt Arreglo Vacío []
            P->>P: Estado UI = 'empty'
        else Con Datos
            P->>P: Estado UI = 'success'
        end
    else Error de Red / Servidor (Status 0 / 500)
        API-->>H: HttpErrorResponse
        H-->>S: Catch error
        S-->>P: ThrowError (mensaje accesible)
        P->>P: Estado UI = 'error' (Muestra caja de alerta)
    end
```
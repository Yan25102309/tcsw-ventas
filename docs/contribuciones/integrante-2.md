# Reporte de Contribución Individual — P09 (M12 / S34-S36)

* **Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll` — Estudiante 2)
* **Proyecto:** `tcsw-ventas` (EE: Tecnologías para la Construcción de Software)
* **Rama de Trabajo:** `feat/p09-api-exception-handler-ventas`

---

## 1. Responsabilidades Asignadas y Resueltas
1. **Manejador Global de Errores (S36):** Implementé `ApiExceptionHandler` anotado con `@RestControllerAdvice` y el DTO `ErrorResponse` conforme al estándar **Problem Details (RFC 9457)**, garantizando respuestas uniformes y sanitizadas.
2. **DTOs de Venta:** Diseñé `ConfirmarVentaRequest` y `DetalleVentaRequest` con validaciones declarativas `@Valid`, `@NotNull`, `@Min` y `@NotEmpty`.
3. **Controlador REST de Ventas (S34/S35):** Desarrollé `VentaRestController` exponiendo `POST /api/v1/ventas` y delegando la transacción en el caso de uso `ConfirmarVentaService`.
4. **Bootstrap y Composition Root:** Implementé `RestApp` (`@SpringBootApplication`) configurando los Beans de infraestructura para la inyección de dependencias hexagonal.
5. **Suite de Pruebas Unitarias HTTP:** Desarrollé `VentaRestControllerTest` utilizando `MockMvc` aislado con *fakes*, comprobando respuestas `201 Created`, `400 Bad Request`, `409 Conflict` y `500 Internal Server Error`.

---

## 2. Decisiones Técnicas y Axiología
* **Sanitización de Errores:** Erradicación total de exposición de trazas de pila (*stack traces*), dialectos relacionales o datos sensibles hacia los clientes web.
* **Calidad Estática:** Pruebas JUnit 5 con visibilidad de paquete (*package-private*) resolviendo la regla SonarQube `java:S5786`.

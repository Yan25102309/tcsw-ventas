# Changelog

Los cambios importantes de `tcsw-ventas` se registran por versión, cabe mencionar que las capacidades descritas como incluidas en una versión pueden haberse incorporado en incrementos anteriores y consolidarse en este candidato.

---

## [v1.0-rc]

### Capacidades incluidas

- API REST Spring Boot para consultar y registrar productos y confirmar ventas, con validación de entradas, documentación OpenAPI/Swagger y errores estructurados conforme a Problem Details.
- Cliente web Angular 16 para consultar el catálogo y registrar productos, con formularios reactivos y estados accesibles de carga, vacío y error.
- Integración de API, Angular y PostgreSQL 16; el proxy del servidor de desarrollo reenvía `/api` a la API REST.

### Cambios del candidato

- Automatización CI con GitHub Actions para pruebas y empaquetado del backend con PostgreSQL 16, pruebas Karma en ChromeHeadless y compilación de producción Angular.
- Versiones congeladas para la RC: backend `1.0.0-RC1` y frontend `1.0.0-rc.1`.
- Generación y smoke test del JAR ejecutable de Spring Boot, además de verificación de los endpoints de API y OpenAPI en CI.
- Correcciones de resiliencia del proxy de desarrollo: errores de conexión, `502` y `504` se presentan al usuario con un mensaje recuperable; se conserva el detalle RFC 9457 cuando el servidor sí lo devuelve.

### Verificación y calidad

- En el PR #73, GitHub Actions informó como exitosos los jobs de backend (Java 11, Maven y PostgreSQL 16) y frontend (Angular 16 y Karma ChromeHeadless).
- Se reportaron 79 pruebas backend aprobadas, 23 pruebas Angular aprobadas, empaquetado del JAR y smoke test HTTP satisfactorios.
- El Overview de SonarQube para `1.0.0-RC1` muestra Quality Gate **PASSED** y en la vista de código nuevo: 0 bugs, 0 vulnerabilidades, 0 hotspots nuevos, 0 code smells y 0 deuda añadida; cobertura no mostrada al haber 0 líneas nuevas por cubrir y duplicación de 0.0 % en 9 líneas. Ver [ANALISIS_SONARQUBE_C04.md](docs/ANALISIS_SONARQUBE_C04.md) para el alcance y las salvedades, de igual forma en Overall Code informa 85.5 % de cobertura sobre 776 líneas evaluables, 79 pruebas unitarias, 0 bugs, vulnerabilidades, hotspots, code smells y deuda, y 0.0 % de duplicación en aproximadamente 1.6 mil líneas. Cabe mencionar que el 84.8 % publicado para C03 es histórico, no una medida de esta RC.

### Limitaciones conocidas

- No hay autenticación ni autorización por roles.
- No se incluyen gestión de clientes, facturación electrónica, pagos ni devoluciones/cancelaciones.
- El proxy de Angular es para desarrollo; el despliegue de producción requiere configurar un servidor web y enrutar `/api` a la API.
- El repositorio no define un despliegue único de frontend, API y PostgreSQL ni un registro de artefactos de producción.

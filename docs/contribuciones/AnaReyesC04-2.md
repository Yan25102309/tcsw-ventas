# Mi contribución al corte C04 — C04-2

**Estudiante:** Ana María Reyes Hernández (`Yan25102309`)
**Proyecto:** `tcsw-ventas` — Equipo TCSW-E05
**Issue de este incremento:** [#74](https://github.com/Yan25102309/tcsw-ventas/issues/74)
**Fecha de actualización:** 26 de noviembre de 2026

Este reporte reúne dos partes: el trabajo del incremento anterior que quedó integrado en el PR #73 y las tareas que estoy realizando ahora para el issue #74. De esta forma queda claro qué ya se entregó y qué corresponde a este
incremento.

---

## Incremento anterior — PR #73

En el PR #73 participé en la revisión y aprobación de la preparación de la versión candidata y revisé que la integración continua ejecutara las pruebas del backend y del frontend, que el empaquetado generara el JAR ejecutable y que la documentación explicara cómo compilar y ejecutar el proyecto.

Los cambios del PR quedaron registrados en estos commits:

| Commit | Qué se integró |
|---|---|
| [`ba96031`](https://github.com/Yan25102309/tcsw-ventas/commit/ba96031ed6bfa77b93cce59de44af29dd812cec1) | Pipeline de CI para backend y frontend, relacionado con el issue #71. |
| [`4d35bfe`](https://github.com/Yan25102309/tcsw-ventas/commit/4d35bfec913821db0eb14b7aa4e121978f27c88d) | Versiones RC y configuración para empaquetar el JAR, relacionado con el issue #72. |
| [`4428c99`](https://github.com/Yan25102309/tcsw-ventas/commit/4428c99a5ccee2acf144884e0931192f16d10f10) | Guía de empaquetado y actualización de la trazabilidad C04 en README. |
| [`8a2b646`](https://github.com/Yan25102309/tcsw-ventas/commit/8a2b646f992d9f55ec07086c01951b382eb7aecf) | Registro de los commits y de la evidencia de CI en el reporte individual. |
| [`01f9c73`](https://github.com/Yan25102309/tcsw-ventas/commit/01f9c73bcbaf3dffa603dd5bb0bd026cc1bac492) | Actualización de acciones, resolución dinámica de la versión del JAR y smoke test. |
| [`9df0a3c`](https://github.com/Yan25102309/tcsw-ventas/commit/9df0a3c7c74de58f234afb91532abd300eca9c50) | Integración final de los cambios C04 a `main`. |

Mi revisión cruzada quedó registrada como `APPROVED` en el PR #73, el PR se fusionó en `main`, por otro lado, los checks de GitHub Actions terminaron correctamente: backend con Java 11, Maven y PostgreSQL 16; y frontend con Angular 16 y Karma ChromeHeadless.

La evidencia registrada para ese incremento indica 79 pruebas backend aprobadas, 23 pruebas Angular aprobadas, empaquetado del JAR y respuestas HTTP exitosas en los endpoints verificados. Los detalles están en el [PR #73](https://github.com/Yan25102309/tcsw-ventas/pull/73).

## Incremento actual — Issue #74

En este incremento estoy cerrando las tareas de calidad y documentación que faltaban para el corte C04:

- Revisé las vistas de del Overview de SonarQube y documenté por separado las métricas de **New Code** y **Overall Code** en [ANALISIS_SONARQUBE_C04.md](../ANALISIS_SONARQUBE_C04.md).
- Redacté las notas de versión `v1.0-rc` en [CHANGELOG.md](../../CHANGELOG.md), incluyendo las capacidades los arreglos del proxy, la automatización CI y las limitaciones conocidas.
- Elaboré el procedimiento operacional de reversa en [PLAN_REVERSA_ROLLBACK.md](../PLAN_REVERSA_ROLLBACK.md), con disparadores, pasos de recuperación de aplicación y base de datos, y verificaciones posteriores.
- Actualicé este reporte para dejar constancia del trabajo del PR anterior y del incremento asociado al issue #74.

En **Overall Code**, SonarQube se muestra Quality Gate `PASSED`, cobertura de **85.5 % sobre 776 líneas evaluables**, 79 pruebas unitarias, duplicación de **0.0 % en aproximadamente 1.6 mil líneas** y ningún bloque duplicado. También muestra 0 bugs, 0 vulnerabilidades, 0 hotspots y 0 code smells, con ratings A en Reliability, Security, Security Review y Maintainability.

En **New Code**, se indica 0 bugs, vulnerabilidades, hotspots, code smells y deuda añadida; la cobertura no tiene porcentaje visible porque hay 0 líneas nuevas por cubrir. La duplicación es 0.0 % en 9 líneas nuevas.
Cabe mencionar que ambas vistas y sus alcances se explican con más detalle en el reporte de SonarQube.


## Alcance del reporte

Este archivo se limita al trabajo realizado en el PR #73 y al que corresponde al issue #74 por lo que no se pretende atribuir a este incremento tareas de prácticas o entregas anteriores.

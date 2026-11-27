# Análisis de SonarQube — C04

**Proyecto:** `tcsw-ventas`
**Versión analizada:** `1.0.0-RC1`
**Rama:** `main`
**Fecha que muestra SonarQube:** 26 de noviembre de 2026

---

## ¿Qué muestra el análisis?

SonarQube marca el **Quality Gate como PASSED**: todas las condiciones configuradas para esta versión se cumplieron. El tablero permite consultar dos vistas distintas: **New Code**, que se enfoca en los cambios recientes, y **Overall Code**, que presenta el estado del proyecto completo. Conviene mantenerlas separadas para no confundir sus resultados.

## Código nuevo

En **New Code**, SonarQube compara los cambios con la referencia “Since
1.0-SNAPSHOT”. Para ese conjunto de cambios informa:

| Métrica | Resultado |
|---|---:|
| Bugs nuevos | 0 |
| Vulnerabilidades nuevas | 0 |
| Security Hotspots nuevos | 0 |
| Code Smells nuevos | 0 |
| Deuda técnica añadida | 0 |
| Cobertura | No se muestra porcentaje; hay 0 líneas nuevas por cubrir |
| Duplicación | 0.0 % en 9 líneas nuevas |
| Ratings de Reliability, Security, Security Review y Maintainability | A |

La cobertura de esta vista no equivale a 0 %: el motivo es que el panel no presenta un porcentaje porque no hay líneas nuevas por cubrir.

## Código completo

Por otro lado **Overall Code** muestra el estado acumulado del proyecto:

| Métrica | Resultado |
|---|---:|
| Bugs | 0 |
| Vulnerabilidades | 0 |
| Security Hotspots | 0 |
| Code Smells | 0 |
| Deuda técnica | 0 |
| Cobertura | **85.5 %** sobre 776 líneas evaluables |
| Pruebas unitarias | 79 |
| Duplicación | **0.0 %** en aproximadamente 1.6 mil líneas |
| Bloques duplicados | 0 |
| Ratings de Reliability, Security, Security Review y Maintainability | A |

En dicha interfaz se muestra “Reviewed” junto al indicador de Security Hotspots, pero no presenta un número en ese campo; por eso aquí solo registramos el conteo visible de 0 hotspots y no inferimos cuántos fueron revisados.

## Cómo interpretar el resultado

En conjunto, las dos vistas son favorables: el Quality Gate pasó, no aparecen bugs, vulnerabilidades ni code smells y la cobertura global visible es 85.5 %. El porcentaje global corresponde al código completo y no debe confundirse con la cobertura de código nuevo.

Como referencia, el reporte de C03 indicaba 84.8 % de cobertura. Ese valor es histórico; para esta versión C04 usamos el 85.5 % que muestra la vista **Overall Code** de SonarQube.

La automatización de GitHub Actions en `.github/workflows/ci.yml` ejecuta las pruebas y builds y el resultado de análisis y sus métricas se documentan aquí con base en lo visto en la interfaz del Overview de SonarQube.

# Reporte de Contribución Individual — P08 (M10 / S28-S30)

* **Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll` — Estudiante 2)
* **Proyecto:** `tcsw-ventas` (EE: Tecnologías para la Construcción de Software)
* **Rama de Trabajo:** `feat/p08-adaptador-jdbc-producto` y `docs/p08-pruebas-reporte-integrador`

---

## 1. Responsabilidades Asignadas y Resueltas
1. **Configuración de Conexión Segura (S30):** Implementé `DatabaseConfig.java` abstrayendo los parámetros de conexión a través de variables de entorno sanitizadas (`DB_URL`, `DB_USER`, `DB_PASSWORD`).
2. **Adaptador JDBC con PreparedStatement (M10):** Construí `ProductoJdbcRepository.java` satisfaciendo el contrato de `ProductoRepository` con consultas SQL parametrizadas, gestión determinista mediante `try-with-resources` y sentencias upsert (`ON CONFLICT (id) DO UPDATE`).
3. **Suite de Pruebas Automatizadas (Issue #43):** Diseñé `ProductoJdbcRepositoryTest.java` validando persistencia real, búsquedas con `Optional.empty()` y rechazo de violaciones de integridad.
4. **Reporte Integrador:** Redacté `docs/P08-persistencia-jdbc.md` documentando las decisiones de diseño hexagonal y el protocolo de pruebas.

---

## 2. Decisiones Técnicas y Axiología
* **Prevención de Inyecciones SQL:** Uso estricto de consultas parametrizadas con marcadores de posición posicionales.
* **Control de Fugas:** Cierre garantizado de descriptores relacionales mediante `try-with-resources`.
* **Aislamiento Hexagonal:** Capas `domain` y `application` 100% puras y libres de tipos de infraestructura (`java.sql.*`).

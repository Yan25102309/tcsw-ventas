# Reporte de Contribución Individual — P08 (M10 / S28-S30)

* **Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll` — Estudiante 2)
* **Proyecto:** `tcsw-ventas` (EE: Tecnologías para la Construcción de Software)
* **Rama de Trabajo:** `feat/p08-adaptador-jdbc-producto` y `docs/p08-pruebas-reporte-integrador`

---

## 1. Responsabilidades Asignadas y Resueltas
1. **Configuración de Conexión Segura (S30):** Implementé `DatabaseConfig.java` abstrayendo los parámetros de conexión a través de variables de entorno con valores por defecto seguros.
2. **Adaptador JDBC con PreparedStatement (M10):** Construí `ProductoJdbcRepository.java` cumpliendo el contrato de `ProductoRepository` con consultas SQL parametrizadas, gestión determinista de conexiones mediante `try-with-resources` y sentencias upsert (`ON CONFLICT (id) DO UPDATE`).
3. **Desacoplamiento Hexagonal:** Mantuve la capa de dominio y aplicación totalmente aisladas de librerías de persistencia relacional (`java.sql.*`).

---

## 2. Decisiones Técnicas y Axiología
* **Prevención de Inyección SQL:** Uso estricto de `PreparedStatement` con marcadores posicionales para todos los parámetros de entrada.
* **Control de Fugas de Recursos:** Toda conexión, declaración y conjunto de resultados se cierran deterministamente dentro del bloque de gestión de recursos.
* **Protección de Credenciales:** Ninguna contraseña productiva o dato sensible fue incluido en el código fuente.

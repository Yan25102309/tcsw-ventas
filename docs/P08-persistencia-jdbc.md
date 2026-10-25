# P08: Reporte Integrador de Persistencia JDBC y PostgreSQL (M10 / S28-S30)

* **Experiencia Educativa:** Tecnologías para la Construcción de Software (NRC 19234)
* **Docente Responsable:** Dr. Gabriel Rodríguez Vásquez
* **Autores:** Ana María Reyes Hernández y Victor Gustavo Reyes Vidal
* **Proyecto:** Prototipo de Sistema de Ventas (`tcsw-ventas`)

---

## 1. Introducción y Propósito del Incremento
El presente reporte consolida la integración de la persistencia relacional en el prototipo de sistema de ventas (`tcsw-ventas`). Se implementó un **Adaptador de Salida (*Driven Adapter*)** en `adapter.jdbc` conectado a un motor **PostgreSQL 16** mediante **Docker Compose**, preservando el desacoplamiento estricto del dominio bajo los lineamientos de la **Arquitectura Hexagonal**.

---

## 2. Decisiones de Diseño y Mapeo Relacional
* **Frontera de Arquitectura:** El puerto `ProductoRepository` se mantiene independiente de librerías relacionales (`java.sql.*`).
* **SQL Parametrizado:** Uso obligatorio de `PreparedStatement` con marcadores `?` para prevenir inyecciones SQL y garantizar tipado fuerte en operaciones DML.
* **Gestión Determinista de Recursos:** Uso de `try-with-resources` para asegurar el cierre automático de `Connection`, `PreparedStatement` y `ResultSet`.
* **Integridad Relacional Declarativa:** Restricciones `CHECK (precio >= 0.00)` y `CHECK (existencia >= 0)` en PostgreSQL que actúan como defensa secundaria del sistema frente a estados inconsistentes.

---

## 3. Protocolo de Pruebas y Resultados
| Escenario de Prueba | Resultado Esperado | Estado |
| :--- | :--- | :---: |
| **Persistencia Exitosa** | El producto se guarda en PostgreSQL vía sentencia parametrizada y se recupera preservando invariantes. | **VERIFICADO** |
| **Consulta Inexistente** | La búsqueda de un identificador no registrado devuelve `Optional.empty()` sin excepciones SQL. | **VERIFICADO** |
| **Integridad Relacional** | El sistema y motor relacional rechazan inserciones que vulneran las restricciones de consistencia. | **VERIFICADO** |

---

## 4. Verificación Ejecutable
Se ejecutó la suite de pruebas unitarias y de integración automatizadas con Maven:
mvn clean test (BUILD SUCCESS con 0 fallos y 0 errores).

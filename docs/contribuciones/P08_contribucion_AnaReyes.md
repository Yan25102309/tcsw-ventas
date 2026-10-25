# Reporte de Contribución Individual — P08 (M10 / S28-S30)

* **Estudiante:** Ana María Reyes Hernández
* **Proyecto:** `tcsw-ventas` (Experiencia Educativa TCSW-19234)
* **Rama de trabajo:** `feat/p08-base-relacional-docker` y `feat/p08-adaptador-hibernate-orm`

---

## 1. Responsabilidades Asignadas y Resueltas
1. **Infraestructura PostgreSQL en Docker (Issue #4):** Configuré `compose.yml` especificando la imagen PostgreSQL 16, persistencia de volúmenes con `pgdata` y chequeo de salud `pg_isready` (de acuerdo con S30).
2. **Esquema Relacional DDL y Mapeo:** Creé `infrastructure/database/001_schema.sql` con restricciones `CHECK` para garantizar integridad relacional y redacté `docs/modelo-relacional.md`.
3. **Mapeador ORM e Hibernate (Issue #6):** Implementé `ProductoEntity.java` y `ProductoHibernateRepository.java` gestionando las sesiones y transacciones con `commit()` y `rollback()` sin contaminar las clases de dominio (S28, S29).

---

## 2. Justificación Técnica de Arquitectura
* **Desacoplamiento Domain vs. Infrastructure:** Mantuve la clase `domain.Producto` libre de anotaciones `@Entity` de JPA ya que la traducción ocurre en los adaptadores relacionales para proteger las invariantes.
* **Ciclo de Vida (S28):** Las instancias pasan de transitorias en memoria a administradas en el contexto de Hibernate y finalmente separadas al cerrar deterministamente la sesión.

---

## 3. Evidencias de Comprobación y Commits
* **Commit Issue #38:** `infra(p08): base relacional PostgreSQL 16, DDL schema y modelo relacional`
* **Commit Issue #39:** `feat(orm): mapeo Hibernate S28/S29 y repositorio JPA desacoplado`


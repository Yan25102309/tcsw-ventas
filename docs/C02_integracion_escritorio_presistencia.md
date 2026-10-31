# Reporte Integrador de Arquitectura y Persistencia — Corte C02

**Proyecto:** `tcsw-ventas`  
**Versión de Entrega:** `v0.2-escritorio`  
**Fecha de Emisión:** 30 de Octubre de 2026  
**Tecnologías:** Java 11, Swing, Hibernate ORM 5.6, PostgreSQL 15, Docker Compose, JUnit 5, JaCoCo, SonarQube, Maven.

---

## 1. Resumen Ejecutivo
El incremento **C02** consolida la integración vertical entre el cliente de escritorio desarrollado en **Java Swing** y la capa de persistencia relacional con **PostgreSQL**, gestionada a través del framework **Hibernate ORM** dentro de una arquitectura hexagonal limpia (*Ports and Adapters*).

Durante este corte se logró la ejecución atómica de transacciones comerciales sobre el agregado `Venta`, garantizando la consistencia del inventario mediante operaciones de reversión (*rollback*) ante fallos de persistencia, manteniendo la interfaz de usuario responsiva mediante procesamiento asíncrono y superando los estándares de calidad definidos en el Quality Gate de SonarQube.

---

## 2. Decisiones de Diseño y Justificación Arquitectónica

### 2.1. Desacoplamiento Hexagonal (Puertos y Adaptadores)
- **Dominio Aislado:** Las entidades de negocio (`Venta`, `DetalleVenta`, `Producto`) no poseen dependencias directas con librerías externas ni anotaciones `@Entity` de JPA.
- **Inversión de Dependencias:** La capa de aplicación interactúa únicamente con las interfaces `VentaRepository` y `ProductoRepository`. Los adaptadores relacionales `VentaHibernateRepository` y `ProductoHibernateRepository` implementan dichos puertos en la infraestructura.

### 2.2. Procesamiento Asíncrono en la Interfaz Gráfica (`SwingWorker`) — Criterio S33 / M09
- Para evitar el congelamiento de la interfaz de usuario (*Event Dispatch Thread* - EDT) durante las operaciones I/O contra PostgreSQL, el controlador Swing (`VentaController`) encapsula la llamada a `ConfirmarVentaService` dentro del método `doInBackground()` de un `SwingWorker`.
- Las actualizaciones de la interfaz y el despliegue de diálogos de confirmación o error (`JOptionPane`) se realizan dentro del método `done()`, ejecutándose de manera segura de regreso en el EDT.

### 2.3. Transaccionalidad Atómica y Mecanismo de Reversión (`Rollback`) — Criterio S31
- La confirmación de una venta implica modificar múltiples entidades (`VentaEntity`, `DetalleVentaEntity` y `ProductoEntity`).
- En `VentaHibernateRepository`, la operación se delimita explícitamente con `session.beginTransaction()` y `tx.commit()`. Ante cualquier fallo durante el procesamiento de partidas o inconsistencia de datos, se ejecuta `tx.rollback()`, garantizando que PostgreSQL no almacene registros huérfanos o parciales.

### 2.4. Mapeo ORM y Contención en Contenedor Docker
- El esquema relacional se define en el script DDL `infrastructure/database/002_schema_venta.sql`, estableciendo restricciones de clave foránea `ON DELETE CASCADE`.
- Hibernate administra el ciclo de vida de la sesión mediante `hibernate.cfg.xml`, mapeando relaciones de una a muchas (`@OneToMany`) con cascada total (`CascadeType.ALL`) y eliminación de huérfanos (`orphanRemoval = true`).

---

## 3. Matriz de Trazabilidad de Tareas e Issues (Corte C02)

| Issue # | Componente / Descripción de la Tarea | Responsable | Rama Git | Commit Clave | Pull Request | Estado |
| :---: | :--- | :---: | :--- | :--- | :---: | :---: |
| **#48** | Mapeo DDL (`002_schema_venta.sql`), Entidades JPA (`VentaEntity`, `DetalleVentaEntity`) y `VentaHibernateRepository` | Ana Reyes | `feat/c02-hibernate-venta-repo` | `feat(hibernate): implementacion de entidades JPA y VentaHibernateRepository` | PR #49 | **Merged** |
| **#50** | Caso de uso atómico `ConfirmarVentaService` y suite de pruebas de reversión `VentaHibernateRepositoryTest` | Ana Reyes | `feat/c02-confirmar-venta-service` | `feat(application): ConfirmarVentaService transaccional y pruebas de cobertura` | PR #51 | **Merged** |
| **#52** | Interfaz gráfica `VentaView`, `VentaPanel` y controlador `VentaController` asíncrono con `SwingWorker` | Victor Gustavo | `feat/c02-swing-ventas-ui` | `feat(swing): interfaz VentaView y VentaController asincrono` | PR #54 | **Merged** |
| **#53** | Composición de dependencias en `MainApp.java`, script de verificación M11 y reporte integrador C02 | Victor Gustavo | `feat/c02-integracion-main-reporte` | `feat(infrastructure): composicion de dependencias en SwingApp y reporte C02` | PR # | **Meged** |

---

## 4. Resultados de Verificación Automatizada y Calidad de Código

### 4.1. Pruebas Unitarias e Integración (JUnit 5)
- **Pruebas ejecutadas:** 62 tests automatizados.
- **Fallos / Errores:** 0.
- **Resultado de Compilación:** `BUILD SUCCESS`.

### 4.2. Cobertura de Código (JaCoCo)
- **Porcentaje global alcanzado:** **81.0>%** de cobertura sobre las líneas de código de las capas de dominio, aplicación y adaptadores.
- **Requisito mínimo estipulado:** > 80.0%.

### 4.3. SonarQube Quality Gate
- **Estado:** **`PASSED`**.
- **Vulnerabilidades:** 0.
- **Bugs Bloqueantes:** 0.
- **Code Smells:** 0 en las clases modificadas del incremento.

### 4.4. Verificación de Módulo (M11)
- **Resultado:** **`VERIFICADO — TODOS LOS CRITERIOS CUMPLIDOS`**.

---

## 5. Estrategia de Despliegue y Etiquetado (*Tagging*)

Para formalizar el cierre del incremento **C02**, se generó la etiqueta anotada en el repositorio Git apuntando al commit de fusión en la rama `main`:

- **Tag:** `v0.2-escritorio`
- **Mensaje del Tag:** `Release v0.2-escritorio: Integración completa de Cliente Swing con Persistencia Hibernate y PostgreSQL.`

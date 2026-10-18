# Contribución Individual - Estudiante 2 (P07)

* **Estudiante:** Victor Gustavo Reyes Vidal (`Novaxll`)
* **Actividad:** P07 — Cliente Swing (M08 / M09)
* **Rama Git:** `feat/p07-swing-registro`
* **Entregables Desarrollados:**
  - `src/main/java/adapter/swing/RegistrarProductoView.java`
  - `src/main/java/adapter/swing/ProductoController.java`
  - `src/main/java/adapter/swing/SwingApp.java`
  - `src/test/java/adapter/swing/ProductoControllerTest.java`
* **Resumen de Implementación:**
  Construcción del formulario de registro de productos con soporte de accesibilidad (`AccessibleContext`), controlador MVC con validación sintáctica de entradas y sanitización de errores, ensamblado explícito en el hilo EDT (`SwingUtilities.invokeLater`) dentro de `SwingApp`, y pruebas unitarias en JUnit 5 tanto para el flujo esperado como para casos de error de formato y de dominio.

* **Experiencia Educativa:** Tecnologías para la Construcción de Software (Clave TCSW-19234)
* **Docente / Responsable Académico:** Dr. Gabriel Rodríguez Vásquez
* **Integrantes / Autores:** Ana María Reyes Hernández y Victor Gustavo Reyes Vidal
* **Proyecto:** Prototipo de Sistema de Ventas (`tcsw-ventas`)

---

## 1. Decisiones de Arquitectura y Desacoplado de la UI

### 1.1. Adaptador de Entrada (*Driving Adapter*)
El paquete `adapter.swing` se diseñó bajo el patrón de **Arquitectura Hexagonal (Puertos y Adaptadores)** como un adaptador exterior de entrada (*Driving Adapter*):
* **Separación de Responsabilidades (MVC):**
  * **Vistas (`RegistrarProductoView`, `VentaView`):** Heredan de `JPanel` y se encargan únicamente del despliegue gráfico y captura de componentes de Swing (`JTextField`, `JTable`, `JComboBox`).
  * **Controladores (`ProductoController`, `VentaController`):** Actúan como mediadores que traducen los gestos e interacciones del usuario hacia comandos neutros (`RegistrarProductoCommand`) sin acoplar las vistas con las entidades del negocio.
* **Aislamiento e Inmutabilidad del Núcleo:** El paquete de dominio (`domain`) y aplicación (`application`) no contienen importaciones hacia `javax.swing.*` ni `java.awt.*`, preservando las invariantes y reglas de negocio intactas.

---

## 2. Concurrencia, Hilo EDT y Responsividad

### 2.1. Gestión del Event Dispatch Thread (EDT)
* **Seguridad de Hilos (*Thread-Safety*):** La inicialización de la interfaz en `SwingApp.java` (*Composition Root*) se delega al hilo **EDT** utilizando `SwingUtilities.invokeLater()` para evitar condiciones de carrera en el renderizado.

### 2.2. Procesamiento Asíncrono con `SwingWorker`
* **Operaciones No Bloqueantes:** Para evitar el congelamiento de la ventana durante la ejecución de tareas intensivas o llamadas al servicio, `VentaController` hace uso de **`SwingWorker`**:
  * `doInBackground()`: Ejecuta la lógica pesada y el cálculo de promociones (`PoliticaDescuentoFactory`) en un hilo de fondo fuera del EDT.
  * `done()`: Retorna el resultado al hilo EDT de forma segura mediante `get()` y actualiza el modelo visual y etiquetas de total neto.

---

## 3. Máquina de estados visuales y sanitización de errores

### 3.1. Control de Estados UI y Prevención de Reenvíos
* Para garantizar una interacción segura y evitar el envío duplicado por doble clic, la interfaz implementa una máquina de estados visuales:
  * **Estado LISTO:** Controles activos y disponibles para interacción del usuario.
  * **Estado EJECUTANDO:** Los botones de acción (`btnCalcularTotal`) y combos se deshabilitan temporalmente durante el procesamiento en segundo plano.
  * **Restauración:** Al finalizar la tarea (éxito o fallo), el bloque `finally` restaura el estado habilitado de los componentes.

### 3.2. Mensajes Sanitizados de Usuario
* Las excepciones numéricas sintácticas (`NumberFormatException`) y de ejecución del worker (`ExecutionException`) se capturan en los controladores y se informan mediante cuadros `JOptionPane` amigables y descriptivos, evitando la exposición de *stack traces* o rutas internas en la UI.

---

## 4. Protocolo Visual y Checklist de Escenarios de Prueba

| Escenario | Acción de Usuario / Entrada | Resultado Esperado en Pantalla | Estado |
| :--- | :--- | :--- | :---: |
| **1. Alta de Producto Exitosa** | Captura de ID, Nombre, Precio y Existencia válida en `RegistrarProductoView`. | Mensaje `JOptionPane` informa éxito y limpia los campos del formulario. | **VERIFICADO** |
| **2. Error Sintáctico de Entrada** | Inserción de letras en el campo numérico de Precio o ID. | `JOptionPane` muestra advertencia de formato inválido sin cerrar la aplicación. | **VERIFICADO** |
| **3. Despliegue en JTable** | Agregar partidas a la venta actual. | La `JTable` añade las filas con subtotales sin permitir edición directa. | **VERIFICADO** |
| **4. Cálculo Asíncrono Worker** | Selección de política `CLIENTE_FRECUENTE` y procesamiento. | La UI se deshabilita temporalmente, el estado indica "Procesando..." y actualiza el total en EDT. | **VERIFICADO** |

---

## 5. Comprobación Reproducible de Módulos

El proyecto fue compilado y verificado mediante la suite de pruebas unitarias automatizadas:

```powershell
# 1. Ejecución de la suite de pruebas unitarias JUnit 5
mvn clean test
# Resultado: BUILD SUCCESS (0 fallos, 0 errores)


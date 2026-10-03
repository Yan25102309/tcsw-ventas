# P07: Cliente Swing Desacoplado (M08)

* **Experiencia Educativa:** Tecnologías para la Construcción de Software
* **Módulo:** M08 (Cliente Swing I) | **Actividad:** P07
* **Proyecto:** `tcsw-ventas`

## 1. Aplicación del Patrón MVC (Model-View-Controller)
El cliente gráfico se estructura bajo el patrón MVC alojado íntegramente en el paquete `adapter.swing`:
* **View (`RegistrarProductoView`, `VentaView`):** Clases que heredan de `JPanel` construyendo únicamente la interfaz gráfica mediante componentes Swing. No contienen lógica de negocio.
* **Controller (`ProductoController`, `VentaController`):** Adaptadores de entrada que capturan eventos de la vista, traducen entradas a comandos DTO neutros e invocan la capa de aplicación.
* **Model (`domain.*`, `application.*`):** El núcleo de aplicación permanece aislado de tipos gráficos.

## 2. Aplicación del Patrón Facade (Fachada de Aplicación)
El servicio `ProductoService` y la interfaz `RegistrarProductoUseCase` actúan como una Fachada de Aplicación. Abstraen la orquestación del dominio y la persistencia, ofreciendo una API neutra e inmutable para los controladores Swing.

## 3. Análisis Crítico y Descarte del Patrón Singleton
Durante el diseño de este corte, **se descartó el patrón Singleton** para la gestión de repositorios, servicios o controladores debido a los siguientes factores:
* **Estado Global Oculto:** Singleton introduce dependencias globales implícitas en el código, oscureciendo las relaciones entre componentes.
* **Dificultad de Pruebas Unitarias:** Dificulta el aislamiento de tests automatizados al compartir estado global entre ejecuciones paralelas de JUnit 5.
* **Violación de Inversión de Dependencias:** Acopla las clases directamente a la instanciación estática en lugar de abstraer interfaces.
* **Solución Aplicada:** Se utiliza **Inyección de Dependencias por Constructor** administrada de forma limpia dentro del punto de entrada `SwingApp.java` (*Composition Root*), asegurando la thread-safety dentro del Event Dispatch Thread (EDT).

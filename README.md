# P01: Ambiente Reproducible y Producto Java Maven
**Estudiante:** Ana Maria Reyes Hernandez  
**Experiencia Educativa:** Tecnologaas para la Construccion de Software  
**NRC:** 19234  
**Docente:** Dr. Gabriel Rodriguez Vasquez  
**Periodo:** Agosto 2026 - Febrero 2027  

# tcsw-ventas: Incremento del Modelo de Dominio "Producto"

Este repositorio contiene el **primer incremento funcional y verificado** del prototipo de sistema de ventas. Implementa la clase de negocio `Producto` protegiendo sus reglas de consistencia interna (invariantes de dominio) mediante encapsulamiento estricto, junto con una suite de pruebas unitarias automatizadas con **JUnit 5**.

---

## 1. Requisitos Previos del Sistema (¿Qué debes tener instalado?)

Antes de ejecutar cualquier comando, debes asegurar que tu computadora (Windows 11) tiene las siguientes tres herramientas instaladas y configuradas en tus **Variables de Entorno del Sistema**:

* **Java Development Kit (JDK)**
  * **Version Requerida:** 11
  * **Comando de Verificacion:** `java -version`
  * **Resultado Esperado en Consola:** `openjdk version "11.0.32"` o similar.

* **Java Compiler**
  * **Version Requerida:** 11
  * **Comando de Verificacion:** `javac -version`
  * **Resultado Esperado en Consola:** `javac 11.0.32` .

* **Apache Maven**
  * **Version Requerida:** 3.9.9
  * **Comando de Verificacion:** `mvn -version`
  * **Resultado Esperado en Consola:** Debe indicar que se ejecuta bajo el runtime de Java 11.

* **Git**
  * **Version Requerida:** 2.54.0 (o superior)
  * **Comando de Verificacion:** `git --version`
  * **Resultado Esperado en Consola:** `git version 2.54.0.windows.1` o similar.
```

```

> **IMPORTANTE (La trampa común):**  
> A veces, al ejecutar `mvn -version`, Maven reporta que está usando otra versión de Java instalada en tu equipo (como Java 17 o 21), aunque tu terminal use Java 11. **Ambos deben usar Java 11** para evitar problemas de compilación. Si no coinciden, debes ajustar tu variable de entorno `JAVA_HOME` en Windows.

---

## 2. Estructura Exacta del Proyecto

Para que Maven localice automáticamente tu código y tus pruebas sin configuraciones manuales, el proyecto sigue estrictamente la **convención de estructura de directorios estándar de Maven**:

```text
tcsw-ventas/                      <-- Carpeta raíz de tu proyecto
│
├── .gitignore                   <-- Archivo para evitar subir basura o temporales a Git
├── pom.xml                      <-- Archivo de configuración central de Maven 
├── README.md                    <-- Este manual de instrucciones para desarrolladores
│
└── src/                         <-- Carpeta con todo el código fuente del proyecto
    ├── main/                    <-- Código que se empaqueta para producción
    │   └── java/
    │       └── domain/          <-- Paquete del dominio de negocio
    │           └── Producto.java <-- Clase de negocio (Contiene lógica y validaciones)
    │
    └── test/                    <-- Código exclusivo para pruebas automatizadas
        └── java/
            └── domain/          <-- Paquete de pruebas (Espejo de main)
                └── ProductoTest.java <-- Clase JUnit 5 que prueba a "Producto.java"
```

---

## 3. Guía de Inicio

Sigue estas instrucciones en orden para descargar, compilar, probar y empaquetar el proyecto en tu máquina local.

### Paso 1: Clonar el proyecto de forma local
Abre tu terminal de Windows (PowerShell, CMD o Git Bash), navega a la carpeta donde deseas guardar tus proyectos y clona el repositorio ejecutando:
```bash
git clone https://github.com/Yan25102309/tcsw-ventas.git
```
Una vez terminada la descarga, **entra obligatoriamente a la carpeta del proyecto** con:
```bash
cd tcsw-ventas
```

---

### Paso 2: Validar el descriptor de Maven (`mvn validate`)
Este comando lee tu archivo `pom.xml`, valida que la estructura sea correcta y descarga de internet la biblioteca **JUnit 5** necesaria para las pruebas.

**Ejecuta en tu terminal:**
```bash
mvn validate
```
*   **¿Qué debes ver en pantalla?**  
    Tras descargar las dependencias, la terminal debe mostrar al final la confirmación:
    ```text
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    ```

---

### Paso 3: Compilar el código (`mvn compile`)
Este comando toma el archivo de código fuente `Producto.java` y lo traduce a bytecode ejecutable por la JVM.

**Ejecuta en tu terminal:**
```bash
mvn compile
```
*   **¿Qué hace internamente?**  
    Maven crea automáticamente una carpeta temporal llamada `target/classes/` en tu raíz y coloca allí el archivo compilado `Producto.class`.Git ignorará esta carpeta gracias al `.gitignore`.

---

### Paso 4: Ejecutar las pruebas unitarias (`mvn test`)
Este comando compila tus pruebas unitarias y las ejecuta usando el plugin **Surefire** para comprobar que el código de producción cumple con todas las reglas de negocio.

**Ejecuta en tu terminal:**
```bash
mvn test
```
*   **¿Qué debes observar en pantalla?**  
    Deberías ver un reporte limpio indicando que se corrieron **6 pruebas** y ninguna falló:
    ```text
    [INFO] Running domain.ProductoTest
    [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.043 s -- in domain.ProductoTest
    [INFO] 
    [INFO] Results:
    [INFO] 
    [INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
    [INFO] 
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    ```

---

### Paso 5: Limpieza y Empaquetado completo (`mvn clean package`)
Este comando limpia todas las compilaciones anteriores (borra la carpeta temporal `target/`), compila todo desde cero, ejecuta las pruebas automatizadas y genera un archivo ejecutable empaquetado final tipo **JAR**.

**Ejecuta en tu terminal:**
```bash
mvn clean package
```
*   **Resultado esperado:**  
    En tu directorio raíz aparecerá la carpeta `/target/` y dentro de ella verás el archivo empaquetado final:  
    `target/tcsw-ventas-1.0-SNAPSHOT.jar`

---

## 4. Solución de Fallos Comunes (Troubleshooting)

### Problema 1: Maven ejecuta 0 pruebas ("Tests run: 0")
*   **Causa:** El plugin Surefire de Maven busca archivos de prueba que sigan un patrón específico. Si tu archivo se llama `PruebaProducto.java` o `ProductoPrueba.java`, Maven lo ignorará.
*   **Solución:** Asegúrate de que el archivo esté en la carpeta `src/test/java/domain/` y que su nombre termine exactamente con el sufijo **`Test.java`** (ejemplo: `ProductoTest.java`).

### Problema 2: Error "Unsupported class file major version"
*   **Causa:** Estás intentando compilar o ejecutar el proyecto con una versión de Java distinta a la declarada en el `pom.xml`.
*   **Solución:** Ejecuta `mvn -version` en tu terminal. Revisa qué JDK está utilizando Maven. Si no es el JDK 11, configura de forma correcta tu variable de entorno `JAVA_HOME` apuntando a la ruta de instalación de tu Java 11 en Windows (ej. `C:\Program Files\Java\jdk-11.x.x`).

### Problema 3: Error de dependencias no encontradas en Windows
*   **Causa:** Tu computadora no tiene acceso a internet para descargar JUnit 5 por primera vez, o tu firewall de Windows está bloqueando las peticiones de Maven hacia los servidores centrales (Maven Central Repository).
*   **Solución:** Asegúrate de estar conectado a internet en tu primera compilación para que Maven descargue las librerías necesarias. Una vez descargadas, podrás trabajar de forma local offline sin ningún problema.

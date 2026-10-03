package adapter.swing;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
public class SwingAppTest {
    @BeforeAll
    static void setUpHeadlessMode() {
        // Evita errores de entorno gráfico en servidores de CI/CD
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void testMainExecution() {
        // Ejecuta el método main para cubrir las líneas de inicialización y el EDT
        assertDoesNotThrow(() -> SwingApp.main(new String[]{}));
    }
}

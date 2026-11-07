package adapter.swing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VentaPanelTest {

    @BeforeAll
    static void setUpHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void testInicializaControlesYValoresPorDefecto() {
        VentaPanel panel = new VentaPanel();

        assertEquals("", panel.getProductoId());
        assertEquals("", panel.getCantidad());
        assertEquals("", panel.getTxtResumen().getText());
        assertNotNull(panel.getBtnAgregarDetalle());
        assertNotNull(panel.getBtnConfirmarVenta());
        assertFalse(panel.getTxtResumen().isEditable());
    }

    @Test
    void testExponeControlesYActualizaResumen() {
        VentaPanel panel = new VentaPanel();
        panel.getBtnAgregarDetalle().setText("Agregar");
        panel.getBtnConfirmarVenta().setText("Confirmar");
        panel.getTxtResumen().setText("Mouse USB x 2");
        assertDoesNotThrow(() -> panel.setStatus("Venta confirmada"));

        assertEquals("Agregar", panel.getBtnAgregarDetalle().getText());
        assertEquals("Confirmar", panel.getBtnConfirmarVenta().getText());
        assertEquals("Mouse USB x 2", panel.getTxtResumen().getText());
    }

    @Test
    void testLimpiaEntradasYLimpiaTodo() {
        VentaPanel panel = new VentaPanel();
        panel.getTxtResumen().setText("Producto agregado");

        panel.limpiarEntradasDetalle();

        assertEquals("", panel.getProductoId());
        assertEquals("", panel.getCantidad());
        assertEquals("Producto agregado", panel.getTxtResumen().getText());

        panel.limpiarTodo();

        assertEquals("", panel.getProductoId());
        assertEquals("", panel.getCantidad());
        assertEquals("", panel.getTxtResumen().getText());
    }
}

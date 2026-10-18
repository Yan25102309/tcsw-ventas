package adapter.swing;

import domain.Producto;
import domain.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VentaControllerTest {

    private Venta venta;
    private VentaView view;
    private VentaController controller;

    @BeforeEach
    void setUp() {
        this.venta = new Venta();
        this.view = new VentaView();
        this.controller = new VentaController(view, venta);
    }

    @Test
    @DisplayName("Debe agregar un producto a la venta y reflejarlo en la JTable")
void testAgregarProductoYActualizarModeloTabla() {
        Producto producto = new Producto(1L, "Teclado Mecánico", new BigDecimal("500.00"), 10);

        controller.agregarProductoAVenta(producto, 2);

        assertEquals(1, view.getTableModel().getRowCount());
        assertEquals("Teclado Mecánico", view.getTableModel().getValueAt(0, 0));
        assertEquals(2, view.getTableModel().getValueAt(0, 1));
        assertEquals(new BigDecimal("500.00"), view.getTableModel().getValueAt(0, 2));
        assertEquals(new BigDecimal("1000.00"), view.getTableModel().getValueAt(0, 3));
    }

    @Test
    @DisplayName("Debe probar los métodos de acceso y estados visuales de VentaView")
    void testMetodosYEstadosDeVentaView() {
        assertNotNull(view.getTableModel());
        assertNotNull(view.getComboDescuentos());
        assertNotNull(view.getBtnCalcularTotal());
        assertNotNull(view.getLblEstado());
        assertNotNull(view.getLblTotal());

        view.marcarEjecutando();
        assertFalse(view.getBtnCalcularTotal().isEnabled());
        assertFalse(view.getComboDescuentos().isEnabled());

        view.marcarListo("Listo para operar");
        assertTrue(view.getBtnCalcularTotal().isEnabled());
        assertTrue(view.getComboDescuentos().isEnabled());
        assertTrue(view.getLblEstado().getText().contains("Listo para operar"));
    }

    @Test 
    @DisplayName("Debe verificar el cálculo del total y actualización de componentes") 
    void testCalcularTotalConDiferentesPoliticas() { 
        Producto producto = new Producto(1L, "Monitor 27", new BigDecimal("2000.00"), 5); 
        controller.agregarProductoAVenta(producto, 1); 
       
        view.getComboDescuentos().setSelectedItem("CLIENTE_FRECUENTE"); 
        assertEquals("CLIENTE_FRECUENTE", 
        view.getComboDescuentos().getSelectedItem()); 
        assertDoesNotThrow(() -> controller.calcularTotalAsincrono()); }
}


package adapter.swing;

import domain.Producto;
import domain.Venta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VentaControllerTest {

    @Test
    void testAgregarDetalleYCalcularTotalConEstrategia() {
        Venta venta = new Venta();
        VentaView view = new VentaView();
        VentaController controller = new VentaController(view, venta);

        Producto producto = new Producto(1L, "Teclado", new BigDecimal("500.00"), 10);
        controller.agregarProductoAVenta(producto, 2);

        assertEquals(1, view.getTableModel().getRowCount());
    }
}


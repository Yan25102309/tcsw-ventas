package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DetalleVentaTest {

    @Test
    void testCrearDetalleValido() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.00"), 10);
        final DetalleVenta detalle = new DetalleVenta(producto, 3);
        
        assertEquals(producto, detalle.getProducto());
        assertEquals(3, detalle.getCantidad());
        assertEquals(new BigDecimal("120.00"), detalle.getPrecioCapturado());
        assertEquals(new BigDecimal("360.00"), detalle.getSubtotal());
    }

    @Test
    void testRechazarProductoNuloEnDetalle() {
        assertThrows(IllegalArgumentException.class, () -> new DetalleVenta(null, 5));
    }

    @Test
    void testRechazarCantidadInvalidaEnDetalle() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.00"), 10);
        assertThrows(IllegalArgumentException.class, () -> new DetalleVenta(producto, 0));
        assertThrows(IllegalArgumentException.class, () -> new DetalleVenta(producto, -1));
    }
}
package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoTest {

    @Test
    void testCrearProductoValido() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 50);
        assertEquals(1L, producto.getId());
        assertEquals("Cafe Veracruz", producto.getNombre());
        assertEquals(new BigDecimal("120.50"), producto.getPrecio());
        assertEquals(50, producto.getExistencia());
    }

    @Test
    void testRechazarIdInvalido() {
        final BigDecimal precio = new BigDecimal("10.00");
        assertThrows(IllegalArgumentException.class, () -> new Producto(0L, "Cafe", precio, 10));
        assertThrows(IllegalArgumentException.class, () -> new Producto(-5L, "Cafe", precio, 10));
    }

    @Test
    void testRechazarNombreInvalido() {
        final BigDecimal precio = new BigDecimal("10.00");
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, null, precio, 10));
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "   ", precio, 10));
    }

    @Test
    void testRechazarPrecioInvalido() {
        final BigDecimal precioNegativo = new BigDecimal("-1.50");
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Cafe", null, 10));
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Cafe", precioNegativo, 10));
    }

    @Test
    void testRechazarExistenciaNegativa() {
        final BigDecimal precio = new BigDecimal("10.00");
        assertThrows(IllegalArgumentException.class, () -> new Producto(1L, "Cafe", precio, -1));
    }

    @Test
    void testDescontarCantidadValida() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        producto.descontar(4);
        assertEquals(6, producto.getExistencia());
    }

    @Test
    void testRechazarCantidadDescuentoInvalida() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        assertThrows(IllegalArgumentException.class, () -> producto.descontar(0));
        assertThrows(IllegalArgumentException.class, () -> producto.descontar(-3));
    }

    @Test
    void testRechazarDescuentoMayorQueExistencia() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 5);
        assertThrows(IllegalStateException.class, () -> producto.descontar(6));
    }

    @Test
    void testRechazoNoModificaEstadoPrevio() {
        final Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 8);
        assertThrows(IllegalStateException.class, () -> producto.descontar(10));
        assertEquals(8, producto.getExistencia());
    }
}
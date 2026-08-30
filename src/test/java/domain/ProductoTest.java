package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductoTest {

    @Test
    void testCrearProductoValido() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 50);
        
        assertEquals(1L, producto.getId());
        assertEquals("Cafe Veracruz", producto.getNombre());
        assertEquals(new BigDecimal("120.50"), producto.getPrecio());
        assertEquals(50, producto.getExistencia());
    }

    @Test
    void testRechazarPrecioNegativo() {
        final BigDecimal precioNegativo = new BigDecimal("-5.00");
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Producto(1L, "Cafe Veracruz", precioNegativo, 50)
        );
    }

    @Test
    void testDescontarCantidadValida() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        producto.descontar(4);
        assertEquals(6, producto.getExistencia());
    }

    @Test
    void testRechazarCantidadDescuentoInvalida() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        
        assertThrows(IllegalArgumentException.class, () -> producto.descontar(0));
        assertThrows(IllegalArgumentException.class, () -> producto.descontar(-3));
    }

    @Test
    void testRechazarDescuentoMayorQueExistencia() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 5);
        
        assertThrows(IllegalStateException.class, () -> producto.descontar(6));
    }

    @Test
    void testRechazoNoModificaEstadoPrevio() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 8);
        
        assertThrows(IllegalStateException.class, () -> producto.descontar(10));
        assertEquals(8, producto.getExistencia());
    }
}
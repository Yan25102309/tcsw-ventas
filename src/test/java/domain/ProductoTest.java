package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    public void testCrearProductoValido() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 50);
        
        assertEquals(1L, producto.getId());
        assertEquals("Cafe Veracruz", producto.getNombre());
        assertEquals(new BigDecimal("120.50"), producto.getPrecio());
        assertEquals(50, producto.getExistencia());
    }

    @Test
    public void testRechazarPrecioNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producto(1L, "Cafe Veracruz", new BigDecimal("-5.00"), 50);
        }, "Deberia lanzar IllegalArgumentException si el precio es negativo");
    }

    @Test
    public void testDescontarCantidadValida() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        producto.descontar(4);
        assertEquals(6, producto.getExistencia(), "La existencia restante deberia ser 6");
    }

    @Test
    public void testRechazarCantidadDescuentoInvalida() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        
        assertThrows(IllegalArgumentException.class, () -> {
            producto.descontar(0);
        }, "Deberia lanzar IllegalArgumentException si la cantidad es 0");

        assertThrows(IllegalArgumentException.class, () -> {
            producto.descontar(-3);
        }, "Deberia lanzar IllegalArgumentException si la cantidad es negativa");
    }

    @Test
    public void testRechazarDescuentoMayorQueExistencia() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 5);
        
        assertThrows(IllegalStateException.class, () -> {
            producto.descontar(6);
        }, "Deberia lanzar IllegalStateException si la cantidad supera la existencia");
    }

    @Test
    public void testRechazoNoModificaEstadoPrevio() {
        Producto producto = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 8);
        
        assertThrows(IllegalStateException.class, () -> {
            producto.descontar(10);
        });
        
        assertEquals(8, producto.getExistencia(), "La existencia debe permanecer intacta tras un rechazo");
    }
}
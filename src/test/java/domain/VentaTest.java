package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VentaTest {

    @Test
    void testAgregarDetalleExitosoYCalcularTotal() {
        final Venta venta = new Venta();
        final Producto cafe = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 20);
        final Producto dona = new Producto(2L, "Dona Chocolate", new BigDecimal("18.00"), 15);

        venta.agregarDetalle(cafe, 2);
        venta.agregarDetalle(dona, 5);

        assertEquals(2, venta.getDetalles().size());
        assertEquals(new BigDecimal("331.00"), venta.calcularTotal());
        assertEquals(18, cafe.getExistencia());
        assertEquals(10, dona.getExistencia());
    }

    @Test
    void testRechazarCantidadDescuentoInvalida() {
        final Venta venta = new Venta();
        final Producto cafe = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);

        assertThrows(IllegalArgumentException.class, () -> venta.agregarDetalle(cafe, 0));
        assertThrows(IllegalArgumentException.class, () -> venta.agregarDetalle(cafe, -5));
        assertEquals(10, cafe.getExistencia());
    }

    @Test
    void testRechazarVentaPorStockInsuficiente() {
        final Venta venta = new Venta();
        final Producto cafe = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 5);

        assertThrows(IllegalStateException.class, () -> venta.agregarDetalle(cafe, 6));
        assertEquals(5, cafe.getExistencia());
        assertEquals(0, venta.getDetalles().size());
    }

    @Test
    void testExposicionColeccionProtegida() {
        final Venta venta = new Venta();
        final Producto cafe = new Producto(1L, "Cafe Veracruz", new BigDecimal("120.50"), 10);
        venta.agregarDetalle(cafe, 2);

        final List<DetalleVenta> detallesExpuestos = venta.getDetalles();
        assertThrows(UnsupportedOperationException.class, () -> detallesExpuestos.clear());
    }

    @Test
    void testRechazarProductoNuloEnVenta() {
        final Venta venta = new Venta();
        assertThrows(IllegalArgumentException.class, () -> venta.agregarDetalle(null, 5));
    }
}
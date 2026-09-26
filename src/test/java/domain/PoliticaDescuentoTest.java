package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescuentoTest {

    @Test
    void testSinDescuentoDevuelveCero() {
        Venta venta = new Venta();
        Producto producto = new Producto(1L, "Café", new BigDecimal("100.00"), 10);
        venta.agregarDetalle(producto, 2);

        PoliticaDescuento sinDescuento = new SinDescuento();
        assertEquals(0, new BigDecimal("0.00").compareTo(sinDescuento.calcularDescuento(venta)));
        assertEquals(0, new BigDecimal("200.00").compareTo(venta.calcularTotal(sinDescuento)));
    }

    @Test
    void testDescuentoPorVolumenAplicaAPartirDeQuinientos() {
        Venta venta = new Venta();
        Producto producto = new Producto(1L, "Café", new BigDecimal("300.00"), 10);
        venta.agregarDetalle(producto, 2);

        PoliticaDescuento descuentoVolumen = new DescuentoPorVolumen();
        assertEquals(0, new BigDecimal("60.00").compareTo(descuentoVolumen.calcularDescuento(venta)));
        assertEquals(0, new BigDecimal("540.00").compareTo(venta.calcularTotal(descuentoVolumen)));
    }
}

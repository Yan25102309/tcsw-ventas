package domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaDescuentoFactoryTest {

    @Test
    void testCrearPoliticaClienteFrecuente() {
        PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica("CLIENTE_FRECUENTE");
        assertTrue(politica instanceof DescuentoClienteFrecuente);

        Venta venta = new Venta();
        Producto p = new Producto(1L, "Molino", new BigDecimal("100.00"), 5);
        venta.agregarDetalle(p, 1);

        // 15% de 100 es 15.00
        assertEquals(new BigDecimal("15.00"), politica.calcularDescuento(venta));
    }

    @Test
    void testTipoInvalidoDevuelveSinDescuento() {
        PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica("DESCONOCIDO");
        assertTrue(politica instanceof SinDescuento);
    }
}

package domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Nueva estrategia concreta: Aplica 15% de descuento fijo para clientes frecuentes.
 * Demuestra extensibilidad del patrón Strategy sin modificar código existente.
 */
public final class DescuentoClienteFrecuente implements PoliticaDescuento {
    private static final BigDecimal PORCENTAJE = new BigDecimal("0.15");

    @Override
    public BigDecimal calcularDescuento(Venta venta) {
        if (venta == null || venta.calcularSubtotal() == null) {
            return BigDecimal.ZERO;
        }
        return venta.calcularSubtotal().multiply(PORCENTAJE).setScale(2, RoundingMode.HALF_UP);
    }
}

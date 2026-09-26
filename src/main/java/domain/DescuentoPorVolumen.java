package domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DescuentoPorVolumen implements PoliticaDescuento {
    private static final BigDecimal UMBRAL_VOLUMEN = new BigDecimal("500.00");
    private static final BigDecimal PORCENTAJE = new BigDecimal("0.10");

    @Override
    public BigDecimal calcularDescuento(Venta venta) {
        if (venta == null) {
            return new BigDecimal("0.00");
        }
        BigDecimal subtotal = venta.calcularSubtotal();
        if (subtotal != null && subtotal.compareTo(UMBRAL_VOLUMEN) >= 0) {
            return subtotal.multiply(PORCENTAJE).setScale(2, RoundingMode.HALF_UP);
        }
        return new BigDecimal("0.00");
    }
}

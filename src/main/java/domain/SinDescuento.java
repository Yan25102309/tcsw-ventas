package domain;

import java.math.BigDecimal;

public final class SinDescuento implements PoliticaDescuento {
    @Override
    public BigDecimal calcularDescuento(Venta venta) {
        return new BigDecimal("0.00");
    }
}

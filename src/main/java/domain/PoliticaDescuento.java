package domain;

import java.math.BigDecimal;

public interface PoliticaDescuento {
    BigDecimal calcularDescuento(Venta venta);
}

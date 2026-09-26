package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Venta {
    private final List<DetalleVenta> detalles = new ArrayList<>();

    public void agregarDetalle(final Producto producto, final int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Error de validacion: La cantidad a agregar debe ser strictly positiva. Recibido: " + cantidad);
        }
        producto.descontar(cantidad);
        this.detalles.add(new DetalleVenta(producto, cantidad));
    }

    public BigDecimal calcularSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (final DetalleVenta detalle : detalles) {
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        return subtotal;
    }

    public BigDecimal calcularTotal() {
        return calcularTotal(new SinDescuento());
    }

    public BigDecimal calcularTotal(final PoliticaDescuento politica) {
        final PoliticaDescuento estrategia = (politica != null) ? politica : new SinDescuento();
        final BigDecimal subtotal = calcularSubtotal();
        final BigDecimal descuento = estrategia.calcularDescuento(this);
        return subtotal.subtract(descuento);
    }

    public List<DetalleVenta> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
}

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
            throw new IllegalArgumentException("La cantidad a agregar debe ser estrictamente positiva. Recibido: " + cantidad);
        }
        producto.descontar(cantidad);
        this.detalles.add(new DetalleVenta(producto, cantidad));
    }

    public BigDecimal calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (final DetalleVenta detalle : detalles) {
            total = total.add(detalle.getSubtotal());
        }
        return total;
    }

    public List<DetalleVenta> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }
}
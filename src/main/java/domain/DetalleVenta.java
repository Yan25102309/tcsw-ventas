package domain;

import java.math.BigDecimal;

public final class DetalleVenta {
    private final Producto producto;
    private final int cantidad;
    private final BigDecimal precioCapturado;

    public DetalleVenta(final Producto producto, final int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioCapturado = producto.getPrecio();
    }

    public BigDecimal getSubtotal() {
        return precioCapturado.multiply(BigDecimal.valueOf(cantidad));
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioCapturado() { return precioCapturado; }
}

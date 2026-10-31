package adapter.hibernate;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
public class DetalleVentaEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private VentaEntity venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    public DetalleVentaEntity() {
    }

    public DetalleVentaEntity(Long id, VentaEntity venta, ProductoEntity producto, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.id = id;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getId() { return id; }
    public VentaEntity getVenta() { return venta; }
    public ProductoEntity getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}

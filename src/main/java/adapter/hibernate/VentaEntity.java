package adapter.hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta")
public class VentaEntity {

    @Id
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleVentaEntity> detalles = new ArrayList<>();

    public VentaEntity() {
    }

    public VentaEntity(Long id, LocalDateTime fechaHora, BigDecimal total) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.total = total;
    }

    public void agregarDetalle(DetalleVentaEntity detalle) {
        detalles.add(detalle);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<DetalleVentaEntity> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaEntity> detalles) { this.detalles = detalles; }
}


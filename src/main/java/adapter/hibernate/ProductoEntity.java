package adapter.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class ProductoEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer existencia;

    public ProductoEntity() {
        // Constructor sin argumentos requerido por la especificacion JPA/Hibernate
    }

    public ProductoEntity(Long id, String nombre, BigDecimal precio, Integer existencia) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.existencia = existencia;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getExistencia() {
        return existencia;
    }
}

package adapter.rest.dto;

import domain.Producto;

import java.math.BigDecimal;

public class ProductoResponse {

    private final long id;
    private final String nombre;
    private final BigDecimal precio;
    private final int existencia;

    public ProductoResponse(long id, String nombre, BigDecimal precio, int existencia) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.existencia = existencia;
    }
    
public static ProductoResponse desdeDominio(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getExistencia()
        );
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getExistencia() { return existencia; }
}

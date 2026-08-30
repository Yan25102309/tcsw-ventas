package domain;

import java.math.BigDecimal;

public final class Producto {
    private final long id;
    private final String nombre;
    private final BigDecimal precio;
    
    private int existencia;

    public Producto(long id, String nombre, BigDecimal precio, int existencia) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser mayor que cero");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio y no puede estar vacio");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (existencia < 0) {
            throw new IllegalArgumentException("La existencia no puede ser negativa");
        }
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.existencia = existencia;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }
    public int getExistencia() { return existencia; }

    public void descontar(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser positiva");
        }
        if (cantidad > existencia) {
            throw new IllegalStateException("Existencia insuficiente para realizar el descuento");
        }
        this.existencia -= cantidad;
    }
}
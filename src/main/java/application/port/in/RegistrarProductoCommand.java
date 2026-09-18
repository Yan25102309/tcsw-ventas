package application.port.in;

import java.math.BigDecimal;
import java.util.Objects;

public final class RegistrarProductoCommand {
    private final Long id;
    private final String nombre;
    private final BigDecimal precio;
    private final int existencia;

    public RegistrarProductoCommand(Long id, String nombre, BigDecimal precio, int existencia) {
        this.id = Objects.requireNonNull(id, "El id no puede ser nulo");
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.precio = Objects.requireNonNull(precio, "El precio no puede ser nulo");
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

    public int getExistencia() {
        return existencia;
    }
}

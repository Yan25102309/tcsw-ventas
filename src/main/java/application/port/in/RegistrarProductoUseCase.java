package application.port.in;

import domain.Producto;
import java.math.BigDecimal;

public interface RegistrarProductoUseCase {
    Producto registrarProducto(Long id, String nombre, BigDecimal precio, int existencia);
}

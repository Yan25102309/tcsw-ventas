package application.port.in;

import domain.Producto;

public interface RegistrarProductoUseCase {
    Producto registrarProducto(RegistrarProductoCommand comando);
}

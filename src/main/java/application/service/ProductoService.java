package application.service;

import application.ProductoRepository;
import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import domain.Producto;
import java.util.Objects;

public class ProductoService implements RegistrarProductoUseCase {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = Objects.requireNonNull(productoRepository, "El repositorio no puede ser nulo");
    }

    @Override
    public Producto registrarProducto(RegistrarProductoCommand comando) {
        Objects.requireNonNull(comando, "El comando no puede ser nulo");
        Producto producto = new Producto(
            comando.getId(),
            comando.getNombre(),
            comando.getPrecio(),
            comando.getExistencia()
        );
        return productoRepository.guardar(producto);
    }
}

package application.service;

import application.ProductoRepository;
import application.port.in.RegistrarProductoUseCase;
import domain.Producto;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductoService implements RegistrarProductoUseCase {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = Objects.requireNonNull(productoRepository, "El repositorio no puede ser nulo");
    }

    @Override
    public Producto registrarProducto(Long id, String nombre, BigDecimal precio, int existencia) {
        Producto producto = new Producto(id, nombre, precio, existencia);
        return productoRepository.guardar(producto);
    }
}

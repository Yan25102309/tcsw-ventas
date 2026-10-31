package application;

import domain.DetalleVenta;
import domain.Producto;
import domain.Venta;
import infrastructure.exception.PersistenceException;

public class ConfirmarVentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public ConfirmarVentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    public Venta confirmarVenta(Venta venta) {
        if (venta == null || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un detalle");
        }

        // 1. Validar existencias y actualizar existencias de cada producto
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();

            // Se evalúa el valor de retorno en el if, resolviendo la advertencia de Sonar
            if (productoRepository.buscarPorId(producto.getId()).isEmpty()) {
                throw new PersistenceException(
                        "Producto con ID " + producto.getId() + " no encontrado", null);
            }

            // Persistir el producto con la existencia actualizada
            productoRepository.guardar(producto);
        }

        // 2. Persistir el agregado completo de la Venta en Hibernate
        return ventaRepository.guardar(venta);
    }
}

package adapter.rest;

import adapter.rest.dto.ConfirmarVentaRequest;
import adapter.rest.dto.DetalleVentaRequest;
import application.ConfirmarVentaService;
import application.ProductoRepository;
import domain.Producto;
import domain.Venta;
import infrastructure.exception.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaRestController {

    private final ConfirmarVentaService confirmarVentaService;
    private final ProductoRepository productoRepository;

    public VentaRestController(ConfirmarVentaService confirmarVentaService, ProductoRepository productoRepository) {
        this.confirmarVentaService = confirmarVentaService;
        this.productoRepository = productoRepository;
    }

    @PostMapping
    public ResponseEntity<Venta> confirmarVenta(@Valid @RequestBody ConfirmarVentaRequest request) {
        Venta venta = new Venta();
        for (DetalleVentaRequest d : request.getDetalles()) {
            Producto producto = productoRepository.buscarPorId(d.getProductoId())
                    .orElseThrow(() -> new PersistenceException("Producto no encontrado con ID: " + d.getProductoId(), null));
            venta.agregarDetalle(producto, d.getCantidad());
        }

        Venta confirmada = confirmarVentaService.confirmarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(confirmada);
    }
}

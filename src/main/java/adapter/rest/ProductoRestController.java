package adapter.rest;

import adapter.rest.dto.CrearProductoRequest;
import adapter.rest.dto.ProductoResponse;
import application.ProductoRepository;
import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import domain.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoRestController {

    private final RegistrarProductoUseCase registrarProductoUseCase;
    private final ProductoRepository productoRepository;

    public ProductoRestController(RegistrarProductoUseCase registrarProductoUseCase,
            ProductoRepository productoRepository) {
        this.registrarProductoUseCase = registrarProductoUseCase;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        List<ProductoResponse> productos = productoRepository.obtenerTodos()
                .stream()
                .map(ProductoResponse::desdeDominio)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable long id) {
        return productoRepository.buscarPorId(id)
                .map(p -> ResponseEntity.ok(ProductoResponse.desdeDominio(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody CrearProductoRequest request) {
        // Instanciación del comando utilizando los parámetros del DTO recibido
        RegistrarProductoCommand comando = new RegistrarProductoCommand(
                request.getId(),
                request.getNombre(),
                request.getPrecio(),
                request.getExistencia());

        // Invocación al puerto de entrada pasando el comando
        Producto creado = registrarProductoUseCase.registrarProducto(comando);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.desdeDominio(creado));
    }
}

package adapter.rest;

import adapter.rest.dto.ConfirmarVentaRequest;
import adapter.rest.dto.DetalleVentaRequest;
import application.ConfirmarVentaService;
import application.ProductoRepository;
import application.VentaRepository;
import domain.Producto;
import domain.Venta;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VentaRestControllerTest {

    private MockMvc mockMvc;
    private FakeProductoRepository productoRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.productoRepository = new FakeProductoRepository();
        VentaRepository ventaRepository = new FakeVentaRepository();
        ConfirmarVentaService service = new ConfirmarVentaService(ventaRepository, productoRepository);

        VentaRestController controller = new VentaRestController(service, productoRepository);
        ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(exceptionHandler)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/v1/ventas debe procesar una venta correctamente")
    void testConfirmarVentaExitosa() throws Exception {
        Producto p = new Producto(1L, "Teclado USB", new BigDecimal("150.00"), 10);
        productoRepository.guardar(p);

        ConfirmarVentaRequest req = new ConfirmarVentaRequest(List.of(new DetalleVentaRequest(1L, 2)));

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/ventas debe devolver 500 cuando el producto no existe")
    void testConfirmarVentaProductoInexistente() throws Exception {
        ConfirmarVentaRequest req = new ConfirmarVentaRequest(List.of(new DetalleVentaRequest(99L, 1)));

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Fallo de Persistencia"));
    }

    @Test
    @DisplayName("POST /api/v1/ventas debe devolver 400 cuando los datos son invalidos")
    void testConfirmarVentaValidacionInvalida() throws Exception {
        ConfirmarVentaRequest req = new ConfirmarVentaRequest(Collections.emptyList());

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Error de Validación de Formato"));
    }

    @Test
    @DisplayName("POST /api/v1/ventas debe devolver 409 cuando se excede el stock")
    void testConfirmarVentaConflictoStock() throws Exception {
        Producto p = new Producto(2L, "Mouse Gamer", new BigDecimal("250.00"), 2);
        productoRepository.guardar(p);

        ConfirmarVentaRequest req = new ConfirmarVentaRequest(List.of(new DetalleVentaRequest(2L, 10)));

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflicto de Regla de Negocio"));
    }

    private static class FakeProductoRepository implements ProductoRepository {
        private final List<Producto> lista = new ArrayList<>();
        @Override public Producto guardar(Producto p) { lista.add(p); return p; }
        @Override public Optional<Producto> buscarPorId(long id) { return lista.stream().filter(p -> p.getId() == id).findFirst(); }
        @Override public List<Producto> obtenerTodos() { return new ArrayList<>(lista); }
    }

    private static class FakeVentaRepository implements VentaRepository {
        @Override public Venta guardar(Venta v) { return v; }
        @Override public Optional<Venta> buscarPorId(long id) { return Optional.empty(); }
        @Override public List<Venta> obtenerTodos() { return new ArrayList<>(); }
    }
}

package adapter.rest;

import adapter.rest.dto.CrearProductoRequest;
import application.ProductoRepository;
import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import domain.Producto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductoRestControllerTest {

    private MockMvc mockMvc;
    private FakeProductoRepository productoRepository;
    private FakeRegistrarProductoUseCase useCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.productoRepository = new FakeProductoRepository();
        this.useCase = new FakeRegistrarProductoUseCase(productoRepository);
        ProductoRestController controller = new ProductoRestController(useCase, productoRepository);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("GET /api/v1/productos debe retornar lista vacia inicialmente")
    void testObtenerTodosVacio() throws Exception {
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /api/v1/productos debe crear un producto exitosamente")
    void testCrearProductoExito() throws Exception {
        CrearProductoRequest req = new CrearProductoRequest(10L, "Monitor Gaming", new BigDecimal("3500.00"), 5);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Monitor Gaming"));
    }

    @Test
    @DisplayName("GET /api/v1/productos/{id} debe retornar 404 si no existe")
    void testObtenerPorIdNoEncontrado() throws Exception {
        mockMvc.perform(get("/api/v1/productos/999"))
                .andExpect(status().isNotFound());
    }

    // Fakes para aislamiento de prueba unitaria sin BD
    private static class FakeProductoRepository implements ProductoRepository {
        private final List<Producto> lista = new ArrayList<>();

        @Override
        public Producto guardar(Producto producto) {
            lista.add(producto);
            return producto;
        }

        @Override
        public Optional<Producto> buscarPorId(long id) {
            return lista.stream().filter(p -> p.getId() == id).findFirst();
        }

        @Override
        public List<Producto> obtenerTodos() {
            return new ArrayList<>(lista);
        }
    }

    private static class FakeRegistrarProductoUseCase implements RegistrarProductoUseCase {
        private final ProductoRepository repo;

        public FakeRegistrarProductoUseCase(ProductoRepository repo) {
            this.repo = repo;
        }

        @Override
        public Producto registrarProducto(RegistrarProductoCommand comando) {
            // Lectura de los valores desde el comando con sus getters correspondientes
            Producto producto = new Producto(
                    comando.getId(),
                    comando.getNombre(),
                    comando.getPrecio(),
                    comando.getExistencia());
            return repo.guardar(producto);
        }
    }
}

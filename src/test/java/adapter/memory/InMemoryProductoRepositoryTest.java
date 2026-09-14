package adapter.memory;

import application.ProductoRepository;
import domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adapter.memory.InMemoryProductoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryProductoRepositoryTest {

    private ProductoRepository repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new InMemoryProductoRepository();
    }

    @Test
    void testGuardarYBuscarPorIdExitoso() {
        Producto producto = new Producto(1L, "Café Veracruz", new BigDecimal("120.50"), 10);
        repositorio.guardar(producto);

        Optional<Producto> encontrado = repositorio.buscarPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("Café Veracruz", encontrado.get().getNombre());
    }

    @Test
    void testBuscarPorIdInexistenteRetornaOptionalVacio() {
        Optional<Producto> encontrado = repositorio.buscarPorId(99L);

        assertTrue(encontrado.isEmpty());
    }

    @Test
    void testGuardarProductoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> repositorio.guardar(null));
    }

    @Test
    void testObtenerTodosRetornaListaCompletaYCopiaDefensiva() {
        Producto p1 = new Producto(1L, "Café Veracruz", new BigDecimal("120.50"), 10);
        Producto p2 = new Producto(2L, "Galletas", new BigDecimal("35.00"), 20);

        repositorio.guardar(p1);
        repositorio.guardar(p2);

        List<Producto> todos = repositorio.obtenerTodos();

        assertEquals(2, todos.size());

        todos.clear();
        assertEquals(2, repositorio.obtenerTodos().size());
    }
}
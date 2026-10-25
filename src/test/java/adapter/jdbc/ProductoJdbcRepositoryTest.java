package adapter.jdbc;

import domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductoJdbcRepositoryTest {

    private ProductoJdbcRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new ProductoJdbcRepository();
    }

    @Test
    @DisplayName("Debe guardar y recuperar un producto correctamente via JDBC")
    void testGuardarYBuscarPorIdExitoso() {
        Producto producto = new Producto(500L, "Monitor Gamer 144Hz", new BigDecimal("4500.00"), 8);
        repository.guardar(producto);

        Optional<Producto> resultado = repository.buscarPorId(500L);
        assertTrue(resultado.isPresent());
        assertEquals("Monitor Gamer 144Hz", resultado.get().getNombre());
        assertEquals(new BigDecimal("4500.00"), resultado.get().getPrecio());
        assertEquals(8, resultado.get().getExistencia());
    }

    @Test
    @DisplayName("Debe retornar Optional.empty() al buscar un ID inexistente")
    void testBuscarPorIdInexistente() {
        Optional<Producto> resultado = repository.buscarPorId(99999L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe rechazar la insercion cuando se viola una restriccion relacional CHECK")
    void testRechazarViolacionIntegridad() {
        assertThrows(RuntimeException.class, () -> {
            Producto invalido = new Producto(501L, "Error", new BigDecimal("-10.00"), 5);
            repository.guardar(invalido);
        });
    }
}

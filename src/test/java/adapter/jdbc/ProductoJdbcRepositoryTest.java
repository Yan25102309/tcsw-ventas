package adapter.jdbc;

import domain.Producto;
import infrastructure.exception.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
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
    @DisplayName("Debe lanzar IllegalArgumentException al intentar guardar un producto nulo")
    void testGuardarNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.guardar(null));
    }

    @Test
    @DisplayName("Debe actualizar un producto existente via ON CONFLICT")
    void testActualizarProductoExistente() {
        Producto original = new Producto(700L, "Monitor HD", new BigDecimal("2000.00"), 5);
        repository.guardar(original);

        Producto actualizado = new Producto(700L, "Monitor Full HD", new BigDecimal("2500.00"), 10);
        repository.guardar(actualizado);

        Optional<Producto> resultado = repository.buscarPorId(700L);
        assertTrue(resultado.isPresent());
        assertEquals("Monitor Full HD", resultado.get().getNombre());
        assertEquals(new BigDecimal("2500.00"), resultado.get().getPrecio());
        assertEquals(10, resultado.get().getExistencia());
    }

    @Test
    @DisplayName("Debe retornar Optional.empty() al buscar un ID inexistente")
    void testBuscarPorIdInexistente() {
        Optional<Producto> resultado = repository.buscarPorId(99999L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe obtener la lista completa de productos via JDBC")
    void testObtenerTodos() {
        Producto p1 = new Producto(600L, "Teclado USB", new BigDecimal("250.00"), 10);
        Producto p2 = new Producto(601L, "Mouse Pad XL", new BigDecimal("150.00"), 15);
        repository.guardar(p1);
        repository.guardar(p2);

        List<Producto> todos = repository.obtenerTodos();

        assertFalse(todos.isEmpty());
        assertTrue(todos.stream().anyMatch(p -> p.getId() == 600L));
        assertTrue(todos.stream().anyMatch(p -> p.getId() == 601L));
    }

    @Test
    @DisplayName("Debe rechazar la insercion cuando se viola una restriccion relacional en base de datos")
    void testRechazarViolacionIntegridad() {
        Producto invalido = new Producto(501L, "Teclado", new BigDecimal("999999999999.00"), 5);
        assertThrows(PersistenceException.class, () -> repository.guardar(invalido));
    }
}

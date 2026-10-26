package adapter.hibernate;

import domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductoHibernateRepositoryTest {

    private ProductoHibernateRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new ProductoHibernateRepository();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al intentar guardar un producto nulo en Hibernate")
    void testGuardarNull() {
        assertThrows(IllegalArgumentException.class, () -> repository.guardar(null));
    }

    @Test
    @DisplayName("Debe guardar y buscar un producto correctamente via Hibernate")
    void testGuardarYBuscarPorIdExitoso() {
        Producto producto = new Producto(800L, "Silla Ergonomica", new BigDecimal("3200.00"), 4);
        repository.guardar(producto);

        Optional<Producto> resultado = repository.buscarPorId(800L);

        assertTrue(resultado.isPresent());
        assertEquals("Silla Ergonomica", resultado.get().getNombre());
        assertEquals(new BigDecimal("3200.00"), resultado.get().getPrecio());
        assertEquals(4, resultado.get().getExistencia());
    }

    @Test
    @DisplayName("Debe retornar Optional.empty() al buscar un ID inexistente en Hibernate")
    void testBuscarPorIdInexistente() {
        Optional<Producto> resultado = repository.buscarPorId(99998L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe obtener todos los productos via Hibernate")
    void testObtenerTodos() {
        Producto p1 = new Producto(801L, "Audifonos Bluetooth", new BigDecimal("1200.00"), 12);
        Producto p2 = new Producto(802L, "Microfono Condensador", new BigDecimal("1800.00"), 6);
        repository.guardar(p1);
        repository.guardar(p2);

        List<Producto> productos = repository.obtenerTodos();

        assertFalse(productos.isEmpty());
        assertTrue(productos.stream().anyMatch(p -> p.getId() == 801L));
        assertTrue(productos.stream().anyMatch(p -> p.getId() == 802L));
    }

    @Test
    @DisplayName("Debe actualizar un producto existente via Hibernate saveOrUpdate")
    void testActualizarProductoExistente() {
        Producto original = new Producto(803L, "Escritorio Madera", new BigDecimal("4000.00"), 2);
        repository.guardar(original);

        Producto actualizado = new Producto(803L, "Escritorio Ajustable", new BigDecimal("5500.00"), 5);
        repository.guardar(actualizado);

        Optional<Producto> resultado = repository.buscarPorId(803L);
        assertTrue(resultado.isPresent());
        assertEquals("Escritorio Ajustable", resultado.get().getNombre());
        assertEquals(new BigDecimal("5500.00"), resultado.get().getPrecio());
    }
}

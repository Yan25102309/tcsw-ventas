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
    @DisplayName("Debe guardar y recuperar un producto correctamente via Hibernate")
    void testGuardarYBuscarPorIdExitoso() {
        Producto producto = new Producto(700L, "Teclado Mecanico RGB", new BigDecimal("1250.00"), 12);
        repository.guardar(producto);

        Optional<Producto> resultado = repository.buscarPorId(700L);
        assertTrue(resultado.isPresent());
        assertEquals("Teclado Mecanico RGB", resultado.get().getNombre());
        assertEquals(new BigDecimal("1250.00"), resultado.get().getPrecio());
        assertEquals(12, resultado.get().getExistencia());
    }

    @Test
    @DisplayName("Debe retornar Optional.empty() al buscar un ID inexistente en Hibernate")
    void testBuscarPorIdInexistente() {
        Optional<Producto> resultado = repository.buscarPorId(888888L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe obtener todos los productos vía Hibernate")
    void testObtenerTodos() {
        List<Producto> productos = repository.obtenerTodos();
        assertNotNull(productos);
        assertFalse(productos.isEmpty());
    }

    @Test
    @DisplayName("Debe rechazar guardar un producto nulo en Hibernate")
    void testGuardarProductoNulo() {
        assertThrows(IllegalArgumentException.class, () -> repository.guardar(null));
    }

    @Test
    @DisplayName("Debe comprobar getters y constructor de ProductoEntity")
    void testProductoEntityAccessors() {
        ProductoEntity entity = new ProductoEntity(15L, "Audifonos", new BigDecimal("850.00"), 4);
        assertEquals(15L, entity.getId());
        assertEquals("Audifonos", entity.getNombre());
        assertEquals(new BigDecimal("850.00"), entity.getPrecio());
        assertEquals(4, entity.getExistencia());

        ProductoEntity vacia = new ProductoEntity();
        assertNotNull(vacia);
    }
}

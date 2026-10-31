package adapter.hibernate;

import domain.Producto;
import domain.Venta;
import infrastructure.exception.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VentaHibernateRepositoryTest {

    private VentaHibernateRepository ventaRepository;
    private ProductoHibernateRepository productoRepository;

    @BeforeEach
    void setUp() {
        this.ventaRepository = new VentaHibernateRepository();
        this.productoRepository = new ProductoHibernateRepository();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al intentar guardar una Venta nula")
    void testGuardarVentaNula() {
        assertThrows(IllegalArgumentException.class, () -> ventaRepository.guardar(null));
    }

    @Test
    @DisplayName("Debe guardar y consultar la lista completa de ventas")
    void testGuardarYObtenerTodos() {
        Producto p1 = new Producto(800L, "Mouse USB", new BigDecimal("250.00"), 15);
        productoRepository.guardar(p1);

        Venta venta = new Venta();
        venta.agregarDetalle(p1, 1);

        ventaRepository.guardar(venta);

        List<Venta> lista = ventaRepository.obtenerTodos();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    @DisplayName("Debe retornar Optional.empty al buscar un ID de Venta inexistente")
    void testBuscarPorIdInexistente() {
        Optional<Venta> resultado = ventaRepository.buscarPorId(999999L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Debe realizar rollback completo si se intenta guardar un detalle con producto inexistente")
    void testRollbackAnteError() {
        Venta ventaInvalida = new Venta();
        Producto productoInexistente = new Producto(88888L, "Producto Inexistente", new BigDecimal("10.00"), 5);
        ventaInvalida.agregarDetalle(productoInexistente, 1);

        assertThrows(PersistenceException.class, () -> ventaRepository.guardar(ventaInvalida));
    }
}


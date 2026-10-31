package application;

import domain.Producto;
import domain.Venta;
import infrastructure.exception.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConfirmarVentaServiceTest {

    private ConfirmarVentaService service;
    private FakeProductoRepository productoRepository;
    private FakeVentaRepository ventaRepository;

    @BeforeEach
    void setUp() {
        this.productoRepository = new FakeProductoRepository();
        this.ventaRepository = new FakeVentaRepository();
        this.service = new ConfirmarVentaService(ventaRepository, productoRepository);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si la venta es nula")
    void testVentaNula() {
        assertThrows(IllegalArgumentException.class, () -> service.confirmarVenta(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si la venta no tiene detalles")
    void testVentaSinDetalles() {
        Venta ventaVacia = new Venta();
        assertThrows(IllegalArgumentException.class, () -> service.confirmarVenta(ventaVacia));
    }

    @Test
    @DisplayName("Debe lanzar PersistenceException si el producto no existe en el repositorio")
    void testProductoNoEncontrado() {
        Producto pInexistente = new Producto(99L, "Fantasma", new BigDecimal("10.00"), 5);
        Venta venta = new Venta();
        venta.agregarDetalle(pInexistente, 1);

        assertThrows(PersistenceException.class, () -> service.confirmarVenta(venta));
    }

    @Test
    @DisplayName("Debe lanzar IllegalStateException al intentar agregar un detalle con existencia insuficiente")
    void testExistenciaInsuficiente() {
        Producto p = new Producto(1L, "Lapiz", new BigDecimal("5.00"), 2);
        productoRepository.guardar(p);

        Venta venta = new Venta();
        // El dominio Venta intercepta la existencia insuficiente al agregar el detalle
        assertThrows(IllegalStateException.class, () -> venta.agregarDetalle(p, 5));
    }

    @Test
    @DisplayName("Debe confirmar la venta exitosamente y actualizar existencias")
    void testConfirmarVentaExitosa() {
        Producto p = new Producto(1L, "Cuaderno", new BigDecimal("25.00"), 10);
        productoRepository.guardar(p);

        Venta venta = new Venta();
        venta.agregarDetalle(p, 3); // Descuenta de 10 a 7

        Venta resultado = service.confirmarVenta(venta);

        assertNotNull(resultado);
        Producto productoActualizado = productoRepository.buscarPorId(1L).orElseThrow();
        assertEquals(7, productoActualizado.getExistencia());
    }

    // Adaptadores Fake para pruebas unitarias de capa de aplicación
    private static class FakeProductoRepository implements ProductoRepository {
        private final List<Producto> productos = new ArrayList<>();

        @Override
        public Producto guardar(Producto producto) {
            productos.removeIf(p -> p.getId() == producto.getId());
            productos.add(producto);
            return producto;
        }

        @Override
        public Optional<Producto> buscarPorId(long id) {
            return productos.stream().filter(p -> p.getId() == id).findFirst();
        }

        @Override
        public List<Producto> obtenerTodos() {
            return new ArrayList<>(productos);
        }
    }

    private static class FakeVentaRepository implements VentaRepository {
        private final List<Venta> ventas = new ArrayList<>();

        @Override
        public Venta guardar(Venta venta) {
            ventas.add(venta);
            return venta;
        }

        @Override
        public Optional<Venta> buscarPorId(long id) {
            return Optional.empty();
        }

        @Override
        public List<Venta> obtenerTodos() {
            return new ArrayList<>(ventas);
        }
    }
}

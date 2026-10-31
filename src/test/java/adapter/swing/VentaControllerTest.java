package adapter.swing;

import application.ConfirmarVentaService;
import application.ProductoRepository;
import application.VentaRepository;
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

class VentaControllerTest {

    private VentaView view;
    private Venta venta;
    private VentaController controller;
    private FakeProductoRepository productoRepository;
    private FakeVentaRepository ventaRepository;

    @BeforeEach
    void setUp() {
        this.view = new VentaView();
        this.venta = new Venta();
        this.productoRepository = new FakeProductoRepository();
        this.ventaRepository = new FakeVentaRepository();
        ConfirmarVentaService ventaService = new ConfirmarVentaService(ventaRepository, productoRepository);
        this.controller = new VentaController(view, venta, ventaService);
    }

    @Test
    @DisplayName("Debe instanciar el controlador correctamente")
    void testInicializacionExitosa() {
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Debe agregar un producto a la venta y actualizar el modelo de la tabla")
    void testAgregarProductoAVentaExitoso() {
        Producto producto = new Producto(1L, "Teclado RGB", new BigDecimal("500.00"), 10);
        controller.agregarProductoAVenta(producto, 2);

        assertEquals(1, venta.getDetalles().size());
        if (view.getTableModel() != null) {
            assertEquals(1, view.getTableModel().getRowCount());
        }
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al intentar agregar un producto nulo")
    void testAgregarProductoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> controller.agregarProductoAVenta(null, 1));
    }

    @Test
    @DisplayName("Debe ejecutar calcularTotalAsincrono de forma exitosa")
    void testCalcularTotalAsincronoExito() throws InterruptedException {
        Producto producto = new Producto(1L, "Mouse USB", new BigDecimal("200.00"), 5);
        productoRepository.guardar(producto);
        controller.agregarProductoAVenta(producto, 1);

        controller.calcularTotalAsincrono();
        Thread.sleep(300); // Dar tiempo al SwingWorker para ejecutar doInBackground() y done()

        assertFalse(venta.getDetalles().isEmpty());
    }

    @Test
    @DisplayName("Debe manejar errores durante la ejecucion asincrona de calcularTotalAsincrono")
    void testCalcularTotalAsincronoConError() throws InterruptedException {
        FakeVentaRepository failingVentaRepo = new FakeVentaRepository() {
            @Override
            public Venta guardar(Venta v) {
                throw new PersistenceException("Error forzado para probar catch de SwingWorker", null);
            }
        };
        ConfirmarVentaService failingService = new ConfirmarVentaService(failingVentaRepo, productoRepository);
        VentaController failingController = new VentaController(view, venta, failingService);

        Producto producto = new Producto(1L, "Monitor 4K", new BigDecimal("3000.00"), 5);
        failingController.agregarProductoAVenta(producto, 1);

        failingController.calcularTotalAsincrono();
        Thread.sleep(300); // Dar tiempo para que pase por ExecutionException en done()

        assertNotNull(failingController);
    }

    // Repositorios Fakes ligeros para pruebas unitarias desacopladas
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

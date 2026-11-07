package adapter.swing;

import application.ConfirmarVentaService;
import application.ProductoRepository;
import application.VentaRepository;
import domain.Producto;
import domain.Venta;
import infrastructure.exception.PersistenceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class VentaControllerTest {

    private VentaView view;
    private Venta venta;
    private VentaController controller;
    private FakeProductoRepository productoRepository;
    private FakeVentaRepository ventaRepository;

    @BeforeAll
    static void setUpHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @BeforeEach
    void setUp() {
        this.view = new TestVentaView();
        this.venta = new Venta();
        this.productoRepository = new FakeProductoRepository();
        this.ventaRepository = new FakeVentaRepository();
        ConfirmarVentaService ventaService = new ConfirmarVentaService(ventaRepository, productoRepository);
        this.controller = new VentaController(view, venta, ventaService, productoRepository);
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
    @DisplayName("Debe agregar desde la interfaz el producto buscado por ID")
    void testAgregarProductoDesdeVista() {
        Producto producto = new Producto(2L, "Audífonos", new BigDecimal("800.00"), 4);
        productoRepository.guardar(producto);
        view.setProductoIdText("2");
        view.setCantidadText("2");

        view.getBtnAgregarProducto().doClick();

        assertEquals(1, venta.getDetalles().size());
        assertEquals(1, view.getTableModel().getRowCount());
        assertEquals("Audífonos", view.getTableModel().getValueAt(0, 0));
        assertEquals(2, view.getTableModel().getValueAt(0, 1));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al intentar agregar un producto nulo")
    void testAgregarProductoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> controller.agregarProductoAVenta(null, 1));
    }

    @Test
    @DisplayName("Debe ejecutar calcularTotalAsincrono de forma exitosa")
    void testCalcularTotalAsincronoExito() throws Exception {
        Producto producto = new Producto(1L, "Mouse USB", new BigDecimal("200.00"), 5);
        productoRepository.guardar(producto);
        controller.agregarProductoAVenta(producto, 1);

        controller.calcularTotalAsincrono();
        assertTrue(ventaRepository.awaitSave());
        assertTrue(((TestVentaView) view).awaitReady());

        assertFalse(venta.getDetalles().isEmpty());
        assertTrue(view.getLblEstado().getText().contains("éxito"));
    }

    @Test
    @DisplayName("Debe manejar errores durante la ejecucion asincrona de calcularTotalAsincrono")
    void testCalcularTotalAsincronoConError() throws Exception {
        CountDownLatch failureLatch = new CountDownLatch(1);
        FakeVentaRepository failingVentaRepo = new FakeVentaRepository() {
            @Override
            public Venta guardar(Venta v) {
                failureLatch.countDown();
                throw new PersistenceException("Error forzado para probar catch de SwingWorker", null);
            }
        };
        Producto producto = new Producto(1L, "Monitor 4K", new BigDecimal("3000.00"), 5);
        productoRepository.guardar(producto);
        ConfirmarVentaService failingService = new ConfirmarVentaService(failingVentaRepo, productoRepository);
        VentaController failingController = new VentaController(view, venta, failingService);

        failingController.agregarProductoAVenta(producto, 1);

        failingController.calcularTotalAsincrono();
        assertTrue(failureLatch.await(5, TimeUnit.SECONDS));
        assertTrue(((TestVentaView) view).awaitReady());

        assertEquals("Estado: Error al procesar.", view.getLblEstado().getText());
    }

    @Test
    @DisplayName("Debe rechazar campos vacíos al agregar un producto desde la vista")
    void testAgregarProductoDesdeVistaConCamposVacios() {
        view.setProductoIdText("");
        view.setCantidadText("2");

        view.getBtnAgregarProducto().doClick();

        assertEquals(0, venta.getDetalles().size());
        assertTrue(view.getLblEstado().getText().contains("Error:"));
    }

    @Test
    @DisplayName("Debe rechazar valores no numéricos al agregar un producto desde la vista")
    void testAgregarProductoDesdeVistaConValoresInvalidos() {
        view.setProductoIdText("abc");
        view.setCantidadText("dos");

        view.getBtnAgregarProducto().doClick();

        assertEquals(0, venta.getDetalles().size());
        assertEquals("Estado: Error: El ID y la cantidad deben ser números enteros válidos.",
                view.getLblEstado().getText());
    }

    @Test
    @DisplayName("Debe rechazar un producto inexistente al agregarlo desde la vista")
    void testAgregarProductoDesdeVistaConProductoInexistente() {
        view.setProductoIdText("999");
        view.setCantidadText("1");

        view.getBtnAgregarProducto().doClick();

        assertTrue(view.getLblEstado().getText().contains("No existe un producto con ID 999"));
    }

    @Test
    @DisplayName("Debe informar si el controlador no tiene repositorio de productos")
    void testAgregarProductoDesdeVistaSinRepositorio() {
        VentaView viewSinRepositorio = new VentaView();
        VentaController controllerSinRepositorio = new VentaController(
                viewSinRepositorio, venta, new ConfirmarVentaService(ventaRepository, productoRepository));

        viewSinRepositorio.setProductoIdText("1");
        viewSinRepositorio.setCantidadText("1");
        viewSinRepositorio.getBtnAgregarProducto().doClick();

        assertEquals("Estado: Error: No hay un repositorio de productos configurado.",
                viewSinRepositorio.getLblEstado().getText());
        assertNotNull(controllerSinRepositorio);
    }

    @Test
    @DisplayName("La vista debe reflejar el estado de procesamiento")
    void testVistaMarcaEjecucionYRegresaALista() {
        view.marcarEjecutando();

        assertFalse(view.getBtnCalcularTotal().isEnabled());
        assertFalse(view.getComboDescuentos().isEnabled());
        assertEquals("Estado: Procesando en segundo plano...", view.getLblEstado().getText());

        view.marcarListo("Operación terminada.");

        assertTrue(view.getBtnCalcularTotal().isEnabled());
        assertTrue(view.getComboDescuentos().isEnabled());
        assertEquals("Estado: Operación terminada.", view.getLblEstado().getText());
    }

    // Repositorios Fakes ligeros para pruebas unitarias desacopladas
    private static class TestVentaView extends VentaView {
        private final CountDownLatch readyLatch = new CountDownLatch(1);

        @Override
        public void marcarListo(String mensaje) {
            super.marcarListo(mensaje);
            if ("Error al procesar.".equals(mensaje)
                    || mensaje.contains("Venta calculada y persistida con éxito.")) {
                readyLatch.countDown();
            }
        }

        boolean awaitReady() throws InterruptedException {
            return readyLatch.await(5, TimeUnit.SECONDS);
        }
    }

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
        private final CountDownLatch saveLatch = new CountDownLatch(1);

        @Override
        public Venta guardar(Venta venta) {
            ventas.add(venta);
            saveLatch.countDown();
            return venta;
        }

        boolean awaitSave() throws InterruptedException {
            return saveLatch.await(5, TimeUnit.SECONDS);
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

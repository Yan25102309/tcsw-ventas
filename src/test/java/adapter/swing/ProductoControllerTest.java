package adapter.swing;

import application.port.in.RegistrarProductoUseCase;
import domain.Producto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ProductoControllerTest {

    @BeforeAll
    static void setUpHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    void testInicializacionControladorConPuerto() {
        RegistrarProductoUseCase stubFacade = cmd ->
                new Producto(cmd.getId(), cmd.getNombre(), cmd.getPrecio(), cmd.getExistencia());
        RegistrarProductoView view = new RegistrarProductoView();
        ProductoController controller = new ProductoController(view, stubFacade);
        assertNotNull(controller);
    }

    @Test
    void testProcesarRegistroExitosoLimpiaCampos() {
        AtomicReference<Producto> productoGuardado = new AtomicReference<>();
        RegistrarProductoUseCase stubFacade = cmd -> {
            Producto p = new Producto(cmd.getId(), cmd.getNombre(), cmd.getPrecio(), cmd.getExistencia());
            productoGuardado.set(p);
            return p;
        };

        RegistrarProductoView view = new RegistrarProductoView();
        ProductoController controller = new ProductoController(view, stubFacade);

        view.setIdText("10");
        view.setNombreText("Monitor LED");
        view.setPrecioText("3500.00");
        view.setExistenciaText("5");

        controller.procesarRegistro();

        assertNotNull(productoGuardado.get());
        assertEquals("Monitor LED", productoGuardado.get().getNombre());
        assertEquals("", view.getIdText());
        assertEquals("", view.getNombreText());
        assertEquals("", view.getPrecioText());
        assertEquals("", view.getExistenciaText());
    }

    @Test
    void testProcesarRegistroConErrorDeFormatoConservaCampos() {
        RegistrarProductoUseCase stubFacade = cmd ->
                new Producto(cmd.getId(), cmd.getNombre(), cmd.getPrecio(), cmd.getExistencia());

        RegistrarProductoView view = new RegistrarProductoView();
        ProductoController controller = new ProductoController(view, stubFacade);

        view.setIdText("10");
        view.setNombreText("Monitor LED");
        view.setPrecioText("PRECIO_INVALIDO");
        view.setExistenciaText("5");

        assertDoesNotThrow(controller::procesarRegistro);
        assertEquals("PRECIO_INVALIDO", view.getPrecioText());
    }

    @Test
    void testProcesarRegistroConErrorDeDominioNoLimpiaCampos() {
        RegistrarProductoUseCase stubFacade = cmd ->
                new Producto(cmd.getId(), cmd.getNombre(), cmd.getPrecio(), cmd.getExistencia());

        RegistrarProductoView view = new RegistrarProductoView();
        ProductoController controller = new ProductoController(view, stubFacade);

        view.setIdText("-5");
        view.setNombreText("Monitor LED");
        view.setPrecioText("3500.00");
        view.setExistenciaText("5");

        assertDoesNotThrow(controller::procesarRegistro);
        assertEquals("-5", view.getIdText());
    }

    @Test
    void testRechazarDependenciasNulasEnConstructor() {
        RegistrarProductoView view = new RegistrarProductoView();
        RegistrarProductoUseCase stubFacade = cmd ->
                new Producto(cmd.getId(), cmd.getNombre(), cmd.getPrecio(), cmd.getExistencia());

        assertThrows(NullPointerException.class, () -> new ProductoController(null, stubFacade));
        assertThrows(NullPointerException.class, () -> new ProductoController(view, null));
    }
}

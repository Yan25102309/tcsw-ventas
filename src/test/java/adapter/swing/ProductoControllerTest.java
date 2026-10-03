package adapter.swing;

import application.port.in.RegistrarProductoUseCase;
import domain.Producto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductoControllerTest {
    @Test
    void testInicializacionControladorConFacade() {
        RegistrarProductoUseCase stubFacade = cmd ->
            new Producto(cmd.getId(), cmd.getNombre(), cmd.getPrecio(), cmd.getExistencia());

        RegistrarProductoView view = new RegistrarProductoView();
        ProductoController controller = new ProductoController(view, stubFacade);

        assertNotNull(controller);
    }
}

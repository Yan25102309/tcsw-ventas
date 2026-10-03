package adapter.swing;

import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import domain.Producto;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductoController {
    private final RegistrarProductoView view;
    private final RegistrarProductoUseCase facadeUseCase;

    public ProductoController(RegistrarProductoView view, RegistrarProductoUseCase facadeUseCase) {
        this.view = Objects.requireNonNull(view);
        this.facadeUseCase = Objects.requireNonNull(facadeUseCase);
        this.view.getBtnGuardar().addActionListener(e -> procesarRegistro());
    }

    public void procesarRegistro() {
        try {
            Long id = Long.parseLong(view.getIdText().trim());
            String nombre = view.getNombreText().trim();
            BigDecimal precio = new BigDecimal(view.getPrecioText().trim());
            int existencia = Integer.parseInt(view.getExistenciaText().trim());

            RegistrarProductoCommand command = new RegistrarProductoCommand(id, nombre, precio, existencia);
            Producto registrado = facadeUseCase.registrarProducto(command);

            JOptionPane.showMessageDialog(view, "Producto '" + registrado.getNombre() + "' registrado con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Error de Formato: Ingrese valores válidos en ID, Precio y Existencia.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Error de Dominio", JOptionPane.ERROR_MESSAGE);
        }
    }
}

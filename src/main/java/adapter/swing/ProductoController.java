package adapter.swing;

import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import domain.Producto;

import javax.swing.*;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductoController {
    private final RegistrarProductoView view;
    private final RegistrarProductoUseCase facadeUseCase;

    public ProductoController(RegistrarProductoView view, RegistrarProductoUseCase facadeUseCase) {
        this.view = Objects.requireNonNull(view, "La vista no puede ser nula");
        this.facadeUseCase = Objects.requireNonNull(facadeUseCase, "El puerto de entrada no puede ser nulo");
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

            mostrarMensaje("Producto '" + registrado.getNombre() + "' registrado con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarCampos();
        } catch (NumberFormatException ex) {
            mostrarMensaje("Error: Ingrese valores numéricos válidos en ID, Precio y Existencia.",
                    "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            mostrarMensaje(ex.getMessage(), "Error de Dominio", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        if (!GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
        }
    }
}

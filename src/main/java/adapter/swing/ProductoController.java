package adapter.swing;

import application.port.in.RegistrarProductoCommand;
import application.port.in.RegistrarProductoUseCase;
import application.ProductoRepository;
import domain.Producto;

import javax.swing.*;
import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductoController {
    private final RegistrarProductoView view;
    private final RegistrarProductoUseCase facadeUseCase;
    private final ProductoRepository productoRepository;

    public ProductoController(RegistrarProductoView view, RegistrarProductoUseCase facadeUseCase) {
        this(view, facadeUseCase, null);
    }

    public ProductoController(RegistrarProductoView view, RegistrarProductoUseCase facadeUseCase,
                               ProductoRepository productoRepository) {
        this.view = Objects.requireNonNull(view, "La vista no puede ser nula");
        this.facadeUseCase = Objects.requireNonNull(facadeUseCase, "El puerto de entrada no puede ser nulo");
        this.productoRepository = productoRepository;
        this.view.getBtnGuardar().addActionListener(e -> procesarRegistro());
        cargarProductos();
    }

    public void procesarRegistro() {
        try {
            String idTexto = view.getIdText().trim();
            String nombre = view.getNombreText().trim();
            String precioTexto = view.getPrecioText().trim();
            String existenciaTexto = view.getExistenciaText().trim();
            if (idTexto.isEmpty() || nombre.isEmpty() || precioTexto.isEmpty() || existenciaTexto.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            Long id = Long.parseLong(idTexto);
            BigDecimal precio = new BigDecimal(precioTexto);
            int existencia = Integer.parseInt(existenciaTexto);

            RegistrarProductoCommand command = new RegistrarProductoCommand(id, nombre, precio, existencia);
            Producto registrado = facadeUseCase.registrarProducto(command);

            view.agregarProducto(registrado);
            view.mostrarEstado("Producto registrado con éxito.");
            mostrarMensaje("Producto '" + registrado.getNombre() + "' registrado con éxito.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarCampos();
        } catch (NumberFormatException ex) {
            view.mostrarEstado("Entrada inválida.");
            mostrarMensaje("Error: Ingrese valores numéricos válidos en ID, Precio y Existencia.",
                    "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            String mensaje = ex.getMessage() == null ? "No fue posible registrar el producto." : ex.getMessage();
            view.mostrarEstado("Error: " + mensaje);
            mostrarMensaje(mensaje, "Error de Dominio", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarProductos() {
        if (productoRepository == null) {
            return;
        }
        try {
            view.cargarProductos(productoRepository.obtenerTodos());
        } catch (Exception ex) {
            view.mostrarEstado("No fue posible cargar los productos.");
            mostrarMensaje("No fue posible cargar los productos: " + ex.getMessage(),
                    "Error de carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        if (!GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(view, mensaje, titulo, tipo);
        }
    }
}

package adapter.swing;

import application.ConfirmarVentaService;
import application.ProductoRepository;
import domain.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Controlador MVC para Ventas. Utiliza SwingWorker para ejecutar
 * la persistencia atómica en Hibernate/PostgreSQL fuera del EDT.
 */
public class VentaController {

    private final VentaView view;
    private final Venta venta;
    private final ConfirmarVentaService ventaService;
    private final ProductoRepository productoRepository;

    public VentaController(VentaView view, Venta venta, ConfirmarVentaService ventaService) {
        this(view, venta, ventaService, null);
    }

    public VentaController(VentaView view, Venta venta, ConfirmarVentaService ventaService,
                           ProductoRepository productoRepository) {
        this.view = Objects.requireNonNull(view, "La vista no puede ser nula");
        this.venta = Objects.requireNonNull(venta, "La venta no puede ser nula");
        this.ventaService = Objects.requireNonNull(ventaService, "El servicio de venta no puede ser nulo");
        this.productoRepository = productoRepository;

        if (this.view.getBtnCalcularTotal() != null) {
            this.view.getBtnCalcularTotal().addActionListener(e -> calcularTotalAsincrono());
        }
        this.view.getBtnAgregarProducto().addActionListener(e -> agregarProductoDesdeVista());
    }

    public void agregarProductoAVenta(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        venta.agregarDetalle(producto, cantidad);

        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        if (view.getTableModel() != null) {
            view.getTableModel().addRow(new Object[]{
                    producto.getNombre(), cantidad, producto.getPrecio(), subtotal
            });
        }
    }

    public void calcularTotalAsincrono() {
        String tipoPolitica = (String) view.getComboDescuentos().getSelectedItem();
        view.marcarEjecutando();

        SwingWorker<Venta, Void> worker = new SwingWorker<>() {
            @Override
            protected Venta doInBackground() throws Exception {
                PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica(tipoPolitica);
                venta.calcularTotal(politica);
                return ventaService.confirmarVenta(venta);
            }

            @Override
            protected void done() {
                try {
                    Venta ventaConfirmada = get();
                    BigDecimal total = ventaConfirmada.calcularTotal();
                    view.getLblTotal().setText("Total: $" + total.toString());
                    view.marcarListo("Venta calculada y persistida con éxito.");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    mostrarAdvertencia("La operación fue interrumpida.");
                    view.marcarListo("Operación interrumpida.");
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    mostrarError("No fue posible procesar la venta: " + cause.getMessage());
                    view.marcarListo("Error al procesar.");
                }
            }
        };

        worker.execute();
    }

    private void agregarProductoDesdeVista() {
        try {
            if (productoRepository == null) {
                throw new IllegalStateException("No hay un repositorio de productos configurado.");
            }
            String idTexto = view.getProductoIdText();
            String cantidadTexto = view.getCantidadText();
            if (idTexto.isEmpty() || cantidadTexto.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el ID del producto y la cantidad.");
            }
            long id = Long.parseLong(idTexto);
            int cantidad = Integer.parseInt(cantidadTexto);
            Producto producto = productoRepository.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("No existe un producto con ID " + id + "."));
            agregarProductoAVenta(producto, cantidad);
            view.limpiarEntradasProducto();
            view.marcarListo("Producto agregado a la venta.");
        } catch (NumberFormatException ex) {
            mostrarError("El ID y la cantidad deben ser números enteros válidos.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage() == null ? "No fue posible agregar el producto." : ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        view.marcarListo("Error: " + mensaje);
        if (!java.awt.GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(view, mensaje, "Entrada inválida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        if (!java.awt.GraphicsEnvironment.isHeadless()) {
            JOptionPane.showMessageDialog(view, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}

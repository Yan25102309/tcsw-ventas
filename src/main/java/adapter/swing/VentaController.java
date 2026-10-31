package adapter.swing;

import application.ConfirmarVentaService;
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

    public VentaController(VentaView view, Venta venta, ConfirmarVentaService ventaService) {
        this.view = Objects.requireNonNull(view, "La vista no puede ser nula");
        this.venta = Objects.requireNonNull(venta, "La venta no puede ser nula");
        this.ventaService = Objects.requireNonNull(ventaService, "El servicio de venta no puede ser nulo");

        if (this.view.getBtnCalcularTotal() != null) {
            this.view.getBtnCalcularTotal().addActionListener(e -> calcularTotalAsincrono());
        }
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
                    JOptionPane.showMessageDialog(view, "La operación fue interrumpida.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    view.marcarListo("Operación interrumpida.");
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(view, "No fue posible procesar la venta: " + cause.getMessage(),
                            "Error de Cálculo / Persistencia", JOptionPane.ERROR_MESSAGE);
                    view.marcarListo("Error al procesar.");
                }
            }
        };

        worker.execute();
    }
}



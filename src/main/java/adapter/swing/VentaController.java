package adapter.swing;

import domain.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Controlador MVC para Ventas. Utiliza SwingWorker para ejecutar
 * el cálculo fuera del EDT y mantener la UI responsiva.
 */
public class VentaController {
    private final VentaView view;
    private final Venta venta;

    public VentaController(VentaView view, Venta venta) {
        this.view = Objects.requireNonNull(view, "La vista no puede ser nula");
        this.venta = Objects.requireNonNull(venta, "La venta no puede ser nula");
        this.view.getBtnCalcularTotal().addActionListener(e -> calcularTotalAsincrono());
    }

    public void agregarProductoAVenta(Producto producto, int cantidad) {
        venta.agregarDetalle(producto, cantidad);
        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        view.getTableModel().addRow(new Object[]{
                producto.getNombre(), cantidad, producto.getPrecio(), subtotal
        });
    }

    public void calcularTotalAsincrono() {
        String tipoPolitica = (String) view.getComboDescuentos().getSelectedItem();
        view.marcarEjecutando();
        SwingWorker<BigDecimal, Void> worker = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() throws Exception {
                PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica(tipoPolitica);
                return venta.calcularTotal(politica);
            }

            @Override
            protected void done() {
                try {
                    BigDecimal total = get();
                    view.getLblTotal().setText("Total: $" + total.toString());
                    view.marcarListo("Venta calculada con éxito.");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(view, "La operación fue interrumpida.",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    view.marcarListo("Operación interrumpida.");
                } catch (ExecutionException ex) {
                    JOptionPane.showMessageDialog(view, "No fue posible calcular el total: " + ex.getCause().getMessage(),
                            "Error de Cálculo", JOptionPane.ERROR_MESSAGE);
                    view.marcarListo("Error al procesar.");
  }
            }
        };

        worker.execute();
    }
}



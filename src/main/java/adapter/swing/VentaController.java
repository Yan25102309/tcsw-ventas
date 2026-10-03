package adapter.swing;

import domain.*;
import java.math.BigDecimal;
import java.util.Objects;

public class VentaController {
    private final VentaView view;
    private final Venta venta; // Modelo de Dominio

    public VentaController(VentaView view, Venta venta) {
        this.view = Objects.requireNonNull(view);
        this.venta = Objects.requireNonNull(venta);
        this.view.getBtnCalcularTotal().addActionListener(e -> calcularTotal());
    }

    public void agregarProductoAVenta(Producto producto, int cantidad) {
        venta.agregarDetalle(producto, cantidad);
        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        view.getTableModel().addRow(new Object[]{
                producto.getNombre(), cantidad, producto.getPrecio(), subtotal
        });
    }

    public void calcularTotal() {
        String tipoPolitica = (String) view.getComboDescuentos().getSelectedItem();
        // Uso del Factory Method para obtener la Strategy
        PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica(tipoPolitica);
        BigDecimal total = venta.calcularTotal(politica);
        view.getLblTotal().setText("Total: $" + total.toString());
    }
}


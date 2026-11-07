package adapter.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista Swing para procesamiento de ventas
 * Integra JTable, selector de descuentos y gestión de estados visuales
 */
public class VentaView extends JPanel {
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Producto", "Cantidad", "Precio Unitario ($)", "Subtotal ($)"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
private final JTable tablaDetalles = new JTable(tableModel);
    private final JComboBox<String> comboDescuentos = new JComboBox<>(
            new String[]{"SIN_DESCUENTO", "VOLUMEN", "CLIENTE_FRECUENTE"}
    );
    private final JButton btnCalcularTotal = new JButton("Procesar Venta y Calcular Total");
    private final JTextField txtProductoId = new JTextField(8);
    private final JTextField txtCantidad = new JTextField(6);
    private final JButton btnAgregarProducto = new JButton("Agregar producto");
    private final JLabel lblEstado = new JLabel("Estado: Listo");
    private final JLabel lblTotal = new JLabel("Total: $0.00");

    public VentaView() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Política de Descuento:"));
        panelSuperior.add(comboDescuentos);
        panelSuperior.add(btnCalcularTotal);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregar.add(new JLabel("ID producto:"));
        panelAgregar.add(txtProductoId);
        panelAgregar.add(new JLabel("Cantidad:"));
        panelAgregar.add(txtCantidad);
        panelAgregar.add(btnAgregarProducto);

        JPanel panelControles = new JPanel(new BorderLayout());
        panelControles.add(panelAgregar, BorderLayout.NORTH);
        panelControles.add(panelSuperior, BorderLayout.SOUTH);

        JPanel panelInferior = new JPanel(new BorderLayout());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelInferior.add(lblEstado, BorderLayout.WEST);
        panelInferior.add(lblTotal, BorderLayout.EAST);

        add(panelControles, BorderLayout.NORTH);
        add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        comboDescuentos.getAccessibleContext().setAccessibleName("Selector de política de descuento");
        btnCalcularTotal.getAccessibleContext().setAccessibleName("Botón procesar venta");
        btnAgregarProducto.getAccessibleContext().setAccessibleName("Botón agregar producto a la venta");
        txtProductoId.getAccessibleContext().setAccessibleName("Campo ID de producto para venta");
        txtCantidad.getAccessibleContext().setAccessibleName("Campo cantidad para venta");
        tablaDetalles.getAccessibleContext().setAccessibleName("Tabla de partidas de la venta");
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JComboBox<String> getComboDescuentos() { return comboDescuentos; }
    public JButton getBtnCalcularTotal() { return btnCalcularTotal; }
    public JButton getBtnAgregarProducto() { return btnAgregarProducto; }
    public String getProductoIdText() { return txtProductoId.getText().trim(); }
    public String getCantidadText() { return txtCantidad.getText().trim(); }
    public void setProductoIdText(String id) { txtProductoId.setText(id); }
    public void setCantidadText(String cantidad) { txtCantidad.setText(cantidad); }
    public JLabel getLblEstado() { return lblEstado; }
    public JLabel getLblTotal() { return lblTotal; }

    public void marcarEjecutando() {
        btnCalcularTotal.setEnabled(false);
        comboDescuentos.setEnabled(false);
        lblEstado.setText("Estado: Procesando en segundo plano...");
    }

    public void marcarListo(String mensaje) {
        btnCalcularTotal.setEnabled(true);
        comboDescuentos.setEnabled(true);
        lblEstado.setText("Estado: " + mensaje);
    }

    public void limpiarEntradasProducto() {
        txtProductoId.setText("");
        txtCantidad.setText("");
    }
}

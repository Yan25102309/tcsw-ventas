package adapter.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentaView extends JPanel {
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Producto", "Cantidad", "Precio Unitario ()","Subtotal()"}, 0
    );
    private final JTable tablaDetalles = new JTable(tableModel);
    private final JComboBox<String> comboDescuentos = new JComboBox<>(
            new String[]{"SIN_DESCUENTO", "VOLUMEN", "CLIENTE_FRECUENTE"}
    );
    private final JButton btnCalcularTotal = new JButton("Calcular Total Venta");
    private final JLabel lblTotal = new JLabel("Total: $0.00");

    public VentaView() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Política de Descuento:"));
        panelSuperior.add(comboDescuentos);
        panelSuperior.add(btnCalcularTotal);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelInferior.add(lblTotal);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JComboBox<String> getComboDescuentos() { return comboDescuentos; }
    public JButton getBtnCalcularTotal() { return btnCalcularTotal; }
    public JLabel getLblTotal() { return lblTotal; }
}

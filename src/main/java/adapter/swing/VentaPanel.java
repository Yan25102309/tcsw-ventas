package adapter.swing;

import javax.swing.*;
import java.awt.*;

public class VentaPanel extends JPanel {

    private final JTextField txtProductoId;
    private final JTextField txtCantidad;
    private final JButton btnAgregarDetalle;
    private final JButton btnConfirmarVenta;
    private final JLabel lblStatus;
    private final JTextArea txtResumen;

    public VentaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de Formulario Superior
        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Registrar Venta Atómica (C02 - Swing)"));

        pnlForm.add(new JLabel("ID Producto:"));
        txtProductoId = new JTextField();
        pnlForm.add(txtProductoId);

        pnlForm.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        pnlForm.add(txtCantidad);

        btnAgregarDetalle = new JButton("Agregar a Venta");
        btnConfirmarVenta = new JButton("Confirmar Venta");
        pnlForm.add(btnAgregarDetalle);
        pnlForm.add(btnConfirmarVenta);

        add(pnlForm, BorderLayout.NORTH);

        // Resumen Central del Agregado Venta
        txtResumen = new JTextArea(10, 40);
        txtResumen.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResumen);
        scroll.setBorder(BorderFactory.createTitledBorder("Detalle de la Venta en Curso"));
        add(scroll, BorderLayout.CENTER);

        // Barra de Estado Inferior
        lblStatus = new JLabel("Estado: Listo");
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(lblStatus, BorderLayout.SOUTH);
    }

    public String getProductoId() { return txtProductoId.getText().trim(); }
    public String getCantidad() { return txtCantidad.getText().trim(); }
    public JButton getBtnAgregarDetalle() { return btnAgregarDetalle; }
    public JButton getBtnConfirmarVenta() { return btnConfirmarVenta; }
    public JTextArea getTxtResumen() { return txtResumen; }

    public void setStatus(String status) {
        lblStatus.setText("Estado: " + status);
    }

    public void limpiarEntradasDetalle() {
        txtProductoId.setText("");
        txtCantidad.setText("");
    }

    public void limpiarTodo() {
        limpiarEntradasDetalle();
        txtResumen.setText("");
    }
}

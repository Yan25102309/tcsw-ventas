package adapter.swing;

import javax.swing.*;
import java.awt.*;

public class RegistrarProductoView extends JPanel {
    private final JTextField txtId = new JTextField(10);
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtPrecio = new JTextField(10);
    private final JTextField txtExistencia = new JTextField(10);
    private final JButton btnGuardar = new JButton("Guardar Producto");

    public RegistrarProductoView() {
        setLayout(new GridLayout(5, 2, 5, 5));
        add(new JLabel("ID Producto:"));
        add(txtId);
        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Precio ($):"));
        add(txtPrecio);
        add(new JLabel("Existencia:"));
        add(txtExistencia);
        add(new JLabel(""));
        add(btnGuardar);
    }

    public String getIdText() { return txtId.getText(); }
    public String getNombreText() { return txtNombre.getText(); }
    public String getPrecioText() { return txtPrecio.getText(); }
    public String getExistenciaText() { return txtExistencia.getText(); }
    public JButton getBtnGuardar() { return btnGuardar; }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtExistencia.setText("");
    }
}

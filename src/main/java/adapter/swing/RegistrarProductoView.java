package adapter.swing;

import domain.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RegistrarProductoView extends JPanel {
    private final JTextField txtId = new JTextField(10);
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtPrecio = new JTextField(10);
    private final JTextField txtExistencia = new JTextField(10);
    private final JButton btnGuardar = new JButton("Guardar Producto");
    private final JLabel lblEstado = new JLabel("Estado: Listo");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Existencia"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaProductos = new JTable(tableModel);

    public RegistrarProductoView() {
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));
        formulario.add(new JLabel("ID Producto:"));
        formulario.add(txtId);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Precio ($):"));
        formulario.add(txtPrecio);
        formulario.add(new JLabel("Existencia:"));
        formulario.add(txtExistencia);
        formulario.add(new JLabel(""));
        formulario.add(btnGuardar);
        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);

        txtId.getAccessibleContext().setAccessibleName("Campo ID de producto");
        txtNombre.getAccessibleContext().setAccessibleName("Campo Nombre de producto");
        txtPrecio.getAccessibleContext().setAccessibleName("Campo Precio de producto");
        txtExistencia.getAccessibleContext().setAccessibleName("Campo Existencia de producto");
        btnGuardar.getAccessibleContext().setAccessibleName("Botón Guardar producto");
        tablaProductos.getAccessibleContext().setAccessibleName("Tabla de productos registrados");
    }

    public String getIdText() { return txtId.getText(); }
    public String getNombreText() { return txtNombre.getText(); }
    public String getPrecioText() { return txtPrecio.getText(); }
    public String getExistenciaText() { return txtExistencia.getText(); }
    public JButton getBtnGuardar() { return btnGuardar; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public void setIdText(String id) { txtId.setText(id); }
    public void setNombreText(String nombre) { txtNombre.setText(nombre); }
    public void setPrecioText(String precio) { txtPrecio.setText(precio); }
    public void setExistenciaText(String existencia) { txtExistencia.setText(existencia); }

    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtExistencia.setText("");
    }

    public void mostrarEstado(String mensaje) {
        lblEstado.setText("Estado: " + mensaje);
    }

    public void agregarProducto(Producto producto) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (Long.valueOf(producto.getId()).equals(tableModel.getValueAt(row, 0))) {
                tableModel.setValueAt(producto.getNombre(), row, 1);
                tableModel.setValueAt(producto.getPrecio(), row, 2);
                tableModel.setValueAt(producto.getExistencia(), row, 3);
                return;
            }
        }
        tableModel.addRow(new Object[]{
                producto.getId(), producto.getNombre(), producto.getPrecio(), producto.getExistencia()
        });
    }

    public void cargarProductos(java.util.List<Producto> productos) {
        tableModel.setRowCount(0);
        for (Producto producto : productos) {
            agregarProducto(producto);
        }
    }
}

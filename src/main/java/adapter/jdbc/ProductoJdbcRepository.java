package adapter.jdbc;

import application.ProductoRepository;
import domain.Producto;
import infrastructure.db.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoJdbcRepository implements ProductoRepository {

    @Override
    public Producto guardar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        String sql = "INSERT INTO producto (id, nombre, precio, existencia) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET nombre = EXCLUDED.nombre, "
                + "precio = EXCLUDED.precio, existencia = EXCLUDED.existencia";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, producto.getId());
            ps.setString(2, producto.getNombre());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getExistencia());
            ps.executeUpdate();
            return producto;
        } catch (SQLException e) {
            throw new RuntimeException("Error en persistencia JDBC al guardar Producto", e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(long id) {
        String sql = "SELECT id, nombre, precio, existencia FROM producto WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("existencia")
                    );
                    return Optional.of(producto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta JDBC al buscar Producto por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Producto> obtenerTodos() {
        String sql = "SELECT id, nombre, precio, existencia FROM producto ORDER BY id ASC";
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("existencia")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta JDBC al obtener todos los productos", e);
        }
        return productos;
    }
}

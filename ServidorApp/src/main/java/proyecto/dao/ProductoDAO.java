package proyecto.dao;

import proyecto.interfaces.IProductoDAO;
import proyecto.modelo.Producto;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO {

    public boolean crearProducto(Producto producto) {
        String sql = "INSERT INTO Producto (id, nombre, descripcion, precio, cantidad, precioDeAlquiler) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, producto.getId());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getDescripcion());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getCantidad());
            stmt.setDouble(6, producto.getPrecioDeAlquiler());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto(
                                        rs.getInt("id"),
                                        rs.getString("nombre"),
                                        rs.getString("descripcion"),
                                        rs.getDouble("precio"),
                                        rs.getDouble("precioDeAlquiler"),
                                        rs.getInt("cantidad")
                                );
                productos.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE Producto SET nombre = ?, descripcion = ?, precio = ?, cantidad = ?, precioDeAlquiler = ? WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getCantidad());
            stmt.setDouble(5, producto.getPrecioDeAlquiler());
            stmt.setInt(6, producto.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM Producto WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> obtenerReporteInventario() {
        String sql = """
        SELECT 
            P.nombre AS NombreProducto,
            P.precio,
            P.cantidad,
            P.precio * P.cantidad AS ValorTotalStock
        FROM Producto P
        ORDER BY ValorTotalStock DESC
    """;

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombreProducto", rs.getString("NombreProducto"));
                fila.put("precio", rs.getDouble("precio"));
                fila.put("cantidad", rs.getInt("cantidad"));
                fila.put("valorTotalStock", rs.getDouble("ValorTotalStock"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}

package proyecto.dao;

import proyecto.modelo.Proveedor;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public boolean crearProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO Proveedor (id, nombre, numero_contacto) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getId());
            stmt.setString(2, proveedor.getNombre());
            stmt.setString(3, proveedor.getNumeroContacto());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Proveedor> obtenerProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT id, nombre, numero_contacto FROM Proveedor";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String numero = rs.getString("numero_contacto");

                Proveedor proveedor = new Proveedor(id, nombre, numero);
                proveedores.add(proveedor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proveedores;
    }

    public boolean eliminarProveedor(String id) {
        String sql = "DELETE FROM Proveedor WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProveedor(Proveedor proveedor) {
        String sql = "UPDATE Proveedor SET nombre = ?, numero_contacto = ? WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNumeroContacto());
            stmt.setString(3, proveedor.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Proveedor buscarProveedorPorId(String id) {
        String sql = "SELECT id, nombre, numero_contacto FROM Proveedor WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String numero = rs.getString("numero_contacto");
                return new Proveedor(id, nombre, numero);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

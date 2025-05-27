package proyecto.dao;

import proyecto.modelo.Cargo;
import proyecto.server.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    public List<Cargo> obtenerCargos() {
        List<Cargo> lista = new ArrayList<>();
        String sql = "SELECT idCargo, name, precio_evento FROM Cargo";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idCargo");
                String name = rs.getString("name");
                float precio = rs.getFloat("precio_evento");

                lista.add(new Cargo(id, name, precio));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Agrega un nuevo cargo al sistema
     * @param cargo El cargo a agregar (con id temporal)
     * @return El cargo creado con su ID asignado, o null si hubo un error
     */
    public Cargo agregarCargo(Cargo cargo) {
        // Si el idCargo es 0, generamos un nuevo ID
        if (cargo.getIdCargo() == 0) {
            int nuevoId = obtenerSiguienteId();
            cargo.setIdCargo(nuevoId);
        }

        String sql = "INSERT INTO Cargo (idCargo, name, precio_evento) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cargo.getIdCargo());
            stmt.setString(2, cargo.getName());
            stmt.setFloat(3, cargo.getPrecio_evento());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                return cargo;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza un cargo existente en el sistema
     * @param cargo El cargo con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarCargo(Cargo cargo) {
        String sql = "UPDATE Cargo SET name = ?, precio_evento = ? WHERE idCargo = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cargo.getName());
            stmt.setFloat(2, cargo.getPrecio_evento());
            stmt.setInt(3, cargo.getIdCargo());

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un cargo del sistema
     * @param idCargo El ID del cargo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCargo(int idCargo) {
        String sql = "DELETE FROM Cargo WHERE idCargo = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCargo);

            int filasEliminadas = stmt.executeUpdate();
            return filasEliminadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene el siguiente ID disponible para un nuevo cargo
     * @return El siguiente ID disponible
     */
    private int obtenerSiguienteId() {
        String sql = "SELECT MAX(idCargo) FROM Cargo";
        int maxId = 0;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                maxId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxId + 1;
    }
}

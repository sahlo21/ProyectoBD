package proyecto.dao;

import proyecto.modelo.GestorEvento;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.*;

public class GestorEventoDAO {

    public boolean crearGestor(GestorEvento gestor) {
        // Verificar si el usuario ya existe
        if (existeUsuario(gestor.getUsuario())) {
            return false;
        }

        String sqlUsuario = "INSERT INTO Usuario (cedula, nombre, usuario, contrasena) VALUES (?, ?, ?, ?)";
        String sqlGestor = "INSERT INTO GestorEvento (cedula) VALUES (?)";
        String sqlTelefono = "INSERT INTO Telefonos (cedula_usuario, telefono) VALUES (?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                 PreparedStatement stmtGestor = conn.prepareStatement(sqlGestor);
                 PreparedStatement stmtTelefono = conn.prepareStatement(sqlTelefono)) {

                // Insertar en tabla Usuario
                stmtUsuario.setInt(1, gestor.getCedula());
                stmtUsuario.setString(2, gestor.getNombre());
                stmtUsuario.setString(3, gestor.getUsuario());
                stmtUsuario.setString(4, gestor.getContrasena());
                stmtUsuario.executeUpdate();

                // Insertar en tabla GestorEvento
                stmtGestor.setInt(1, gestor.getCedula());
                stmtGestor.executeUpdate();

                // Insertar teléfonos
                for (String telefono : gestor.getTelefono()) {
                    stmtTelefono.setInt(1, gestor.getCedula());
                    stmtTelefono.setString(2, telefono);
                    stmtTelefono.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarGestor(GestorEvento gestor) {
        String sqlUsuario = "UPDATE Usuario SET nombre = ?, usuario = ?, contrasena = ? WHERE cedula = ?";
        String sqlEliminarTelefonos = "DELETE FROM Telefonos WHERE cedula_usuario = ?";
        String sqlInsertarTelefono = "INSERT INTO Telefonos (cedula_usuario, telefono) VALUES (?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                 PreparedStatement stmtEliminarTelefonos = conn.prepareStatement(sqlEliminarTelefonos);
                 PreparedStatement stmtInsertarTelefono = conn.prepareStatement(sqlInsertarTelefono)) {

                // Actualizar datos de Usuario
                stmtUsuario.setString(1, gestor.getNombre());
                stmtUsuario.setString(2, gestor.getUsuario());
                stmtUsuario.setString(3, gestor.getContrasena());
                stmtUsuario.setInt(4, gestor.getCedula());
                stmtUsuario.executeUpdate();

                // Eliminar teléfonos existentes
                stmtEliminarTelefonos.setInt(1, gestor.getCedula());
                stmtEliminarTelefonos.executeUpdate();

                // Insertar nuevos teléfonos
                for (String telefono : gestor.getTelefono()) {
                    stmtInsertarTelefono.setInt(1, gestor.getCedula());
                    stmtInsertarTelefono.setString(2, telefono);
                    stmtInsertarTelefono.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarGestor(int cedula) {
        String sqlEliminarTelefonos = "DELETE FROM Telefonos WHERE cedula_usuario = ?";
        String sqlEliminarGestor = "DELETE FROM GestorEvento WHERE cedula = ?";
        String sqlEliminarUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtEliminarTelefonos = conn.prepareStatement(sqlEliminarTelefonos);
                 PreparedStatement stmtEliminarGestor = conn.prepareStatement(sqlEliminarGestor);
                 PreparedStatement stmtEliminarUsuario = conn.prepareStatement(sqlEliminarUsuario)) {

                // Eliminar teléfonos primero (clave foránea)
                stmtEliminarTelefonos.setInt(1, cedula);
                stmtEliminarTelefonos.executeUpdate();

                // Eliminar registro de GestorEvento
                stmtEliminarGestor.setInt(1, cedula);
                stmtEliminarGestor.executeUpdate();

                // Eliminar registro de Usuario
                stmtEliminarUsuario.setInt(1, cedula);
                stmtEliminarUsuario.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si un nombre de usuario ya existe en la base de datos
     * @param usuario El nombre de usuario a verificar
     * @return true si el usuario ya existe, false en caso contrario
     */
    private boolean existeUsuario(String usuario) {
        String sql = "SELECT COUNT(*) FROM Usuario WHERE usuario = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<GestorEvento> obtenerGestores() {
        Map<Integer, GestorEvento> mapa = new HashMap<>();
        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, tel.telefono " +
                "FROM Usuario u " +
                "INNER JOIN GestorEvento g ON u.cedula = g.cedula " +
                "LEFT JOIN Telefonos tel ON u.cedula = tel.cedula_usuario";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int cedula = rs.getInt("cedula");
                GestorEvento gestor = mapa.get(cedula);

                if (gestor == null) {
                    String nombre = rs.getString("nombre");
                    String usuario = rs.getString("usuario");
                    String contrasena = rs.getString("contrasena");
                    gestor = new GestorEvento(cedula, nombre, usuario, contrasena, new ArrayList<>());
                    mapa.put(cedula, gestor);
                }

                String telefono = rs.getString("telefono");
                if (telefono != null && !gestor.getTelefono().contains(telefono)) {
                    gestor.getTelefono().add(telefono);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(mapa.values());
    }
}

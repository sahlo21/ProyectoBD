package proyecto.dao;

import proyecto.modelo.Cargo;
import proyecto.modelo.Trabajador;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.*;

public class TrabajadorDAO {

    public boolean crearTrabajador(Trabajador trabajador) {
        String sql = "INSERT INTO Usuario (cedula, nombre, usuario, contrasena) VALUES (?, ?, ?, ?)";
        String sqlTrabajador = "INSERT INTO Trabajador (cedula, cargo_id) VALUES (?, ?)";
        String sqlTelefono = "INSERT INTO Telefonos (cedula_usuario, telefono) VALUES (?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sql);
                 PreparedStatement stmtTrabajador = conn.prepareStatement(sqlTrabajador);
                 PreparedStatement stmtTelefono = conn.prepareStatement(sqlTelefono)) {

                stmtUsuario.setInt(1, trabajador.getCedula());
                stmtUsuario.setString(2, trabajador.getNombre());
                stmtUsuario.setString(3, trabajador.getUsuario());
                stmtUsuario.setString(4, trabajador.getContrasena());
                stmtUsuario.executeUpdate();

                stmtTrabajador.setInt(1, trabajador.getCedula());
                stmtTrabajador.setInt(2, trabajador.getCargo().getIdCargo());
                stmtTrabajador.executeUpdate();

                for (String telefono : trabajador.getTelefono()) {
                    stmtTelefono.setInt(1, trabajador.getCedula());
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

    public List<Trabajador> obtenerTrabajadores() {
        Map<Integer, Trabajador> mapa = new HashMap<>();
        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, " +
                "t.cargo_id, c.precio_evento, tel.telefono " +
                "FROM Usuario u " +
                "INNER JOIN Trabajador t ON u.cedula = t.cedula " +
                "INNER JOIN Cargo c ON t.cargo_id = c.idCargo " +
                "LEFT JOIN Telefonos tel ON u.cedula = tel.cedula_usuario";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int cedula = rs.getInt("cedula");
                Trabajador trabajador = mapa.get(cedula);

                if (trabajador == null) {
                    String nombre = rs.getString("nombre");
                    String usuario = rs.getString("usuario");
                    String contrasena = rs.getString("contrasena");
                    int cargoId = rs.getInt("cargo_id");
                    float precioEvento = rs.getFloat("precio_evento");
                    Cargo cargo = new Cargo(cargoId, 1, precioEvento);
                    trabajador = new Trabajador(cedula, nombre, usuario, contrasena, new ArrayList<>(), cargo);
                    mapa.put(cedula, trabajador);
                }

                String telefono = rs.getString("telefono");
                if (telefono != null && !trabajador.getTelefono().contains(telefono)) {
                    trabajador.getTelefono().add(telefono);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(mapa.values());
    }
}

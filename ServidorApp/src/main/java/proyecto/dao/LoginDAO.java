package proyecto.dao;

import proyecto.modelo.Administrador;
import proyecto.modelo.Cargo;
import proyecto.modelo.Trabajador;
import proyecto.modelo.Usuario;

import proyecto.server.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginDAO {

    public Usuario iniciarSesion(String usuario, String contrasena) {
        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, " +
                     "CASE " +
                     "WHEN a.cedula IS NOT NULL THEN 'ADMINISTRADOR' " +
                     "WHEN g.cedula IS NOT NULL THEN 'GESTOREVENTO' " +
                     "WHEN t.cedula IS NOT NULL THEN 'TRABAJADOR' " +
                     "END AS tipo " +
                     "FROM Usuario u " +
                     "LEFT JOIN Administrador a ON u.cedula = a.cedula " +
                     "LEFT JOIN GestorEvento g ON u.cedula = g.cedula " +
                     "LEFT JOIN Trabajador t ON u.cedula = t.cedula " +
                     "WHERE u.usuario = ? AND u.contrasena = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int cedula = rs.getInt("cedula");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo");
                ArrayList<String> Telefono = new ArrayList<>();
                Telefono.add("12312");
                switch (tipo) {
                    case "ADMINISTRADOR":
                        return new Administrador(cedula, nombre, usuario, contrasena, Telefono);
                    case "GESTOREVENTO":
                        return null;
                    case "TRABAJADOR":
                        return new Trabajador(cedula, nombre, usuario, contrasena, Telefono, Cargo.DESCARGUE, 0);
                    default:
                        return null;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

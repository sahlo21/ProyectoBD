package proyecto.dao;

import proyecto.modelo.Administrador;
import proyecto.modelo.Cargo;
import proyecto.modelo.Trabajador;
import proyecto.modelo.Usuario;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.ArrayList;

public class LoginDAO {

    public Usuario iniciarSesion(String usuario, String contrasena) {
        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, " +
                "CASE " +
                "WHEN a.cedula IS NOT NULL THEN 'ADMINISTRADOR' " +
                "WHEN g.cedula IS NOT NULL THEN 'GESTOREVENTO' " +
                "WHEN t.cedula IS NOT NULL THEN 'TRABAJADOR' " +
                "END AS tipo, " +
                "t.cargo_id, c.precio_evento, tel.telefono " +
                "FROM Usuario u " +
                "LEFT JOIN Administrador a ON u.cedula = a.cedula " +
                "LEFT JOIN GestorEvento g ON u.cedula = g.cedula " +
                "LEFT JOIN Trabajador t ON u.cedula = t.cedula " +
                "LEFT JOIN Cargo c ON t.cargo_id = c.idCargo " +
                "LEFT JOIN Telefonos tel ON u.cedula = tel.cedula_usuario " +
                "WHERE u.usuario = ? AND u.contrasena = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            Usuario user = null;
            ArrayList<String> telefonos = new ArrayList<>();
            Cargo cargo = null;

            int cedula = -1;
            String nombre = null;
            String tipo = null;

            while (rs.next()) {
                if (user == null) {
                    cedula = rs.getInt("cedula");
                    nombre = rs.getString("nombre");
                    tipo = rs.getString("tipo");

                    if ("TRABAJADOR".equals(tipo)) {
                        int cargoId = rs.getInt("cargo_id");
                        float precioEvento = rs.getFloat("precio_evento");
                        cargo = new Cargo(cargoId, 1, precioEvento);
                    }
                }

                String telefono = rs.getString("telefono");
                if (telefono != null && !telefonos.contains(telefono)) {
                    telefonos.add(telefono);
                }
            }

            if (cedula != -1 && tipo != null) {
                switch (tipo) {
                    case "ADMINISTRADOR":
                        return new Administrador(cedula, nombre, usuario, contrasena, telefonos);
                    case "TRABAJADOR":
                        return new Trabajador(cedula, nombre, usuario, contrasena, telefonos, cargo);
                    case "GESTOREVENTO":
                        // A implementar si se desea
                        return null;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

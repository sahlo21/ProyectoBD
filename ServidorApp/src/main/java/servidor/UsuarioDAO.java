package servidor;

import servidor.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    public boolean validarUsuario(String correo, String contrasena) {
        String query = "SELECT * FROM Usuario WHERE Correo = ? AND Contraseña = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // retorna true si encuentra el usuario
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

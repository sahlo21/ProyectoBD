package proyecto.dao;


import proyecto.modelo.Cargo;
import proyecto.modelo.Trabajador;
import proyecto.server.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAO {

    public boolean crearTrabajador(Trabajador trabajador) {
        String sql = "INSERT INTO Trabajador (cedula, nombre, usuario, contrasena, telefono, cargo, precio_evento) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trabajador.getCedula());
            stmt.setString(2, trabajador.getNombre());
            stmt.setString(3, trabajador.getUsuario());
            stmt.setString(4, trabajador.getContrasena());
            stmt.setString(5, trabajador.getTelefono().get(0));
            stmt.setString(6, trabajador.getCargo().name());
            stmt.setInt(7, trabajador.getPrecio_evento());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Trabajador> obtenerTrabajadores() {
        List<Trabajador> lista = new ArrayList<>();
        ArrayList<String> Telefono = new ArrayList<>();
        Telefono.add("12312");
        String sql = "SELECT * FROM Trabajador"; // Ajusta según tu BD
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int cedula = rs.getInt("cedula");
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("usuario");
                String contrasena = rs.getString("contrasena");
                ArrayList<String> telefonos = new ArrayList<>();
                telefonos.add(rs.getString("telefono"));

                String cargoStr = rs.getString("cargo");
                int precio = rs.getInt("precio_evento");

                Cargo cargo = Cargo.valueOf(cargoStr.toUpperCase());

                Trabajador trabajador = new Trabajador(cedula, nombre, usuario, contrasena, telefonos, cargo, precio);
                lista.add(trabajador);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Manejo real: logger
        }

        return lista;
    }
}

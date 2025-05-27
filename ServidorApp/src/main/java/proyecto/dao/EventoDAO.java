package proyecto.dao;

import proyecto.modelo.Evento;
import java.sql.*;
import java.util.*;
import proyecto.server.ConexionBD;

public class EventoDAO {

    public boolean crearEvento(Evento evento) {
        String sql = "INSERT INTO Evento (idEvento, nombre, fecha, lugar, precio) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, evento.getId()); // idEvento ingresado manualmente
            stmt.setString(2, evento.getNombre());
            stmt.setDate(3, new java.sql.Date(evento.getFecha().getTime()));
            stmt.setString(4, evento.getLugar());
            stmt.setDouble(5, evento.getPrecio());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean actualizarEvento(Evento evento) {
        String sql = "UPDATE Evento SET nombre = ?, fecha = ?, lugar = ?, precio = ? WHERE idEvento = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evento.getNombre());
            stmt.setDate(2, new java.sql.Date(evento.getFecha().getTime()));
            stmt.setString(3, evento.getLugar());
            stmt.setDouble(4, evento.getPrecio());
            stmt.setInt(5, evento.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarEvento(int idEvento) {
        String sql = "DELETE FROM Evento WHERE idEvento = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Evento> obtenerEventos() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT idEvento, nombre, fecha, lugar, precio FROM Evento";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getInt("idEvento"));
                evento.setNombre(rs.getString("nombre"));
                evento.setFecha(rs.getDate("fecha"));
                evento.setLugar(rs.getString("lugar"));
                evento.setPrecio(rs.getFloat("precio"));
                lista.add(evento);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}


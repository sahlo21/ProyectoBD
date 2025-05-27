package proyecto.dao;

import proyecto.modelo.Evento;
import java.sql.*;
import java.util.*;

import proyecto.modelo.Producto;
import proyecto.modelo.Trabajador;
import proyecto.server.ConexionBD;

public class EventoDAO {

    public boolean crearEvento(Evento evento) {
        String sqlEvento = "INSERT INTO Evento (idEvento, nombre, fecha, lugar, precio) VALUES (?, ?, ?, ?, ?)";
        String sqlTrabajador = "INSERT INTO Evento_Trabajador (idEvento, idTrabajador) VALUES (?, ?)";
        String sqlProducto = "INSERT INTO Evento_Producto (idEvento, idProducto) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmtEvento = null;
        PreparedStatement stmtTrabajador = null;
        PreparedStatement stmtProducto = null;

        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            // Insertar evento
            stmtEvento = conn.prepareStatement(sqlEvento);
            stmtEvento.setInt(1, evento.getId());
            stmtEvento.setString(2, evento.getNombre());
            stmtEvento.setDate(3, new java.sql.Date(evento.getFecha().getTime()));
            stmtEvento.setString(4, evento.getLugar());
            stmtEvento.setDouble(5, evento.getPrecio());
            stmtEvento.executeUpdate();

            // Insertar trabajadores asociados
            stmtTrabajador = conn.prepareStatement(sqlTrabajador);
            for (Trabajador t : evento.getTrabajadores()) {
                stmtTrabajador.setInt(1, evento.getId());
                stmtTrabajador.setInt(2, t.getCedula());
                stmtTrabajador.addBatch();
            }
            stmtTrabajador.executeBatch();

            // Insertar productos asociados
            stmtProducto = conn.prepareStatement(sqlProducto);
            for (Producto p : evento.getProductos()) {
                stmtProducto.setInt(1, evento.getId());
                stmtProducto.setInt(2, p.getId());
                stmtProducto.addBatch();
            }
            stmtProducto.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { if (stmtEvento != null) stmtEvento.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmtTrabajador != null) stmtTrabajador.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmtProducto != null) stmtProducto.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
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


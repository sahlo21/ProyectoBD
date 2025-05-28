package proyecto.dao;

import proyecto.modelo.Evento;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import proyecto.modelo.Producto;
import proyecto.modelo.Trabajador;
import proyecto.server.ConexionBD;

public class EventoDAO {

    public boolean crearEvento(Evento evento) {
        String sqlEvento = "INSERT INTO Evento (idEvento, nombre, fecha, lugar, precio, idCliente) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlTrabajador = "INSERT INTO Evento_Trabajador (idEvento, idTrabajador) VALUES (?, ?)";
        String sqlProducto = "INSERT INTO Evento_Producto (idEvento, idProducto) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmtEvento = null;
        PreparedStatement stmtTrabajador = null;
        PreparedStatement stmtProducto = null;

        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);
            System.out.println("Obtiene conexion");
            // Insertar evento
            stmtEvento = conn.prepareStatement(sqlEvento);
            stmtEvento.setInt(1, evento.getId());
            stmtEvento.setString(2, evento.getNombre());
            stmtEvento.setDate(3, new java.sql.Date(evento.getFecha().getTime()));
            stmtEvento.setString(4, evento.getLugar());
            stmtEvento.setDouble(5, evento.getPrecio());
            stmtEvento.setInt(6, evento.getCliente().getCedula());
            stmtEvento.executeUpdate();
            System.out.println("Evento insertado correctamente");
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
    public List<Map<String, Object>> obtenerFacturaEvento(int idEvento) {
        String sql = """
        SELECT 
            E.idEvento,
            E.nombre AS nombreEvento,
            E.fecha,
            E.precio AS precioBase,
            E.lugar,
            C.nombre AS nombreCliente,
            C.cedula AS cedulaCliente
        FROM Evento E
        JOIN Cliente C ON E.idCliente = C.cedula
        WHERE E.idEvento = ?
    """;

        String sqlTrabajadores = """
        SELECT 
            U.nombre AS nombreTrabajador,
            CA.name AS cargo,
            CA.precio_evento AS precio
        FROM Evento_Trabajador ET
        JOIN Trabajador T ON ET.idTrabajador = T.cedula
        JOIN Usuario U ON T.cedula = U.cedula
        JOIN Cargo CA ON T.cargo_id = CA.idCargo
        WHERE ET.idEvento = ?
    """;

        String sqlProductos = """
        SELECT 
            P.nombre AS nombreProducto,
            P.cantidad,
            P.precioDeAlquiler AS precioUnitario,
            (P.precioDeAlquiler * P.cantidad) AS subtotal
        FROM Evento_Producto EP
        JOIN Producto P ON EP.idProducto = P.id
        WHERE EP.idEvento = ?
    """;

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion()) {
            Map<String, Object> datos = new HashMap<>();

            // Evento + Cliente
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idEvento);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    datos.put("evento", rs.getString("nombreEvento"));
                    datos.put("lugar", rs.getString("lugar"));
                    datos.put("cliente", rs.getString("nombreCliente"));
                    datos.put("cedulaCliente", rs.getString("cedulaCliente"));
                    datos.put("idEvento", rs.getInt("idEvento"));

                    Date fecha = rs.getDate("fecha");
                    datos.put("fecha", fecha != null ? new SimpleDateFormat("dd/MM/yyyy").format(fecha) : "");

                    double precioBase = rs.getDouble("precioBase");
                    datos.put("precioBase", rs.wasNull() ? 0.0 : precioBase);
                } else {
                    // Si no hay evento, llenar con datos vacíos para evitar NPE
                    datos.put("evento", "");
                    datos.put("lugar", "");
                    datos.put("cliente", "");
                    datos.put("cedulaCliente", "");
                    datos.put("idEvento", idEvento);
                    datos.put("fecha", "");
                    datos.put("precioBase", 0.0);
                }
            }

            // Trabajadores
            List<Map<String, Object>> trabajadores = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlTrabajadores)) {
                ps.setInt(1, idEvento);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("nombre", rs.getString("nombreTrabajador"));
                    fila.put("cargo", rs.getString("cargo"));
                    fila.put("precio", rs.getDouble("precio"));
                    trabajadores.add(fila);
                }
            }
            datos.put("trabajadores", trabajadores); // siempre agrega la lista, vacía o no

            // Productos
            List<Map<String, Object>> productos = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sqlProductos)) {
                ps.setInt(1, idEvento);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("nombre", rs.getString("nombreProducto"));
                    fila.put("cantidad", rs.getInt("cantidad"));
                    fila.put("precioUnitario", rs.getDouble("precioUnitario"));
                    fila.put("subtotal", rs.getDouble("subtotal"));
                    productos.add(fila);
                }
            }
            datos.put("productos", productos); // siempre agrega la lista, vacía o no

            lista.add(datos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Map<String, Object>> obtenerReporteAlquileresMensuales() {
        String sql = """
        SELECT 
            DATENAME(MONTH, E.fecha) AS Mes,
            P.nombre AS Producto,
            COUNT(*) AS VecesAlquilado,
            SUM(P.precioDeAlquiler) AS IngresoTotal
        FROM Evento E
        JOIN Evento_Producto EP ON E.idEvento = EP.idEvento
        JOIN Producto P ON P.id = EP.idProducto
        GROUP BY DATENAME(MONTH, E.fecha), P.nombre
        ORDER BY Mes, Producto
    """;

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("mes", rs.getString("Mes"));
                fila.put("producto", rs.getString("Producto"));
                fila.put("vecesAlquilado", rs.getInt("VecesAlquilado"));
                fila.put("ingresoTotal", rs.getDouble("IngresoTotal"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}


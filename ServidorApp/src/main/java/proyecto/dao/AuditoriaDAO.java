package proyecto.dao;

import proyecto.modelo.Auditoria;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase para manejar las operaciones de base de datos relacionadas con la auditoría
 */
public class AuditoriaDAO {

    /**
     * Registra una nueva entrada en la tabla de auditoría
     * @param auditoria El objeto Auditoria a registrar
     * @return true si se registró correctamente, false en caso contrario
     */
    public boolean registrarAuditoria(Auditoria auditoria) {
        String sql = "INSERT INTO auditoria (id_usuario, nombre_usuario, accion, fecha_hora) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, auditoria.getIdUsuario());
            stmt.setString(2, auditoria.getNombreUsuario());
            stmt.setString(3, auditoria.getAccion());
            stmt.setTimestamp(4, Timestamp.valueOf(auditoria.getFechaHora()));
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar auditoría: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todos los registros de auditoría
     * @return Lista de objetos Auditoria
     */
    public List<Auditoria> obtenerRegistrosAuditoria() {
        List<Auditoria> registros = new ArrayList<>();
        String sql = "SELECT id, id_usuario, nombre_usuario, accion, fecha_hora FROM auditoria ORDER BY fecha_hora DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Auditoria auditoria = new Auditoria();
                auditoria.setId(rs.getInt("id"));
                auditoria.setIdUsuario(rs.getInt("id_usuario"));
                auditoria.setNombreUsuario(rs.getString("nombre_usuario"));
                auditoria.setAccion(rs.getString("accion"));
                auditoria.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                
                registros.add(auditoria);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros de auditoría: " + e.getMessage());
            e.printStackTrace();
        }
        
        return registros;
    }
    
    /**
     * Obtiene los registros de auditoría de un usuario específico
     * @param idUsuario ID del usuario
     * @return Lista de objetos Auditoria del usuario
     */
    public List<Auditoria> obtenerRegistrosPorUsuario(int idUsuario) {
        List<Auditoria> registros = new ArrayList<>();
        String sql = "SELECT id, id_usuario, nombre_usuario, accion, fecha_hora FROM auditoria WHERE id_usuario = ? ORDER BY fecha_hora DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Auditoria auditoria = new Auditoria();
                    auditoria.setId(rs.getInt("id"));
                    auditoria.setIdUsuario(rs.getInt("id_usuario"));
                    auditoria.setNombreUsuario(rs.getString("nombre_usuario"));
                    auditoria.setAccion(rs.getString("accion"));
                    auditoria.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    
                    registros.add(auditoria);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros de auditoría por usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return registros;
    }
    public List<Map<String, Object>> obtenerReporteAuditoria() {
        String sql = """
        SELECT 
            U.nombre AS NombreUsuario,
            A.accion,
            COUNT(*) AS TotalAcciones,
            MIN(A.fecha_hora) AS PrimeraAccion,
            MAX(A.fecha_hora) AS UltimaAccion
        FROM auditoria A
        JOIN Usuario U ON A.id_usuario = U.cedula
        GROUP BY U.nombre, A.accion
        ORDER BY U.nombre
    """;

        List<Map<String, Object>> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombreUsuario", rs.getString("NombreUsuario"));
                fila.put("accion", rs.getString("accion"));
                fila.put("totalAcciones", rs.getInt("TotalAcciones"));
                fila.put("primeraAccion", rs.getTimestamp("PrimeraAccion"));
                fila.put("ultimaAccion", rs.getTimestamp("UltimaAccion"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
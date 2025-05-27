package proyecto.dao;

import proyecto.modelo.Cargo;
import proyecto.modelo.Trabajador;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.*;

public class TrabajadorDAO {

    public boolean crearTrabajador(Trabajador trabajador) {
        // Verificar si el usuario ya existe
        if (existeUsuario(trabajador.getUsuario())) {
            return false;
        }

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

    public boolean actualizarTrabajador(Trabajador trabajador) {
        String sqlUsuario = "UPDATE Usuario SET nombre = ?, usuario = ?, contrasena = ? WHERE cedula = ?";
        String sqlTrabajador = "UPDATE Trabajador SET cargo_id = ? WHERE cedula = ?";
        String sqlEliminarTelefonos = "DELETE FROM Telefonos WHERE cedula_usuario = ?";
        String sqlInsertarTelefono = "INSERT INTO Telefonos (cedula_usuario, telefono) VALUES (?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                 PreparedStatement stmtTrabajador = conn.prepareStatement(sqlTrabajador);
                 PreparedStatement stmtEliminarTelefonos = conn.prepareStatement(sqlEliminarTelefonos);
                 PreparedStatement stmtInsertarTelefono = conn.prepareStatement(sqlInsertarTelefono)) {

                // Actualizar datos de Usuario
                stmtUsuario.setString(1, trabajador.getNombre());
                stmtUsuario.setString(2, trabajador.getUsuario());
                stmtUsuario.setString(3, trabajador.getContrasena());
                stmtUsuario.setInt(4, trabajador.getCedula());
                stmtUsuario.executeUpdate();

                // Actualizar datos de Trabajador
                stmtTrabajador.setInt(1, trabajador.getCargo().getIdCargo());
                stmtTrabajador.setInt(2, trabajador.getCedula());
                stmtTrabajador.executeUpdate();

                // Eliminar teléfonos existentes
                stmtEliminarTelefonos.setInt(1, trabajador.getCedula());
                stmtEliminarTelefonos.executeUpdate();

                // Insertar nuevos teléfonos
                for (String telefono : trabajador.getTelefono()) {
                    stmtInsertarTelefono.setInt(1, trabajador.getCedula());
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

    public boolean eliminarTrabajador(int cedula) {
        String sqlEliminarTelefonos = "DELETE FROM Telefonos WHERE cedula_usuario = ?";
        String sqlEliminarTrabajador = "DELETE FROM Trabajador WHERE cedula = ?";
        String sqlEliminarUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtEliminarTelefonos = conn.prepareStatement(sqlEliminarTelefonos);
                 PreparedStatement stmtEliminarTrabajador = conn.prepareStatement(sqlEliminarTrabajador);
                 PreparedStatement stmtEliminarUsuario = conn.prepareStatement(sqlEliminarUsuario)) {

                // Eliminar teléfonos primero (clave foránea)
                stmtEliminarTelefonos.setInt(1, cedula);
                stmtEliminarTelefonos.executeUpdate();

                // Eliminar registro de Trabajador
                stmtEliminarTrabajador.setInt(1, cedula);
                stmtEliminarTrabajador.executeUpdate();

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

    public List<Trabajador> obtenerTrabajadores() {
        Map<Integer, Trabajador> mapa = new HashMap<>();
        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, " +
                "t.cargo_id, c.name, c.precio_evento, tel.telefono " +
                "FROM Usuario u " +
                "INNER JOIN Trabajador t ON u.cedula = t.cedula " +
                "LEFT JOIN Cargo c ON t.cargo_id = c.idCargo " +
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

                    // Handle potentially null cargo values
                    Cargo cargo;
                    int cargoId = rs.getInt("cargo_id");
                    // rs.getInt returns 0 for NULL values, so we need to check if the column was actually NULL
                    if (rs.wasNull()) {
                        // Create a default cargo if cargo_id is null
                        cargo = new Cargo(1, "Default", 0);
                    } else {
                        String cargoName = rs.getString("name");
                        // If name is null, use "Default" as fallback
                        if (rs.wasNull() || cargoName == null || cargoName.isEmpty()) {
                            cargoName = "Default";
                        }

                        float precioEvento = rs.getFloat("precio_evento");
                        // If precio_evento is null, use 0 as default
                        if (rs.wasNull()) {
                            precioEvento = 0;
                        }
                        cargo = new Cargo(cargoId, cargoName, precioEvento);
                    }

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
    public List<Map<String,Object>> obtenerTrabajadoresConCargoYPrecio() {
        String sql = """
        SELECT
            t.cedula                   AS cedulaTrabajador,
            u.nombre                   AS nombreTrabajador,
            c.name                     AS nombreCargo,
            c.precio_evento            AS precioPorEvento
        FROM dbo.Trabajador AS t
        INNER JOIN dbo.Usuario  AS u
            ON t.cedula = u.cedula
        INNER JOIN dbo.Cargo    AS c
            ON t.cargo_id = c.idCargo
        """;

        List<Map<String,Object>> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String,Object> fila = new HashMap<>();
                fila.put("cedulaTrabajador", rs.getInt("cedulaTrabajador"));
                fila.put("nombreTrabajador", rs.getString("nombreTrabajador"));
                fila.put("nombreCargo",      rs.getString("nombreCargo"));
                fila.put("precioPorEvento",  rs.getDouble("precioPorEvento"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public List<Map<String,Object>> obtenerResumenEventosConCostos() {
        String sql = """
        SELECT 
            E.nombre AS nombreEvento,
            E.fecha,
            E.precio AS precioEntrada,
            COUNT(ET.idTrabajador) AS cantidadTrabajadores,
            SUM(CA.precio_evento) AS costoTotalTrabajadores,
            E.precio + SUM(CA.precio_evento) AS totalCostoEvento
        FROM Evento E
        JOIN Evento_Trabajador ET ON E.idEvento = ET.idEvento
        JOIN Trabajador T ON ET.idTrabajador = T.cedula
        JOIN Cargo CA ON T.cargo_id = CA.idCargo
        GROUP BY E.nombre, E.fecha, E.precio
    """;

        List<Map<String,Object>> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String,Object> fila = new HashMap<>();
                fila.put("nombreEvento", rs.getString("nombreEvento"));
                fila.put("fecha", rs.getDate("fecha"));
                fila.put("precioEntrada", rs.getDouble("precioEntrada"));
                fila.put("cantidadTrabajadores", rs.getInt("cantidadTrabajadores"));
                fila.put("costoTotalTrabajadores", rs.getDouble("costoTotalTrabajadores"));
                fila.put("totalCostoEvento", rs.getDouble("totalCostoEvento"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}

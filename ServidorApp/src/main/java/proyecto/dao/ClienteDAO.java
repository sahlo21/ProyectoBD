package proyecto.dao;

import proyecto.modelo.Cliente;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.*;

public class ClienteDAO {

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        Map<Integer, Cliente> mapa = new HashMap<>();

        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, tel.telefono " +
                "FROM Usuario u " +
                "INNER JOIN Cliente c ON u.cedula = c.cedula " +
                "LEFT JOIN Telefonos tel ON u.cedula = tel.cedula_usuario";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int cedula = rs.getInt("cedula");
                Cliente cliente = mapa.get(cedula);

                if (cliente == null) {
                    String nombre = rs.getString("nombre");
                    String usuario = rs.getString("usuario");
                    String contrasena = rs.getString("contrasena");
                    cliente = new Cliente(cedula, nombre, usuario, contrasena, new ArrayList<>());
                    mapa.put(cedula, cliente);
                }

                String telefono = rs.getString("telefono");
                if (telefono != null && !cliente.getTelefono().contains(telefono)) {
                    cliente.getTelefono().add(telefono);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(mapa.values());
    }

    // Insertar nuevo cliente
    public boolean insertarCliente(Cliente cliente) {
        String sqlUsuario = "INSERT INTO Usuario (cedula, nombre, usuario, contrasena) VALUES (?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO Cliente (cedula) VALUES (?)";
        String sqlTelefono = "INSERT INTO Telefonos (telefono, cedula_usuario) VALUES (?, ?)";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario);
                 PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente);
                 PreparedStatement stmtTelefono = conn.prepareStatement(sqlTelefono)) {

                // Insertar en Usuario
                stmtUsuario.setInt(1, cliente.getCedula());
                stmtUsuario.setString(2, cliente.getNombre());
                stmtUsuario.setString(3, cliente.getUsuario());
                stmtUsuario.setString(4, cliente.getContrasena());
                stmtUsuario.executeUpdate();

                // Insertar en Cliente
                stmtCliente.setInt(1, cliente.getCedula());
                stmtCliente.executeUpdate();

                // Insertar teléfonos
                for (String telefono : cliente.getTelefono()) {
                    stmtTelefono.setString(1, telefono);
                    stmtTelefono.setInt(2, cliente.getCedula());
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

    // Actualizar cliente (solo nombre, usuario y contraseña)
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE Usuario SET nombre = ?, usuario = ?, contrasena = ? WHERE cedula = ?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getUsuario());
            stmt.setString(3, cliente.getContrasena());
            stmt.setInt(4, cliente.getCedula());

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar cliente (Usuario, Cliente y sus teléfonos)
    public boolean eliminarCliente(int cedula) {
        String sqlTelefonos = "DELETE FROM Telefonos WHERE cedula_usuario = ?";
        String sqlCliente = "DELETE FROM Cliente WHERE cedula = ?";
        String sqlUsuario = "DELETE FROM Usuario WHERE cedula = ?";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtTel = conn.prepareStatement(sqlTelefonos);
                 PreparedStatement stmtCli = conn.prepareStatement(sqlCliente);
                 PreparedStatement stmtUsu = conn.prepareStatement(sqlUsuario)) {

                // Eliminar teléfonos
                stmtTel.setInt(1, cedula);
                stmtTel.executeUpdate();

                // Eliminar de Cliente
                stmtCli.setInt(1, cedula);
                stmtCli.executeUpdate();

                // Eliminar de Usuario
                stmtUsu.setInt(1, cedula);
                int filasEliminadas = stmtUsu.executeUpdate();

                conn.commit();
                return filasEliminadas > 0;

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

    public Cliente buscarClientePorCedula(int cedula) {
        Cliente cliente = null;

        String sql = "SELECT u.cedula, u.nombre, u.usuario, u.contrasena, tel.telefono " +
                " FROM Usuario u " +
                " INNER JOIN Cliente c ON u.cedula = c.cedula " +
                " LEFT JOIN Telefonos tel ON u.cedula = tel.cedula_usuario " +
                " WHERE u.cedula = 7";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cedula);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> telefonos = new ArrayList<>();
            String nombre = null, usuario = null, contrasena = null;
            boolean encontrado = false;
            System.out.println("Vamos aquí");
            while (rs.next()) {
                if (!encontrado) {
                    nombre = rs.getString("nombre");
                    usuario = rs.getString("usuario");
                    contrasena = rs.getString("contrasena");
                    encontrado = true;
                }

                String telefono = rs.getString("telefono");
                if (telefono != null && !telefonos.contains(telefono)) {
                    telefonos.add(telefono);
                }
            }

            if (encontrado) {
                cliente = new Cliente(cedula, nombre, usuario, contrasena, telefonos);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cliente;
    }
}


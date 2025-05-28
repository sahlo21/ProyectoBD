package proyecto.dao;

import proyecto.modelo.Cliente;
import proyecto.server.ConexionBD;

import java.sql.*;
import java.util.*;

public class ClienteDAO {

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT cedula, nombre, edad, direccion, genero, telefono FROM Cliente";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cedula"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getString("direccion"),
                        rs.getString("genero"),
                        rs.getString("telefono")
                );
                lista.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Insertar nuevo cliente
    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (cedula, nombre, edad, direccion, genero, telefono) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setInt(3, cliente.getEdad());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getGenero());
            stmt.setString(6, cliente.getTelefono());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar cliente (solo nombre, usuario y contraseña)
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE Cliente SET nombre = ?, edad = ?, direccion = ?, genero = ?, telefono = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setInt(2, cliente.getEdad());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getGenero());
            stmt.setString(5, cliente.getTelefono());
            stmt.setInt(6, cliente.getCedula());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar cliente (Usuario, Cliente y sus teléfonos)
    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // En ClienteDAO.java
    public Cliente buscarClientePorId(int id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM Cliente WHERE cedula = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                cliente = new Cliente(
                        rs.getInt("cedula"),
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        rs.getString("direccion"),
                        rs.getString("genero"),
                        rs.getString("telefono")


                );
                cliente.setId(rs.getInt("cedula"));
                System.out.println("Cliente encontrado: " + cliente.getCedula());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }
}


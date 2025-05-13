package proyecto.dao;

import proyecto.modelo.Cargo;
import proyecto.server.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    public List<Cargo> obtenerCargos() {
        List<Cargo> lista = new ArrayList<>();
        String sql = "SELECT idCargo, name, precio_evento FROM Cargo";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idCargo");
                int name = rs.getInt("name"); // o texto si cambias el campo
                float precio = rs.getFloat("precio_evento");

                lista.add(new Cargo(id, name, precio));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

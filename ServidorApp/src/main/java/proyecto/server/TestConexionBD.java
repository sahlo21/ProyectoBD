package proyecto.server;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase para probar la conexión a la base de datos.
 * Esta clase se puede ejecutar para verificar si las credenciales de la base de datos son correctas.
 */
public class TestConexionBD {
    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");
        try {
            Connection conn = ConexionBD.obtenerConexion();
            System.out.println("¡Conexión exitosa! La base de datos está accesible.");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
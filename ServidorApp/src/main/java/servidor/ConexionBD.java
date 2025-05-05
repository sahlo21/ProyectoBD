package servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=InventarioBD;encrypt=true;trustServerCertificate=true;";

    private static final String USER = "usuarioApp"; // El que creaste o 'sa'
    private static final String PASSWORD = "TuClaveSegura123";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

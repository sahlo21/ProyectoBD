package proyecto.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainServidor {
    // Formato para los logs con marca de tiempo
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            logInfo("Servidor iniciado en el puerto 5000...");
            while (true) {
                Socket socket = servidor.accept(); // Esperar conexión
                logInfo("Nueva conexión aceptada desde: " + socket.getInetAddress().getHostAddress());
                new ManejadorCliente(socket).start(); // Crear un hilo para manejar al cliente
            }
        } catch (Exception e) {
            logError("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para logs informativos
    public static void logInfo(String mensaje) {
        System.out.println("[" + LocalDateTime.now().format(formatter) + "] INFO: " + mensaje);
    }

    // Método para logs de error
    public static void logError(String mensaje) {
        System.err.println("[" + LocalDateTime.now().format(formatter) + "] ERROR: " + mensaje);
    }
}
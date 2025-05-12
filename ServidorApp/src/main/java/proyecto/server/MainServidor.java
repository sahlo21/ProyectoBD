package proyecto.server;

import java.net.ServerSocket;
import java.net.Socket;

public class MainServidor {
    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(5000)) {
            System.out.println("Servidor iniciado en puerto 5000...");
            while (true) {
                Socket socket = servidor.accept();
                new ManejadorCliente(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServidor {
    public static void main(String[] args) {
        int puerto = 5000;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en puerto " + puerto);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado");

                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                PrintWriter salida = new PrintWriter(clienteSocket.getOutputStream(), true);

                // Protocolo simple: el cliente envía usuario|clave
                String mensaje = entrada.readLine();
                String[] partes = mensaje.split("\\|");
                String usuario = partes[0];
                String clave = partes[1];

                UsuarioDAO dao = new UsuarioDAO();
                boolean valido = dao.validarUsuario(usuario, clave);

                salida.println(valido ? "OK" : "ERROR");

                clienteSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

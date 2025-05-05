package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientePrueba {
    public static void main(String[] args) {
        String host = "localhost"; // Cambia si el servidor está en otra IP
        int puerto = 5000;

        try (Socket socket = new Socket(host, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Simula datos de login
            String usuario = "ana.torres@email.com";
            String clave = "1234pass";

            // Enviar datos al servidor
            salida.println(usuario + "|" + clave);

            // Leer respuesta del servidor
            String respuesta = entrada.readLine();
            if ("OK".equals(respuesta)) {
                System.out.println("Inicio de sesión exitoso.");
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

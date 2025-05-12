package proyecto.servicio;

import proyecto.modelo.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public Usuario iniciarSesion(String usuario, String contrasena) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.LOGIN);
            salida.writeObject(usuario);
            salida.writeObject(contrasena);

            String respuesta = (String) entrada.readObject();
            System.out.println("Respuesta: " + respuesta);
            if ("OK".equals(respuesta)) {
                return (Usuario) entrada.readObject(); // Puede ser Trabajador, Admin, etc.
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package proyecto.servicio;

import proyecto.modelo.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    // Variable para almacenar el usuario actualmente logueado
    private static Usuario usuarioActual = null;

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
                Usuario usuarioLogueado = (Usuario) entrada.readObject(); // Puede ser Trabajador, Admin, etc.

                // Guardar el usuario actual para poder cerrar sesión después
                usuarioActual = usuarioLogueado;

                // Registrar el evento de login en la auditoría
                AuditoriaServicio auditoriaServicio = new AuditoriaServicio();
                auditoriaServicio.registrarAuditoria(usuarioLogueado, "LOGIN");

                return usuarioLogueado;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Cierra la sesión del usuario actual y registra el evento en la auditoría
     * @return true si se cerró la sesión correctamente, false en caso contrario
     */
    public boolean cerrarSesion() {
        if (usuarioActual == null) {
            return false;
        }

        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.LOGOUT);
            salida.writeObject(usuarioActual.getCedula());

            // Registrar el evento de logout en la auditoría
            AuditoriaServicio auditoriaServicio = new AuditoriaServicio();
            boolean registrado = auditoriaServicio.registrarAuditoria(usuarioActual, "LOGOUT");

            // Limpiar el usuario actual
            usuarioActual = null;

            return registrado;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene el usuario actualmente logueado
     * @return El usuario actual o null si no hay ninguno
     */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
}

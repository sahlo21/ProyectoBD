package proyecto.servicio;

import proyecto.modelo.GestorEvento;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GestorServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static List<GestorEvento> obtenerGestores() {
        List<GestorEvento> gestores = new ArrayList<>();
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.OBTENER_GESTORES);

            Object respuesta = in.readObject();
            if (respuesta instanceof List<?>) {
                for (Object obj : (List<?>) respuesta) {
                    if (obj instanceof GestorEvento) {
                        gestores.add((GestorEvento) obj);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return gestores;
    }

    public GestorEvento crearGestor(GestorEvento gestor) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.CREAR_GESTOR);
            salida.writeObject(gestor);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof GestorEvento) {
                return (GestorEvento) respuesta;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarGestor(GestorEvento gestorActualizado) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ACTUALIZAR_GESTOR);
            salida.writeObject(gestorActualizado);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof Boolean) {
                return (Boolean) respuesta;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarGestor(int cedula) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ELIMINAR_GESTOR);
            salida.writeObject(cedula);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof Boolean) {
                return (Boolean) respuesta;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
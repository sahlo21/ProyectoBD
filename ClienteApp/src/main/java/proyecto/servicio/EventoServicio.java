package proyecto.servicio;

import proyecto.modelo.Evento;
import proyecto.modelo.Trabajador;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EventoServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static List<Evento> obtenerEventos() {
        List<Evento> eventos = new ArrayList<>();
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.OBTENER_EVENTOS);

            Object respuesta = in.readObject();
            if (respuesta instanceof List<?>) {
                for (Object obj : (List<?>) respuesta) {
                    if (obj instanceof Evento) {
                        eventos.add((Evento) obj);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return eventos;
    }

    public static Evento crearEvento(Evento evento) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.CREAR_EVENTO);
            salida.writeObject(evento);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof Evento) {
                return (Evento) respuesta;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*public static List<Evento> obtenerEventos() {
        List<Evento> eventos = new ArrayList<>();
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("OBTENER_EVENTOS");

            Object respuesta = in.readObject();
            if (respuesta instanceof List<?>) {
                for (Object obj : (List<?>) respuesta) {
                    if (obj instanceof Evento) {
                        eventos.add((Evento) obj);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return eventos;
    }*/

    public boolean actualizarEvento(Evento eventoActualizado) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ACTUALIZAR_EVENTO);
            salida.writeObject(eventoActualizado);

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

    public boolean eliminarEvento(int idEvento) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ELIMINAR_EVENTO);
            salida.writeObject(idEvento);

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



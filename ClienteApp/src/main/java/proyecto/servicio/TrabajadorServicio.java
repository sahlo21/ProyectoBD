package proyecto.servicio;


import proyecto.modelo.Trabajador;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static List<Trabajador> obtenerTrabajadores() {
        List<Trabajador> trabajadores = new ArrayList<>();
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("OBTENER_TRABAJADORES");

            Object respuesta = in.readObject();
            if (respuesta instanceof List<?>) {
                for (Object obj : (List<?>) respuesta) {
                    if (obj instanceof Trabajador) {
                        trabajadores.add((Trabajador) obj);
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return trabajadores;
    }

    public Trabajador crearTrabajador(Trabajador trabajador) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.CREAR_TRABAJADOR);
            salida.writeObject(trabajador);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof Trabajador) {
                return (Trabajador) respuesta;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

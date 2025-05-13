package proyecto.servicio;

import proyecto.modelo.Cargo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CargoServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static List<Cargo> obtenerCargos() {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.OBTENER_CARGOS);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof List<?>) {
                List<?> lista = (List<?>) respuesta;
                List<Cargo> cargos = new ArrayList<>();
                for (Object obj : lista) {
                    if (obj instanceof Cargo cargo) {
                        cargos.add(cargo);
                    }
                }
                return cargos;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

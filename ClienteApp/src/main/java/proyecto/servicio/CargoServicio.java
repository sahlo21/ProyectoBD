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

    /**
     * Agrega un nuevo cargo al sistema
     * @param name Identificador del cargo
     * @param precio Precio del servicio del cargo
     * @return El cargo creado o null si hubo un error
     */
    public static Cargo agregarCargo(String name, float precio) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.AGREGAR_CARGO);

            // Crear un cargo temporal con id 0 (el servidor asignará el id real)
            Cargo nuevoCargo = new Cargo(0, name, precio);
            salida.writeObject(nuevoCargo);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof Cargo) {
                return (Cargo) respuesta;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza un cargo existente en el sistema
     * @param cargo El cargo con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public static boolean actualizarCargo(Cargo cargo) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ACTUALIZAR_CARGO);
            salida.writeObject(cargo);

            Object respuesta = entrada.readObject();
            return respuesta instanceof Boolean && (Boolean) respuesta;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Elimina un cargo del sistema
     * @param idCargo El ID del cargo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public static boolean eliminarCargo(int idCargo) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ELIMINAR_CARGO);
            salida.writeObject(idCargo);

            Object respuesta = entrada.readObject();
            return respuesta instanceof Boolean && (Boolean) respuesta;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}

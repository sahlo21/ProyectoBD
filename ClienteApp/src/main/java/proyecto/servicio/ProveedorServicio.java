package proyecto.servicio;

import proyecto.modelo.Proveedor;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ProveedorServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static List<Proveedor> obtenerProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.OBTENER_PROVEEDORES);

            Object estado = in.readObject();
            if ("OK".equals(estado)) {
                Object respuesta = in.readObject();
                if (respuesta instanceof List<?>) {
                    for (Object obj : (List<?>) respuesta) {
                        if (obj instanceof Proveedor) {
                            proveedores.add((Proveedor) obj);
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return proveedores;
    }

    public static boolean crearProveedor(Proveedor proveedor) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.CREAR_PROVEEDOR);
            out.writeObject(proveedor);

            Object respuesta = in.readObject();
            return "OK".equals(respuesta);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarProveedor(Proveedor proveedor) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.ACTUALIZAR_PROVEEDOR);
            out.writeObject(proveedor);

            Object respuesta = in.readObject();
            return "OK".equals(respuesta);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarProveedor(String id) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.ELIMINAR_PROVEEDOR);
            out.writeObject(id);

            Object respuesta = in.readObject();
            return "OK".equals(respuesta);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Proveedor buscarProveedor(String id) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.BUSCAR_PROVEEDOR);
            out.writeObject(id);

            Object estado = in.readObject();
            if ("OK".equals(estado)) {
                Object respuesta = in.readObject();
                if (respuesta instanceof Proveedor) {
                    return (Proveedor) respuesta;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}


package proyecto.servicio;

import proyecto.modelo.Cliente;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClienteServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static List<Cliente> obtenerCliente() {
        List<Cliente> clientes = new ArrayList<>();
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.OBTENER_CLIENTES);

            Object respuesta = in.readObject();
            if (respuesta instanceof List<?>) {
                for (Object obj : (List<?>) respuesta) {
                    if (obj instanceof Cliente) {
                        clientes.add((Cliente) obj);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clientes;
    }
    public static Cliente buscarCliente(int cedula) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.BUSCAR_CLIENTE); // Debes tener este comando en tu enum
            out.writeObject(cedula);

            Object respuesta = in.readObject();
            if (respuesta instanceof Cliente) {
                System.out.println("Tenemosun cliente: " + respuesta);
                return (Cliente) respuesta;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cliente crearCliente(Cliente cliente) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.CREAR_CLIENTE);
            out.writeObject(cliente);

            Object respuesta = in.readObject();
            if (respuesta instanceof Cliente) {
                return (Cliente) respuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarCliente(Cliente cliente) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.ACTUALIZAR_CLIENTE);
            out.writeObject(cliente);

            Object respuesta = in.readObject();
            if (respuesta instanceof Boolean) {
                return (Boolean) respuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminarCliente(int cedula) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.ELIMINAR_CLIENTE);
            out.writeObject(cedula);

            Object respuesta = in.readObject();
            if (respuesta instanceof Boolean) {
                return (Boolean) respuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
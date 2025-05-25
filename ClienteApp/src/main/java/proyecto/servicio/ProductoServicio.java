package proyecto.servicio;

import proyecto.modelo.Producto;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ProductoServicio {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public Producto crearProducto(Producto producto) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.CREAR_PRODUCTO);
            salida.writeObject(producto);

            Object respuesta = entrada.readObject();
            if (respuesta instanceof Producto) {
                return (Producto) respuesta;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Producto> obtenerProductos() {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.OBTENER_PRODUCTOS);
            return (List<Producto>) entrada.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarProducto(Producto producto) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ACTUALIZAR_PRODUCTO);
            salida.writeObject(producto);
            return (boolean) entrada.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {

            salida.writeObject(Comando.ELIMINAR_PRODUCTO);
            salida.writeObject(idProducto);
            return (boolean) entrada.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

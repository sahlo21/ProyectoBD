package proyecto.server;


import proyecto.dao.CargoDAO;
import proyecto.dao.GestorEventoDAO;
import proyecto.dao.LoginDAO;
import proyecto.dao.ProductoDAO;
import proyecto.dao.ProveedorDAO;
import proyecto.dao.TrabajadorDAO;
import proyecto.modelo.Cargo;
import proyecto.modelo.GestorEvento;
import proyecto.modelo.Producto;
import proyecto.modelo.Proveedor;
import proyecto.modelo.Trabajador;
import proyecto.modelo.Usuario;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ManejadorCliente extends Thread {
    private final Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

        ) {
            String comando = (String) entrada.readObject();
            ArrayList<String> Telefono = new ArrayList<>();
            Telefono.add("12312");
            if (Comando.CREAR_TRABAJADOR.equals(comando)) {
                Trabajador trabajador = (Trabajador) entrada.readObject();
                TrabajadorDAO dao = new TrabajadorDAO();
                System.out.println(trabajador.toString());
                boolean exito = dao.crearTrabajador(trabajador);

                if (exito) {
                    System.out.println("exito");
                    // Puedes devolver el mismo objeto recibido o uno actualizado desde la DB (con ID, por ejemplo)
                    salida.writeObject(trabajador);
                } else {
                    System.out.println("no exito");

                    salida.writeObject(null); // para indicar fallo
                }
            }

            if (Comando.OBTENER_TRABAJADORES.equals(comando)) {
                TrabajadorDAO dao = new TrabajadorDAO();
                List<Trabajador> trabajadores = dao.obtenerTrabajadores();
                salida.writeObject(trabajadores);
            }

            if (Comando.ACTUALIZAR_TRABAJADOR.equals(comando)) {
                Trabajador trabajador = (Trabajador) entrada.readObject();
                TrabajadorDAO dao = new TrabajadorDAO();
                boolean exito = dao.actualizarTrabajador(trabajador);
                salida.writeObject(exito);
            }

            if (Comando.ELIMINAR_TRABAJADOR.equals(comando)) {
                int cedula = (int) entrada.readObject();
                TrabajadorDAO dao = new TrabajadorDAO();
                boolean exito = dao.eliminarTrabajador(cedula);
                salida.writeObject(exito);
            }
            if (Comando.LOGIN.equals(comando)) {
                String usuario = (String) entrada.readObject();
                String contrasena = (String) entrada.readObject();

                LoginDAO loginDAO = new LoginDAO();
                Usuario usuarioLogueado = loginDAO.iniciarSesion(usuario, contrasena);

                if (usuarioLogueado != null) {
                    salida.writeObject("OK");
                    salida.writeObject(usuarioLogueado); // Mandamos el objeto completo
                } else {
                    salida.writeObject("ERROR");
                }
            }
            if (Comando.OBTENER_CARGOS.equals(comando)) {
                CargoDAO dao = new CargoDAO();
                List<Cargo> cargos = dao.obtenerCargos();
                salida.writeObject(cargos);
            }
            if (Comando.CREAR_PRODUCTO.equals(comando)) {
                Producto producto = (Producto) entrada.readObject();
                ProductoDAO dao = new ProductoDAO();
                boolean exito = dao.crearProducto(producto);
                if (exito) {
                    salida.writeObject(producto);
                } else {
                    salida.writeObject(null);
                }
            }

            if (Comando.OBTENER_PRODUCTOS.equals(comando)) {
                ProductoDAO dao = new ProductoDAO();
                List<Producto> productos = dao.obtenerProductos();
                salida.writeObject(productos);
            }

            if (Comando.ACTUALIZAR_PRODUCTO.equals(comando)) {
                Producto producto = (Producto) entrada.readObject();
                ProductoDAO dao = new ProductoDAO();
                boolean exito = dao.actualizarProducto(producto);
                salida.writeObject(exito);
            }

            if (Comando.ELIMINAR_PRODUCTO.equals(comando)) {
                int id = (int) entrada.readObject();
                ProductoDAO dao = new ProductoDAO();
                boolean exito = dao.eliminarProducto(id);
                salida.writeObject(exito);
            }
            if (Comando.CREAR_PROVEEDOR.equals(comando)) {
                Proveedor proveedor = (Proveedor) entrada.readObject();
                ProveedorDAO dao = new ProveedorDAO();
                boolean creado = dao.crearProveedor(proveedor);
                salida.writeObject(creado ? "OK" : "ERROR");
            }

            if (Comando.ACTUALIZAR_PROVEEDOR.equals(comando)) {
                Proveedor proveedor = (Proveedor) entrada.readObject();
                ProveedorDAO dao = new ProveedorDAO();
                boolean actualizado = dao.actualizarProveedor(proveedor);
                salida.writeObject(actualizado ? "OK" : "ERROR");
            }

            if (Comando.ELIMINAR_PROVEEDOR.equals(comando)) {
                String idProveedor = (String) entrada.readObject();
                ProveedorDAO dao = new ProveedorDAO();
                boolean eliminado = dao.eliminarProveedor(idProveedor);
                salida.writeObject(eliminado ? "OK" : "ERROR");
            }

            if (Comando.BUSCAR_PROVEEDOR.equals(comando)) {
                String idProveedor = (String) entrada.readObject();
                ProveedorDAO dao = new ProveedorDAO();
                Proveedor proveedor = dao.buscarProveedorPorId(idProveedor);
                if (proveedor != null) {
                    salida.writeObject("OK");
                    salida.writeObject(proveedor);
                } else {
                    salida.writeObject("ERROR");
                }
            }

            if (Comando.OBTENER_PROVEEDORES.equals(comando)) {
                ProveedorDAO dao = new ProveedorDAO();
                List<Proveedor> proveedores = dao.obtenerProveedores();
                salida.writeObject("OK");
                salida.writeObject(proveedores);
            }

            // Gestión de GestorEvento
            if (Comando.CREAR_GESTOR.equals(comando)) {
                GestorEvento gestor = (GestorEvento) entrada.readObject();
                GestorEventoDAO dao = new GestorEventoDAO();
                boolean exito = dao.crearGestor(gestor);

                if (exito) {
                    salida.writeObject(gestor);
                } else {
                    salida.writeObject(null);
                }
            }

            if (Comando.OBTENER_GESTORES.equals(comando)) {
                GestorEventoDAO dao = new GestorEventoDAO();
                List<GestorEvento> gestores = dao.obtenerGestores();
                salida.writeObject(gestores);
            }

            if (Comando.ACTUALIZAR_GESTOR.equals(comando)) {
                GestorEvento gestor = (GestorEvento) entrada.readObject();
                GestorEventoDAO dao = new GestorEventoDAO();
                boolean exito = dao.actualizarGestor(gestor);
                salida.writeObject(exito);
            }

            if (Comando.ELIMINAR_GESTOR.equals(comando)) {
                int cedula = (int) entrada.readObject();
                GestorEventoDAO dao = new GestorEventoDAO();
                boolean exito = dao.eliminarGestor(cedula);
                salida.writeObject(exito);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

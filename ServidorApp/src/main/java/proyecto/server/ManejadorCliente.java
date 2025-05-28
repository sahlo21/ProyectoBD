package proyecto.server;


import proyecto.dao.*;
import proyecto.modelo.*;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            }if (Comando.CREAR_CLIENTE.equals(comando)) {
                Cliente cliente = (Cliente) entrada.readObject();
                ClienteDAO dao = new ClienteDAO();
                System.out.println(cliente.toString());
                boolean exito = dao.insertarCliente(cliente);

                if (exito) {
                    System.out.println("exito");
                    // Puedes devolver el mismo objeto recibido o uno actualizado desde la DB (con ID, por ejemplo)
                    salida.writeObject(cliente);
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
            if (Comando.OBTENER_CLIENTES.equals(comando)) {
                ClienteDAO dao = new ClienteDAO();
                List<Cliente> clientes = dao.obtenerClientes();
                salida.writeObject(clientes);
            }

            if (Comando.AGREGAR_CARGO.equals(comando)) {
                Cargo cargo = (Cargo) entrada.readObject();
                CargoDAO dao = new CargoDAO();
                Cargo cargoCreado = dao.agregarCargo(cargo);
                salida.writeObject(cargoCreado);
            }

            if (Comando.ACTUALIZAR_CARGO.equals(comando)) {
                Cargo cargo = (Cargo) entrada.readObject();
                CargoDAO dao = new CargoDAO();
                boolean exito = dao.actualizarCargo(cargo);
                salida.writeObject(exito);
            }
            if (Comando.ACTUALIZAR_CLIENTE.equals(comando)) {
                Cliente cliente = (Cliente) entrada.readObject();
                ClienteDAO dao = new ClienteDAO();
                boolean exito = dao.actualizarCliente(cliente);
                salida.writeObject(exito);
            }

            if (Comando.ELIMINAR_CARGO.equals(comando)) {
                int idCargo = (int) entrada.readObject();
                CargoDAO dao = new CargoDAO();
                boolean exito = dao.eliminarCargo(idCargo);
                salida.writeObject(exito);
            }

            if (Comando.ELIMINAR_CLIENTE.equals(comando)) {
                int cedula = (int) entrada.readObject();
                ClienteDAO dao = new ClienteDAO();
                boolean exito = dao.eliminarCliente(cedula);
                salida.writeObject(exito);
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

            // Manejo de comandos de auditoría
            if (Comando.REGISTRAR_AUDITORIA.equals(comando)) {
                Auditoria auditoria = (Auditoria) entrada.readObject();
                AuditoriaDAO dao = new AuditoriaDAO();
                boolean exito = dao.registrarAuditoria(auditoria);
                salida.writeObject(exito);
            }

            if (Comando.OBTENER_REGISTROS_AUDITORIA.equals(comando)) {
                AuditoriaDAO dao = new AuditoriaDAO();
                List<Auditoria> registros = dao.obtenerRegistrosAuditoria();
                salida.writeObject(registros);
            }

            // Manejo del comando LOGOUT
            if (Comando.LOGOUT.equals(comando)) {
                int idUsuario = (int) entrada.readObject();
                // No necesitamos hacer nada especial para el logout en el servidor,
                // ya que la auditoría se registra a través del comando REGISTRAR_AUDITORIA
                salida.writeObject(true);
            }
            // Removed duplicate handler for OBTENER_TRABAJADORES (already defined at lines 46-50)

            if (Comando.OBTENER_EVENTOS.equals(comando)) {
                EventoDAO dao = new EventoDAO();
                List<Evento> eventos = dao.obtenerEventos();
                salida.writeObject(eventos);
            }

            if (Comando.CREAR_EVENTO.equals(comando)) {
                Evento evento = (Evento) entrada.readObject();
                EventoDAO dao = new EventoDAO();
                boolean exito = dao.crearEvento(evento);
                if (exito) {
                    salida.writeObject(evento); // Devuelve el evento creado
                } else {
                    salida.writeObject(null); // O algún error si lo prefieres
                }
            }


            if (Comando.BUSCAR_CLIENTE.equals(comando)) {
                int cedula = (int) entrada.readObject();
                ClienteDAO dao = new ClienteDAO();
                Cliente cliente = dao.buscarClientePorId(cedula);
                salida.writeObject(cliente); // cliente puede ser null si no existe
                salida.flush();
            }

            if (Comando.ACTUALIZAR_EVENTO.equals(comando)) {
                Evento evento = (Evento) entrada.readObject();
                EventoDAO dao = new EventoDAO();
                boolean exito = dao.actualizarEvento(evento);
                salida.writeObject(exito);
            }

            if (Comando.ELIMINAR_EVENTO.equals(comando)) {
                int idEvento = (int) entrada.readObject();
                EventoDAO dao = new EventoDAO();
                boolean exito = dao.eliminarEvento(idEvento);
                salida.writeObject(exito);
            }
            // Reporte 1: Trabajadores + Cargo + Precio
            if (Comando.GENERAR_REPORTE_TRABAJADORES_CARGO_PRECIO.equals(comando)) {
                TrabajadorDAO dao = new TrabajadorDAO();
                List<Map<String,Object>> datos = dao.obtenerTrabajadoresConCargoYPrecio();
                salida.writeObject(datos);
            }
            if (Comando.GENERAR_REPORTE_COSTOS_EVENTOS.equals(comando)) {
                TrabajadorDAO dao = new TrabajadorDAO(); // o EventoDAO
                List<Map<String,Object>> datos = dao.obtenerResumenEventosConCostos();
                salida.writeObject(datos);
            }
            if (Comando.GENERAR_REPORTE_INVENTARIO.equals(comando)) {
                ProductoDAO dao = new ProductoDAO();
                List<Map<String, Object>> datos = dao.obtenerReporteInventario();
                salida.writeObject(datos);
            }
            if (Comando.GENERAR_REPORTE_AUDITORIA.equals(comando)) {
                AuditoriaDAO dao = new AuditoriaDAO();
                List<Map<String, Object>> datos = dao.obtenerReporteAuditoria();
                salida.writeObject(datos);
            }
             if (Comando.GENERAR_FACTURA_EVENTO.equals(comando)) {
                int idEvento = (int) entrada.readObject();
                EventoDAO dao = new EventoDAO();
                List<Map<String, Object>> datos = dao.obtenerFacturaEvento(idEvento);
                salida.writeObject(datos);
            }
            if (Comando.GENERAR_REPORTE_ALQUILERES_MENSUALES.equals(comando)) {
                EventoDAO dao = new EventoDAO();
                List<Map<String, Object>> datos = dao.obtenerReporteAlquileresMensuales();
                salida.writeObject(datos);
            }






        } catch (java.net.SocketException se) {
            System.out.println("Conexión cerrada por el cliente: " + se.getMessage());
            // No es necesario imprimir el stack trace completo para conexiones cerradas normalmente
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}

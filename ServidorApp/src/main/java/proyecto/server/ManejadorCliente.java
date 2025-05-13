package proyecto.server;


import proyecto.dao.CargoDAO;
import proyecto.dao.LoginDAO;
import proyecto.dao.TrabajadorDAO;
import proyecto.modelo.Cargo;
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



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package proyecto.servicio;


import java.io.*;
import java.net.Socket;
import java.util.*;

public class ReporteServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    /**
     * Obtiene del servidor la lista de trabajadores con cargo y precio
     */
    public static List<Map<String, Object>> generarReporteTrabajadoresCargoYPrecio() {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in  = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Comando.GENERAR_REPORTE_TRABAJADORES_CARGO_PRECIO);
            Object resp = in.readObject();
            if (resp instanceof List) {
                //noinspection unchecked
                return (List<Map<String, Object>>) resp;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

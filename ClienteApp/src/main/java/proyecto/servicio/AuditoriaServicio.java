package proyecto.servicio;

import proyecto.modelo.Auditoria;
import proyecto.modelo.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para manejar las operaciones de auditoría
 */
public class AuditoriaServicio {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    /**
     * Registra un evento de login o logout en la auditoría
     * @param usuario El usuario que inicia o cierra sesión
     * @param accion La acción realizada ("LOGIN" o "LOGOUT")
     * @return true si se registró correctamente, false en caso contrario
     */
    public boolean registrarAuditoria(Usuario usuario, String accion) {
        if (usuario == null) {
            return false;
        }
        
        Auditoria auditoria = new Auditoria(
            usuario.getCedula(),
            usuario.getNombre(),
            accion,
            LocalDateTime.now()
        );
        
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
            
            salida.writeObject(Comando.REGISTRAR_AUDITORIA);
            salida.writeObject(auditoria);
            
            return (boolean) entrada.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene todos los registros de auditoría
     * @return Lista de registros de auditoría
     */
    public List<Auditoria> obtenerRegistrosAuditoria() {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
            
            salida.writeObject(Comando.OBTENER_REGISTROS_AUDITORIA);
            
            return (List<Auditoria>) entrada.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
package proyecto.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Clase que representa un registro de auditoría para la entrada y salida de usuarios
 */
public class Auditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int idUsuario;
    private String nombreUsuario;
    private String accion;  // "LOGIN" o "LOGOUT"
    private LocalDateTime fechaHora;
    
    // Constructor vacío necesario para serialización
    public Auditoria() {
    }
    
    // Constructor con todos los campos excepto id (generado por la BD)
    public Auditoria(int idUsuario, String nombreUsuario, String accion, LocalDateTime fechaHora) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.accion = accion;
        this.fechaHora = fechaHora;
    }
    
    // Getters y setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getAccion() {
        return accion;
    }
    
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    @Override
    public String toString() {
        return "Auditoria{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", accion='" + accion + '\'' +
                ", fechaHora=" + fechaHora +
                '}';
    }
}
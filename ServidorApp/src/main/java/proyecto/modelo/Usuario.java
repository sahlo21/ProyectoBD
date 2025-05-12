package proyecto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Usuario implements Serializable {

    int cedula;
    String nombre;
    String usuario;
    String contrasena;
    ArrayList<String> Telefono;


    public Usuario(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        Telefono = telefono;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public ArrayList<String> getTelefono() {
        return Telefono;
    }
    public void setTelefono(ArrayList<String> telefono) {
        Telefono = telefono;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario1 = (Usuario) o;
        return cedula == usuario1.cedula && Objects.equals(nombre, usuario1.nombre) && Objects.equals(usuario, usuario1.usuario) && Objects.equals(contrasena, usuario1.contrasena);
    }
    @Override
    public int hashCode() {
        return Objects.hash(cedula, nombre, usuario, contrasena);
    }
   @Override
    public String toString() {
        return "Usuario{" +
                "cedula=" + cedula +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", Telefono=" + Telefono +
                '}';
    }

}

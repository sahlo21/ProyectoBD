package proyecto.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cedula;
    private String nombre;
    private int edad;
    private String direccion;
    private String genero;
    private String telefono;

    public Cliente(int id, String nombre, int edad, String direccion, String genero, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.edad = edad;
        this.direccion = direccion;
        this.genero = genero;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() { return cedula; }
    public void setId(int id) { this.cedula = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + cedula +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", direccion='" + direccion + '\'' +
                ", genero='" + genero + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return cedula == cliente.cedula;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedula);
    }
}
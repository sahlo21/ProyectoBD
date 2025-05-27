package proyecto.modelo;

import java.io.Serializable;

public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    String id;
    String nombre;
    String numeroContacto;

    public Proveedor(String id, String nombre, String numeroContacto) {
        this.id = id;
        this.nombre = nombre;
        this.numeroContacto = numeroContacto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proveedor proveedor = (Proveedor) o;
        return id.equals(proveedor.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", numeroContacto='" + numeroContacto + '\'' +
                '}';
    }
}

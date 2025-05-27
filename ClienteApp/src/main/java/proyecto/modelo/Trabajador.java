package proyecto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Trabajador extends Usuario implements Serializable {
    Cargo cargo;
    // En proyecto/modelo/Trabajador.java
    private static final long serialVersionUID = 1L;
    public Trabajador(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono, Cargo cargo) {
        super(cedula, nombre, usuario, contrasena, telefono);
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trabajador that = (Trabajador) o;
        return Objects.equals(cargo, that.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cargo);
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "cargo=" + cargo +
                ", cedula=" + cedula +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", Telefono=" + Telefono +
                '}';
    }
    public double getPrecio() {
        return this.cargo != null ? this.cargo.getPrecio() : 0.0;
    }
}

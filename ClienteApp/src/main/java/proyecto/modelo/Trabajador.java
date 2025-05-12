package proyecto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Trabajador extends Usuario implements Serializable {
    Cargo cargo;
    int precio_evento;


    public Trabajador(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono, Cargo cargo, int precio_evento) {
        super(cedula, nombre, usuario, contrasena, telefono);
        this.cargo = cargo;
        this.precio_evento = precio_evento;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }



    public int getPrecio_evento() {
        return precio_evento;
    }

    public void setPrecio_evento(int precio_evento) {
        this.precio_evento = precio_evento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trabajador that = (Trabajador) o;
        return precio_evento == that.precio_evento && cargo == that.cargo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cargo, precio_evento);
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "cargo=" + cargo +
                ", precio_evento=" + precio_evento +
                ", cedula=" + cedula +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", Telefono='" + Telefono + '\'' +
                '}';
    }
}

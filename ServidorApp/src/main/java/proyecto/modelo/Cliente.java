package proyecto.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Cliente extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    public Cliente(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono) {
        super(cedula, nombre, usuario, contrasena, telefono);
    }

    // Getters y Setters heredados desde Usuario
    // Si necesitas getters/setters personalizados para Cliente, agrégalos aquí

    @Override
    public String toString() {
        return "Cliente{" +
                "cedula=" + getCedula() +
                ", nombre='" + getNombre() + '\'' +
                ", usuario='" + getUsuario() + '\'' +
                ", telefono=" + getTelefono() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return getCedula() == cliente.getCedula();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCedula());
    }
}


package proyecto.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Cliente extends Usuario implements Serializable {

    public Cliente(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono) {
        super(cedula, nombre, usuario, contrasena, telefono);
    }
}

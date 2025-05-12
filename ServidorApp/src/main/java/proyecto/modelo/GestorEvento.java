package proyecto.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class GestorEvento extends Usuario implements Serializable {

    public GestorEvento(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono) {
        super(cedula, nombre, usuario, contrasena, telefono);
    }
}

package proyecto.modelo;

import proyecto.interfaces.IAdministradorService;

import java.io.Serializable;
import java.util.ArrayList;

public class Administrador extends Usuario implements IAdministradorService, Serializable {
    private static final long serialVersionUID = 1234567890L; // Debe ser el mismo en el servidor


    public Administrador(int cedula, String nombre, String usuario, String contrasena, ArrayList<String> telefono) {
        super(cedula, nombre, usuario, contrasena, telefono);
    }


    @Override
    public void registrarGestor(GestorEvento gestor) {

    }

    @Override
    public void registrarTrabajador(Trabajador trabajador) {

    }

    @Override
    public void actualizarGestor(GestorEvento gestor) {

    }

    @Override
    public void actualizarTrabajador(Trabajador trabajador) {

    }

    @Override
    public void eliminarGestor(int cedula) {

    }

    @Override
    public void eliminarTrabajador(int cedula) {

    }

    @Override
    public void cerrarSesion() {

    }
}

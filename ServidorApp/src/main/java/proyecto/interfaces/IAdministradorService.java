package proyecto.interfaces;

import proyecto.modelo.GestorEvento;
import proyecto.modelo.Trabajador;


public interface IAdministradorService {
    void registrarGestor(GestorEvento gestor);

    void registrarTrabajador(Trabajador trabajador);

    void actualizarGestor(GestorEvento gestor);

    void actualizarTrabajador(Trabajador trabajador);

    void eliminarGestor(int cedula);

    void eliminarTrabajador(int cedula);

    void cerrarSesion();
}

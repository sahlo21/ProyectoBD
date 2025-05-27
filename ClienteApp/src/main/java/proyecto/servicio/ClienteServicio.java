package proyecto.servicio;

import proyecto.modelo.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteServicio {
    // Simulación de base de datos en memoria
    private static final List<Cliente> clientes = new ArrayList<>();

    public static Cliente buscarCliente(int cedula) {
        for (Cliente c : clientes) {
            if (c.getCedula() == cedula) {
                return c;
            }
        }
        return null;
    }

    public static List<Cliente> obtenerClientes() {
        return new ArrayList<>(clientes);
    }

    public static boolean crearCliente(Cliente cliente) {
        if (buscarCliente(cliente.getCedula()) != null) {
            return false; // Ya existe
        }
        clientes.add(cliente);
        return true;
    }

    public static boolean actualizarCliente(Cliente cliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCedula() == cliente.getCedula()) {
                clientes.set(i, cliente);
                return true;
            }
        }
        return false;
    }

    public static boolean eliminarCliente(int cedula) {
        return clientes.removeIf(c -> c.getCedula() == cedula);
    }
}
package proyecto.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

;

public class Evento implements Serializable {
    private int id;
    private String nombre;
    private Date fecha;
    private String lugar;
    private double precio;
    private Cliente cliente;
    private List<Producto> productos;
    private List<Trabajador> trabajadores;

    // Constructor vacío
    public Evento() {}

    // Constructor completo
    public Evento(int id, String nombre, Date fecha, String lugar, double precio,
                  Cliente cliente, List<Producto> equipos, List<Trabajador> trabajadores) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.precio = precio;
        this.cliente = cliente;
        this.productos = productos;
        this.trabajadores = trabajadores;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Producto> getEquipos() {
        return productos;
    }

    public void setEquipos(List<Producto> equipos) {
        this.productos = equipos;
    }

    public List<Trabajador> getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(List<Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
    }

    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", lugar='" + lugar + '\'' +
                ", precio=" + precio +
                ", cliente=" + cliente +
                ", equipos=" + productos +
                ", trabajadores=" + trabajadores +
                '}';
    }
}


package proyecto.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    int id;
    String nombre;
    String descripcion;
    double precio;
    double precioDeAlquiler;
    int cantidad;

    public Producto(int id, String nombre, String descripcion, double precio, double precioDeAlquiler, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioDeAlquiler = precioDeAlquiler;
        this.cantidad = cantidad;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioDeAlquiler() {
        return precioDeAlquiler;
    }

    public void setPrecioDeAlquiler(double precioDeAlquiler) {
        this.precioDeAlquiler = precioDeAlquiler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;

        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
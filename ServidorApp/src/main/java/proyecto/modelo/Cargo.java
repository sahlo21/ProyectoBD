package proyecto.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Cargo implements Serializable {
    private static final long serialVersionUID = 1L;
    int idCargo;
    String name;
    float precio_evento;

    public Cargo(int idCargo, String name, float precio_evento) {
        this.idCargo = idCargo;
        this.name = name;
        this.precio_evento = precio_evento;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrecio_evento() {
        return precio_evento;
    }

    public void setPrecio_evento(float precio_evento) {
        this.precio_evento = precio_evento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        return idCargo == cargo.idCargo && 
               Objects.equals(name, cargo.name) && 
               Float.compare(cargo.precio_evento, precio_evento) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCargo, name, precio_evento);
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "idCargo=" + idCargo +
                ", name='" + name + '\'' +
                ", precio_evento=" + precio_evento +
                '}';
    }


}

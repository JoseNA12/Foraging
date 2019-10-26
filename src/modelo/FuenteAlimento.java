package modelo;

public class FuenteAlimento {
    private int cantidad;
    private int tiempo_disponible;
    private int tiempo_regenerar;


    public FuenteAlimento(int cantidad, int tiempo_disponible, int tiempo_regenerar) {
        this.cantidad = cantidad;
        this.tiempo_disponible = tiempo_disponible;
        this.tiempo_regenerar = tiempo_regenerar;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTiempo_disponible() {
        return tiempo_disponible;
    }

    public void setTiempo_disponible(int tiempo_disponible) {
        this.tiempo_disponible = tiempo_disponible;
    }

    public int getTiempo_regenerar() {
        return tiempo_regenerar;
    }

    public void setTiempo_regenerar(int tiempo_regenerar) {
        this.tiempo_regenerar = tiempo_regenerar;
    }
}

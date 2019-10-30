package modelo;

public class Agente {

    private String ID;
    private int cantidad_alimento_recoger;
    private boolean tieneVida;
    private int vida;

    private Posicion posicion;


    public Agente(Posicion posicion, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        this.ID = ID;
        this.cantidad_alimento_recoger = cantidad_alimento_recoger;
        this.tieneVida = tieneVida;
        this.vida = vida;
        this.posicion = posicion;
    }

    public Posicion getPosicionNido() {
        return posicion;
    }

    public void setPosicionNido(Posicion posicion) {
        this.posicion = posicion;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCantidad_alimento_recoger() {
        return cantidad_alimento_recoger;
    }

    public void setCantidad_alimento_recoger(int cantidad_alimento_recoger) {
        this.cantidad_alimento_recoger = cantidad_alimento_recoger;
    }

    public boolean isTieneVida() {
        return tieneVida;
    }

    public void setTieneVida(boolean tieneVida) {
        this.tieneVida = tieneVida;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
}

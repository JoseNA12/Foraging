package modelo;

public class Agente {

    private String ID;
    private int cantidad_alimento_recoger;
    private boolean tieneVida;
    private int vida;

    private boolean buscandoComida = true;

    private int cantidad_alimento_encontrado = 0;

    private Posicion posicionNido;
    private Posicion posicionActual;


    public Agente(Posicion posicionNido, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        this.ID = ID;
        this.cantidad_alimento_recoger = cantidad_alimento_recoger;
        this.tieneVida = tieneVida;
        this.vida = vida;
        this.posicionNido = posicionNido;
        this.posicionActual = posicionNido;
    }

    public Posicion getPosicionNido() {
        return posicionNido;
    }

    public void setPosicionNido(Posicion posicionNido) {
        this.posicionNido = posicionNido;
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

    public void restarVida(int i) {
        this.vida -= i;
    }

    public Posicion getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(Posicion posicionActual) {
        this.posicionActual = posicionActual;
    }

    public int getCantidad_alimento_encontrado() {
        return cantidad_alimento_encontrado;
    }

    public void setCantidad_alimento_encontrado(int cantidad_alimento_encontrado) {
        this.cantidad_alimento_encontrado = cantidad_alimento_encontrado;
    }

    public boolean isBuscandoComida() {
        return buscandoComida;
    }

    public void setBuscandoComida(boolean buscandoComida) {
        this.buscandoComida = buscandoComida;
    }
}

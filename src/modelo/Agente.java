package modelo;

public class Agente {

    private String ID;
    private int cantidad_alimento_recoger;
    private boolean tieneVida;
    private int vida;

    private boolean buscandoComida = true;

    private int cantidad_alimento_encontrado = 0;

    // Bitacora
    private int BITACORA_distancia_total_recorrida = 0;
    private int BITACORA_cantidad_alimento_transportado = 0;
    private long BITACORA_tiempo_de_busqueda = 0;
    // -------------

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

    // Bitacora
    public void addBITACORA_distancia_total_recorrida(int i) {
        this.BITACORA_distancia_total_recorrida += i;
    }

    public void addBITACORA_cantidad_alimento_transportado(int i) {
        this.BITACORA_cantidad_alimento_transportado += i;
    }

    public void addBITACORA_tiempo_de_busqueda(long i) {
        this.BITACORA_tiempo_de_busqueda = i - this.BITACORA_tiempo_de_busqueda;
    }

    public int getBITACORA_distancia_total_recorrida() {
        return BITACORA_distancia_total_recorrida;
    }

    public int getBITACORA_cantidad_alimento_transportado() {
        return BITACORA_cantidad_alimento_transportado;
    }

    public long getBITACORA_tiempo_de_busqueda() {
        return BITACORA_tiempo_de_busqueda;
    }

    // -------------

}
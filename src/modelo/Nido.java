package modelo;

import modelo.otros.Celda;

import java.util.ArrayList;


public class Nido extends Celda {

    private String ID;
    private int capacidad_maxima_alimento;
    private int capacidad_minima_alimento;
    private int duracion_alimento;
    private ArrayList<Agente> agentes;

    private int alimentoRecolectado;

    // parametros de agentes
    private int cantidad_agentes;
    private boolean tienenVida = false;
    private int cantidadAlimentoRecoger;
    private int cantidadVida;
    private boolean reproduccionAgentes;


    public Nido(Celda pCelda, int ID, int capacidad_maxima_alimento, int capacidad_minima_alimento,
                int duracion_alimento, int cantidad_agentes,
                boolean pTienenVida, int pCantidaAlimentoRecoger, int pCantidadVida, boolean pReproduccionAgentes) {

        super(pCelda.getFila(), pCelda.getColumna(), pCelda.getTipo_objeto());

        this.ID = ID + "_Nido_";
        this.capacidad_maxima_alimento = capacidad_maxima_alimento;
        this.capacidad_minima_alimento = capacidad_minima_alimento;
        this.duracion_alimento = duracion_alimento * 1000;
        this.cantidad_agentes = cantidad_agentes;
        this.agentes = new ArrayList<>();

        this.alimentoRecolectado = 0;

        this.tienenVida = pTienenVida;
        this.cantidadAlimentoRecoger = pCantidaAlimentoRecoger;
        this.cantidadVida = pCantidadVida;
        this.reproduccionAgentes = pReproduccionAgentes;
    }

    public void addAgente(Agente pAgente) {
        this.agentes.add(pAgente);
    }

    public void removeAgente(Agente pAgente) {
        this.agentes.remove(pAgente);
    }

    public int getCantidadAgentes() {
        return this.agentes.size();
    }

    public ArrayList<Agente> getAgentes() {
        return agentes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCapacidad_maxima_alimento() {
        return capacidad_maxima_alimento;
    }

    public void setCapacidad_maxima_alimento(int capacidad_maxima_alimento) {
        this.capacidad_maxima_alimento = capacidad_maxima_alimento;
    }

    public int getCapacidad_minima_alimento() {
        return capacidad_minima_alimento;
    }

    public void setCapacidad_minima_alimento(int capacidad_minima_alimento) {
        this.capacidad_minima_alimento = capacidad_minima_alimento;
    }

    public int getAlimentoRecolectado() {
        return alimentoRecolectado;
    }

    public void setAlimentoRecolectado(int alimentoRecolectado) {
        this.alimentoRecolectado = alimentoRecolectado;
    }

    public void consumirAlimentoRecolectado() {
        this.alimentoRecolectado -= 1;
    }

    public int getDuracion_alimento() {
        return duracion_alimento;
    }

    public void setDuracion_alimento(int duracion_alimento) {
        this.duracion_alimento = duracion_alimento;
    }

    public int getCantidad_agentes() {
        return cantidad_agentes;
    }

    public void setCantidad_agentes(int cantidad_agentes) {
        this.cantidad_agentes = cantidad_agentes;
    }

    public boolean isTienenVida() {
        return tienenVida;
    }

    public void setTienenVida(boolean tienenVida) {
        this.tienenVida = tienenVida;
    }

    public int getCantidadAlimentoRecoger() {
        return cantidadAlimentoRecoger;
    }

    public void setCantidadAlimentoRecoger(int cantidadAlimentoRecoger) {
        this.cantidadAlimentoRecoger = cantidadAlimentoRecoger;
    }

    public int getCantidadVida() {
        return cantidadVida;
    }

    public void setCantidadVida(int cantidadVida) {
        this.cantidadVida = cantidadVida;
    }

    public boolean isReproduccionAgentes() {
        return reproduccionAgentes;
    }

    public void setReproduccionAgentes(boolean reproduccionAgentes) {
        this.reproduccionAgentes = reproduccionAgentes;
    }
}

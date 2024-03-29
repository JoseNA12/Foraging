package modelo;

import controlador.C_Inicio;
import controlador.TxtWriter;
import modelo.otros.Celda;

import java.util.ArrayList;
import java.util.Random;


public class Nido extends Celda {

    private String ID;
    private int capacidad_maxima_alimento;
    private int capacidad_minima_alimento;
    private int duracion_alimento;
    private ArrayList<Agente> agentes;
    private ArrayList<Agente> agentesDormidos;

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
        this.agentesDormidos = new ArrayList<>();

        this.alimentoRecolectado = 0;

        this.tienenVida = pTienenVida;
        this.cantidadAlimentoRecoger = pCantidaAlimentoRecoger;
        this.cantidadVida = pCantidadVida;
        this.reproduccionAgentes = pReproduccionAgentes;
    }

    public void addAgente(Agente pAgente) {
        this.agentes.add(pAgente);
    }

    public void eliminarAgenteNido(Agente agente) {
        this.agentes.remove(agente);
        C_Inicio.matriz.limpirCelda(agente.getPosicionActual());
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

    public synchronized void depositarAlimentoRecolectado(int alimentoRecolectado) {
        this.alimentoRecolectado += alimentoRecolectado;
    }

    public void consumirAlimentoRecolectado() {
        if (this.alimentoRecolectado != 0) {
            this.alimentoRecolectado -= 1;
        }
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

    public ArrayList<Agente> getAgentesDormidos() {
        return agentesDormidos;
    }

    public void setAgentesDormidos(ArrayList<Agente> agentesDormidos) {
        this.agentesDormidos = agentesDormidos;
    }

    public void dormirAgente(Agente agente) {
        this.agentesDormidos.add(agente);
        this.agentes.remove(agente);
        C_Inicio.matriz.limpirCelda(agente.getPosicionActual());
    }

    public void despertarAgente() {
        for (int i = 0; i < agentesDormidos.size(); i++) {
            this.agentes.add(agentesDormidos.get(i));
            this.agentesDormidos.remove(agentesDormidos.get(i));
        }
    }

    public synchronized void escribirEnBitacora() {
        String contenido = getID() + "\n";
        ArrayList<Agente> p = new ArrayList<>();
        p.addAll(getAgentes()); p.addAll(getAgentesDormidos());

        long tiempo_buscando_fin = System.currentTimeMillis();

        for (int i = 0; i < p.size(); i++) {
            contenido += " - ID: " + p.get(i).getID() + "\n" +
                 "      Tiempo de busqueda: " + ((tiempo_buscando_fin - p.get(i).getBITACORA_tiempo_de_busqueda()) / 1000F) + " segundo(s)\n" +
                 "      Distancia recorrida: " + p.get(i).getBITACORA_distancia_total_recorrida() + "\n" +
                 "      Alimento transportado: " + p.get(i).getBITACORA_cantidad_alimento_transportado() + "\n";
        }
        TxtWriter.registrarBitacora(contenido);
    }
}

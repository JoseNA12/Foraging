package modelo;

import java.util.ArrayList;

public class Nido {
    private String ID;
    private int capacidad_maxima_alimento;
    private int capacidad_minima_alimento;
    private int duracion_alimento;
    private ArrayList<Agente> agentes;

    private int cantidad_agentes;


    public Nido(int ID, int capacidad_maxima_alimento, int capacidad_minima_alimento, int duracion_alimento, int cantidad_agentes) {
        this.ID = ID + "Nido";
        this.capacidad_maxima_alimento = capacidad_maxima_alimento;
        this.capacidad_minima_alimento = capacidad_minima_alimento;
        this.duracion_alimento = duracion_alimento;
        this.cantidad_agentes = cantidad_agentes;
        this.agentes = new ArrayList<>();
    }

    public void crearEnjambre(boolean pTienenVida, int pCantidaAlimentoRecoger, int... x) {
        for (int i = 0; i < cantidad_agentes; i++) {
            String id = this.ID + "_Agente_" + i;

            if (pTienenVida) {
                this.agentes.add(new Agente(id, pCantidaAlimentoRecoger, true, x[0]));
            }
            else {
                this.agentes.add(new Agente(id, pCantidaAlimentoRecoger));
            }
        }
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
}

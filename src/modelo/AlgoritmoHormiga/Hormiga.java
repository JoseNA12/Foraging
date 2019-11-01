package modelo.AlgoritmoHormiga;

import controlador.optimizacion.BreadthFirstSearch;
import javafx.geometry.Pos;
import modelo.Agente;
import modelo.Posicion;

import java.lang.reflect.Array;
import java.util.*;

public class Hormiga extends Agente {

    private boolean buscandoComida = true;
    private ArrayList<Posicion> caminoACasa;
    private Posicion cobertura; // se utiliza para realizar un mapeo de la ruta del mapa al momento de regresar al nido
    private double feromonasPercibidas = 0;

    private Set<String> pasosRealizados; // se utiliza para no repetir pasos, pero se prioriza la celda con feromonas


    public Hormiga(Posicion posicion, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        super(posicion, ID, cantidad_alimento_recoger, tieneVida, vida);
        this.caminoACasa = new ArrayList<>();
        this.cobertura = super.getPosicionActual();
        this.pasosRealizados = new HashSet<>();
    }

    public void recordarPosicion(Posicion posicion) {
        this.caminoACasa.add(posicion);
        recordarCelda(posicion);
        this.feromonasPercibidas += 1;

        if (this.cobertura.getFila() < posicion.getFila()) {
            this.cobertura.setFila(posicion.getFila());
        }
        if (this.cobertura.getColumna() < posicion.getColumna()) {
            this.cobertura.setColumna(posicion.getColumna());
        }
    }

    /**
     * Evitar repeticiones de celdas al momento de buscar comida
     * @param posicion
     */
    public void recordarCelda(Posicion posicion) {
        if (!recorriCelda(posicion)) {
            this.pasosRealizados.add(posicion.getFila() + "," + posicion.getColumna());
        }
    }

    public boolean recorriCelda(Posicion posicion) {
        return pasosRealizados.contains(posicion.getFila() + "," + posicion.getColumna());
    }

    public boolean isBuscandoComida() {
        return buscandoComida;
    }

    /**
     * Cambia el estado de la hormiga. True -> busca comida, False -> Ir al nido.
     * En caso de que el estado indique ir al nido, por medio de un algoritmo de optimización
     * .. se obtiene la ruta mas corta hacia el nido
     * @param buscandoComida
     */
    public void setBuscandoComida(boolean buscandoComida) {
        this.buscandoComida = buscandoComida;

        if (this.buscandoComida) {
            this.cobertura = super.getPosicionNido();
            this.caminoACasa = new ArrayList<>();
            this.feromonasPercibidas = 0;
            this.pasosRealizados = new HashSet<>();
        }
        else {
            // algoritmo de optimización, retorna la mejor ruta para el nido de la hormiga
            BreadthFirstSearch e = new BreadthFirstSearch(cobertura.getFila(), cobertura.getColumna());
            ArrayList<Posicion> a = e.init(this.caminoACasa);
            if (a != null) {
                this.caminoACasa = a;
            }
            else {
                System.out.println(super.getID() + ": Me perdí, no se donde esta mi nido :(");
            }
        }
    }

    public ArrayList<Posicion> getCaminoACasa() {
        return this.caminoACasa;
    }

    public void setCaminoACasa(ArrayList<Posicion> caminoACasa) {
        this.caminoACasa = caminoACasa;
    }

    public void removeCeldaCaminoCasa(int indice) {
        this.caminoACasa.remove(indice);
    }

    public Posicion getCobertura() {
        return cobertura;
    }

    public void setCobertura(Posicion cobertura) {
        this.cobertura = cobertura;
    }

    public double getFeromonasPercibidas() {
        return feromonasPercibidas;
    }

    public void addFeromonasPercibidas(double feromonasPercibidas) {
        this.feromonasPercibidas += feromonasPercibidas;
    }

    public void setFeromonasPercibidas(double feromonasPercibidas) {
        this.feromonasPercibidas = feromonasPercibidas;
    }

    public String toString() {
        return "(" + getPosicionActual().getFila() + ", " + getPosicionActual().getColumna() + ")";
    }
}

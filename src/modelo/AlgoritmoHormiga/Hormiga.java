package modelo.AlgoritmoHormiga;

import controlador.optimizacion.BreadthFirstSearch;
import modelo.Agente;
import modelo.Posicion;

import java.util.ArrayList;

public class Hormiga extends Agente {

    private boolean buscandoComida = true;
    private ArrayList<Posicion> caminoACasa;
    private Posicion cobertura;
    private double feromonasPercibidas = 0;


    public Hormiga(Posicion posicion, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        super(posicion, ID, cantidad_alimento_recoger, tieneVida, vida);
        this.caminoACasa = new ArrayList<>();
        this.cobertura = super.getPosicionActual();
    }

    public void recordarPosicion(Posicion posicion) {
        this.caminoACasa.add(posicion);
        this.feromonasPercibidas += 1;

        if (this.cobertura.getFila() < posicion.getFila()) {
            this.cobertura.setFila(posicion.getFila());
        }
        if (this.cobertura.getColumna() < posicion.getColumna()) {
            this.cobertura.setColumna(posicion.getColumna());
        }
    }

    public boolean isBuscandoComida() {
        return buscandoComida;
    }

    public void setBuscandoComida(boolean buscandoComida) {
        this.buscandoComida = buscandoComida;

        if (this.buscandoComida) {
            this.cobertura = super.getPosicionNido();
            this.caminoACasa = new ArrayList<>();
            this.feromonasPercibidas = 0;
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

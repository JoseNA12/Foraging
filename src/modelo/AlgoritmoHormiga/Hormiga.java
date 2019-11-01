package modelo.AlgoritmoHormiga;

import controlador.optimizacion.BreadthFirstSearch;
import modelo.Agente;
import modelo.Posicion;

import java.util.ArrayList;

public class Hormiga extends Agente {

    private boolean buscandoComida = true;
    private ArrayList<Posicion> caminoACasa;
    private Posicion cobertura;
    private int totalCeldasRecorridas = 0;


    public Hormiga(Posicion posicion, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        super(posicion, ID, cantidad_alimento_recoger, tieneVida, vida);
        this.caminoACasa = new ArrayList<>();
        this.cobertura = super.getPosicionActual();
    }

    public void recordarPosicion(Posicion posicion) {
        this.caminoACasa.add(posicion);

        if (this.cobertura.getFila() < posicion.getFila()) {
            this.cobertura.setFila(posicion.getFila());
        }
        if (this.cobertura.getColumna() < posicion.getColumna()) {
            this.cobertura.setColumna(posicion.getColumna());
        }
    }

    public void clearCaminoACasa() {
        this.caminoACasa.clear();
    }

    public boolean isBuscandoComida() {
        return buscandoComida;
    }

    public void setBuscandoComida(boolean buscadoComida) {
        this.buscandoComida = buscadoComida;

        if (buscadoComida) {
            this.cobertura = super.getPosicionNido();
            this.caminoACasa = new ArrayList<>();
        }
        else {
            BreadthFirstSearch e = new BreadthFirstSearch(cobertura.getFila(), cobertura.getColumna());
            /*System.out.println("JUEP/RA");
            for (Posicion i: this.caminoACasa) {
                System.out.println(i);
            }*/
            ArrayList<Posicion> a = e.init(this.caminoACasa);
            if (a != null) {
                this.caminoACasa = a;
                this.totalCeldasRecorridas = 0;
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

    public int getTotalCeldasRecorridas() {
        return totalCeldasRecorridas;
    }

    public void setTotalCeldasRecorridas(int totalCeldasRecorridas) {
        this.totalCeldasRecorridas = totalCeldasRecorridas;
    }

    public String toString() {
        return "(" + getPosicionActual().getFila() + ", " + getPosicionActual().getColumna() + ")";
    }
}

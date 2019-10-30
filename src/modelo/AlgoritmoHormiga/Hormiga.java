package modelo.AlgoritmoHormiga;

import modelo.Agente;
import modelo.Posicion;

import java.util.ArrayList;

public class Hormiga extends Agente {

    private boolean buscandoComida = true;
    private ArrayList<Posicion> caminoACasa;


    public Hormiga(Posicion posicion, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        super(posicion, ID, cantidad_alimento_recoger, tieneVida, vida);
        this.caminoACasa = new ArrayList<>();
    }

    public void recordarPosicion(Posicion posicion) {
        this.caminoACasa.add(posicion);
    }

    public void clearCaminoACasa() {
        this.caminoACasa.clear();
    }

    public boolean isBuscandoComida() {
        return buscandoComida;
    }

    public void setBuscandoComida(boolean buscadoComida) {
        this.buscandoComida = buscadoComida;
    }
}

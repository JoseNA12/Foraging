package modelo;

import controlador.optimizacion.BreadthFirstSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Agente {

    private String ID;
    private int cantidad_alimento_recoger;
    private boolean tieneVida;
    private int vida;
    private Posicion posicionActual;
    private Posicion posicionNido;
    private int cantidad_alimento_encontrado = 0;
    private boolean buscandoComida = true;
    private ArrayList<Posicion> caminoACasa;
    private Set<String> pasosRealizados;
    private Posicion cobertura; // se utiliza para realizar un mapeo de la ruta del mapa al momento de regresar al nido

    public Agente(Posicion posicionNido, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        this.ID = ID;
        this.cantidad_alimento_recoger = cantidad_alimento_recoger;
        this.tieneVida = tieneVida;
        this.vida = vida;
        this.posicionNido = posicionNido;
        this.posicionActual = posicionNido;
        this.cobertura = posicionNido;
        this.pasosRealizados = new HashSet<>();
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

    public void setPosicionActual(Posicion posicion) {
        this.posicionActual = posicion;
    }

    public int getCantidad_alimento_encontrado() {
        return cantidad_alimento_encontrado;
    }

    public void setCantidad_alimento_encontrado(int cantidad_alimento_encontrado) {
        this.cantidad_alimento_encontrado = cantidad_alimento_encontrado;
    }

    public void recordarPosicion(Posicion posicion) {
        this.caminoACasa.add(posicion);
        recordarCelda(posicion);
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
            this.cobertura = getPosicionNido();
            this.caminoACasa = new ArrayList<>();
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
                System.out.println(getID() + ": Me perdí, no se donde esta mi nido :(");
            }
        }
    }

    public void removeCeldaCaminoCasa(int indice) {
        this.caminoACasa.remove(indice);
    }

    public ArrayList<Posicion> getCaminoACasa() {
        return this.caminoACasa;
    }

    public void setCaminoACasa(ArrayList<Posicion> caminoACasa) {
        this.caminoACasa = caminoACasa;
    }

}

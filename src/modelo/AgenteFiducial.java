package modelo;

import controlador.optimizacion.BreadthFirstSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AgenteFiducial extends Agente{

    private boolean buscandoComida = true;
    private int cantidad_alimento_encontrado = 0;

    // Bitacora
    private int BITACORA_distancia_total_recorrida = 0;
    private int BITACORA_cantidad_alimento_transportado = 0;
    private long BITACORA_tiempo_de_busqueda = 0;
    // -------------

    private ArrayList<Posicion> caminoACasa;
    private Set<String> pasosRealizados;
    private Posicion cobertura; // se utiliza para realizar un mapeo de la ruta del mapa al momento de regresar al nido

    public AgenteFiducial(Posicion posicionNido, String ID, int cantidad_alimento_recoger, boolean tieneVida, int vida) {
        super(posicionNido, ID, cantidad_alimento_recoger, tieneVida, vida);
        this.cobertura = posicionNido;
        this.pasosRealizados = new HashSet<>();
        this.caminoACasa = new ArrayList<>();
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

    public boolean isBuscandoComida() {
        return buscandoComida;
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

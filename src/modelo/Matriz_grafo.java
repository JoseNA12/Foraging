package modelo;

import modelo.otros.Celda;
import modelo.otros.Objeto_IU;

import java.util.ArrayList;


/**
 * Clase que contiene la matriz/grafo de la solución de juego.
 *
 * El modelo Celda tiene:
 *      - fila              (int)
 *      - columna           (int)
 *      - tipo_objeto       (Indica si es Nido, Obstaculo, FuenteAlimento o Celda libre)
 */
public class Matriz_grafo {

    private ArrayList<ArrayList<Celda>> matriz;


    public Matriz_grafo() {
        this.matriz = new ArrayList<ArrayList<Celda>>();
    }

    public void add(ArrayList<Celda> pLista) {
        matriz.add(pLista);
    }

    public Celda get(int fila, int columna) {
        return this.matriz.get(fila).get(columna);
    }

    public void set(int fila, int columna, Celda pCelda) {
        matriz.get(fila).set(columna, pCelda);
    }

    public boolean isNido(int fila, int columna) {
        if (this.get(fila, columna).getTipo_objeto() == Objeto_IU.NIDO) {
            return true;
        }
        return false;
    }

    public boolean isFuenteAlimento(int fila, int columna) {
        if (this.get(fila, columna).getTipo_objeto() == Objeto_IU.ALIMENTO) {
            return true;
        }
        return false;
    }

    public boolean isObstaculo(int fila, int columna) {
        if (this.get(fila, columna).getTipo_objeto() == Objeto_IU.OBSTACULO) {
            return true;
        }
        return false;
    }

    public synchronized boolean setAgenteCasilla(int fila, int columna) {
        if (get(fila, columna).getTipo_objeto() != Objeto_IU.AGENTE) {
            get(fila, columna).setTipo_objeto(Objeto_IU.AGENTE);
            return true;
        }
        return false;
    }

    public synchronized boolean setVacioCasilla(int fila, int columna) {
        if (get(fila, columna).getTipo_objeto() != Objeto_IU.VACIO) {
            get(fila, columna).setTipo_objeto(Objeto_IU.VACIO);
            return true;
        }
        return false;
    }
}

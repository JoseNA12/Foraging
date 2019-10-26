package modelo;

import modelo.otros.Celda;
import java.util.ArrayList;


/**
 * Clase que contiene la matriz/grafo de la solución de juego.
 *
 * El modelo Celda tiene:
 *      - fila              (int)
 *      - columna           (int)
 *      - tipo_objeto       (Enum que indica si es Nido, Obstaculo, FuenteAlimento o Celda libre)
 *      - objeto_en_juego   (Objeto modelo: puede ser Nido, Obstaculo o FuenteAlimento)
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

}

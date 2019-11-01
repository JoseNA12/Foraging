package modelo;

import javafx.scene.image.Image;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;

import java.util.ArrayList;

import static controlador.C_Inicio.mi_canvas;


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

    public boolean isFuenteAlimento(Posicion p) {
        if (this.get(p.getFila(), p.getColumna()).getTipo_objeto() == Objeto_IU.ALIMENTO) {
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

    // p1: nueva posicion   |   p2: posicion anterior
    public synchronized boolean setAgenteCasilla(Image img, Posicion p1, Posicion p2) {
        if (get(p1.getFila(), p1.getColumna()).getTipo_objeto() == Objeto_IU.VACIO) {
            get(p1.getFila(), p1.getColumna()).setTipo_objeto(Objeto_IU.AGENTE);

            mi_canvas.dibujar_canvas(img, p1.getFila(), p1.getColumna());

            if (!isNido(p2.getFila(), p2.getColumna())) { // evitar conflictos al inicio del spam
                get(p2.getFila(), p2.getColumna()).setTipo_objeto(Objeto_IU.VACIO);
                mi_canvas.dibujar_canvas(null, p2.getFila(), p2.getColumna());
            }
            return true;
        }
        return false;
    }
}

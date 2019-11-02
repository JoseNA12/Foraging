package controlador.optimizacion;

import modelo.Posicion;
import sun.security.util.ArrayUtil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Fuentes:
 * https://www.techiedelight.com/lee-algorithm-shortest-path-in-a-maze/
 * https://techiedelight.com/compiler/?~lee_algo_print_shortest_path_java
 */

// nodo de cola utilizado en BFS
class Node {
    // (x, y) son las coordenadas de la celda de la matriz
    // dist es la distancia mínima de la fuente
    int x, y, dist;
    Node padre; // mantener un nodo principal para la ruta de impresión

    Node(int x, int y, int dist, Node padre) {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.padre = padre;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
};

public class BreadthFirstSearch {

    private int fila;
    private int columna;

    // 4 movimientos posibles desde una celda
    private final int fila_movs[] = { -1, 0, 0, 1, 1, 1, -1, -1 };
    private final int col_movs[] = { 0, -1, 1, 0, 1, -1, 1, -1 };
    private ArrayList<Posicion> solucion;


    public BreadthFirstSearch(int fila, int columna) {
        this.fila = fila + 1;
        this.columna = columna + 1;
        this.solucion = new ArrayList<Posicion>();
    }

    // Función para verificar si es posible ir a la posición (pFila, pColumna) desde la posición actual.
    // La función devuelve falso si (pFila, pColumna) no es una posición válida o tiene valor 0 o ya está visitado
    private boolean esValido(int mat[][], boolean visitado[][], int pFila, int pColumna) {
        return (pFila >= 0) && (pFila < fila) && (pColumna >= 0) && (pColumna < columna)
                && mat[pFila][pColumna] == 1 && !visitado[pFila][pColumna];
    }

    // Encuentre la ruta más corta posible en la matriz mat desde la celda de origen (i, j)
    // .. a la celda de destino (x, y)
    private boolean BFS(int mat[][], int i, int j, int x, int y) {
        boolean[][] visitado = new boolean[fila][columna]; // matriz para realizar un seguimiento de las celdas visitadas
        Queue<Node> q = new ArrayDeque<>(); // crear una cola vacia

        visitado[i][j] = true; // marcar la celda fuente como visitada y poner en cola el nodo fuente
        q.add(new Node(i, j, 0, null));

        // almacena la longitud de la ruta más larga desde el origen hasta el destino
        int min_dist = Integer.MAX_VALUE;
        Node nodo = null;

        while (!q.isEmpty()) { // ejecutar hasta que la cola no esté vacía
            nodo = q.poll(); // pop del nodo de la cola y procesarlo
            i = nodo.x; j = nodo.y; // (i, j) representa la celda actual y
            int dist = nodo.dist; // dist almacena su distancia mínima desde la fuente

            if (i == x && j == y) { // si se encuentra el destino, actualice min_dist y detengase
                min_dist = dist;
                break;
            }
            // verifique los 4 movimientos posibles desde la celda actual y ponga en cola cada movimiento válido
            for (int k = 0; k < fila_movs.length; k++) {
                // es posible ir a la posición (i + fila_movs [k], j + col_movs [k]) desde la posición actual ?
                if (esValido(mat, visitado, i + fila_movs[k], j + col_movs[k])) {
                    // marca la siguiente celda como visitada y la pone en cola
                    visitado[i + fila_movs[k]][j + col_movs[k]] = true;
                    q.add(new Node(i + fila_movs[k], j + col_movs[k], dist + 1, nodo));
                }
            }
        }

        if (min_dist != Integer.MAX_VALUE) {
            // System.out.println(min_dist);
            //printCamino(nodo);
            getCamino(nodo); // construyo el array de solución
            //System.out.println();
            //printCamino(nodo);
            //System.out.println();
            return true;
        }
        else { // no hay ruta optima
            return false;
        }
    }

    private void printCamino(Node node) {
        if (node == null) {
            return;
        }
        printCamino(node.padre);
        System.out.print(node);
    }

    private void getCamino(Node node) {
        if (node == null) {
            return;
        }
        getCamino(node.padre);
        solucion.add(0, new Posicion(node.x, node.y));
    }

    public ArrayList<Posicion> init(ArrayList<Posicion> pCamino) {
        Posicion pos_inicio = pCamino.get(0);
        Posicion pos_final = pCamino.get(pCamino.size() - 1);
        int[][] matriz = new int[fila][columna];

        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                matriz[i][j] = 0;
            }
        }
        for (Posicion p: pCamino) {
            matriz[p.getFila()][p.getColumna()] = 1;
        }
        boolean bfs = BFS(matriz, pos_inicio.getFila(), pos_inicio.getColumna(), pos_final.getFila(), pos_final.getColumna());
        if (bfs) {
            return new ArrayList<Posicion>(solucion.subList(1, solucion.size()));
        }
        return null;
    }
}

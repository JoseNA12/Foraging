package modelo.AlgoritmoHormiga;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import modelo.Agente;
import modelo.Nido;
import modelo.Posicion;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;

import java.util.ArrayList;

import static controlador.C_Inicio.*;


public class NidoHormigas extends Nido {

    private double feromonaHormiga = 0.3;
    private double evaporacion = 0.05;

    private double matriz[][];


    public NidoHormigas(Celda pCelda, int ID, int capacidad_maxima_alimento, int capacidad_minima_alimento, int duracion_alimento, int cantidad_agentes, boolean pTienenVida, int pCantidaAlimentoRecoger, int pCantidadVida, boolean pReproduccionAgentes) {
        super(pCelda, ID, capacidad_maxima_alimento, capacidad_minima_alimento, duracion_alimento, cantidad_agentes, pTienenVida, pCantidaAlimentoRecoger, pCantidadVida, pReproduccionAgentes);

        matriz = generarMatriz();
        crearEnjambre();
        iniciar();
    }

    public void crearEnjambre() {
        for (int i = 0; i < getCantidad_agentes(); i++) {
            String id = getID() + "_Hormiga_Agente_" + i;
            addAgente(crearHormiga(id));
        }
    }

    private Hormiga crearHormiga(String id) {
        return new Hormiga(getPosicion(), id, getCantidadAlimentoRecoger(), isTienenVida(), getCantidadVida());
    }

    private double[][] generarMatriz() {
        double[][] randomMatrix = new double[cant_filas][cant_columnas];

        for(int i = 0; i < cant_filas; i++) {
            for(int j = 0; j < cant_columnas; j++) {
                randomMatrix[i][j] = 0.0;
            }
        }
        return randomMatrix;
    }

    private void iniciar() {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {

                long inicio = System.currentTimeMillis();

                while (juego_activo) {
                    for (Agente h: getAgentes()) {
                        Hormiga h_ = (Hormiga) h;

                        if (h_.isBuscandoComida()) {
                            moverHormiga(h_);
                        }

                        if (isTienenVida()) {
                            h.restarVida(1);
                        }
                    }

                    reproducirHormigas();

                    long diferencia = System.currentTimeMillis()-inicio;
                    if (consumirAlimentoNido(diferencia)) {
                        inicio = System.currentTimeMillis();
                    }

                    Thread.sleep(lapsos_tiempo_ejecucion);


                }
                // los agentes pueden morir ? -> la vida (check)
                // capacidad maxima y minima de alimento en nido (check)
                // duración de alimento en nido (check)
                // cantidad de alimento a recoger (check)

                return null;
            }
        };

        new Thread(task).start();
    }
    private void moverHormiga(Hormiga h) {
        celdasVecinasDisponibles(h.getPosicionNido().getFila(), h.getPosicionNido().getColumna());

        /**
         * 1. Escoger que camino tomar validando si hay feromonas
         * 2. Setear la casilla en Matriz_grafo, validar lo que retorna para asegurar la concurrencia
         * 3. Si el retorno es true, pintar en el canvas el agente
         */
    }

    private ArrayList<Posicion> celdasVecinasDisponibles(int i, int j) {
        int rowLimit = cant_filas - 1;
        int columnLimit = cant_columnas - 1;
        ArrayList<Posicion> celdasDisponibles = new ArrayList<>();

        for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, rowLimit); x++) {
            for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, columnLimit); y++) {
                if (x != i || y != j) {
                    if (C_Inicio.matriz.get(x, y).getTipo_objeto() == Objeto_IU.VACIO) {
                        celdasDisponibles.add(new Posicion(x, y));
                    }
                }
            }
        }
        return celdasDisponibles;
    }

    private void reproducirHormigas() {
        // si la cantidad de alimentos recolectados supera el máximo del nido
        // .. agregue una nueva hormiga
        if (getAlimentoRecolectado() > getCapacidad_maxima_alimento()) {
            addAgente(crearHormiga("bastarda"));
            consumirAlimentoRecolectado();
        }
    }

    private boolean consumirAlimentoNido(long tiempo) {
        if ((tiempo >= getDuracion_alimento()) && getAlimentoRecolectado() > 0) {
            consumirAlimentoRecolectado(); // le resta 1 a la cantidad de alimentos en nido
            return true;
        }
        return false;
    }
}

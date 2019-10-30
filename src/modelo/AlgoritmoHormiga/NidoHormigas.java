package modelo.AlgoritmoHormiga;

import javafx.concurrent.Task;
import modelo.Nido;
import modelo.Posicion;
import modelo.otros.Celda;

import static controlador.C_Inicio.*;


public class NidoHormigas extends Nido {

    private double feromonaHormiga = 0.3;
    private double evaporacion = 0.05;

    private double matriz[][];


    public NidoHormigas(Celda pCelda, int ID, int capacidad_maxima_alimento, int capacidad_minima_alimento, int duracion_alimento, int cantidad_agentes, boolean pTienenVida, int pCantidaAlimentoRecoger, int pCantidadVida, boolean pReproduccionAgentes) {
        super(pCelda, ID, capacidad_maxima_alimento, capacidad_minima_alimento, duracion_alimento, cantidad_agentes, pTienenVida, pCantidaAlimentoRecoger, pCantidadVida, pReproduccionAgentes);

        matriz = generarMatriz();
        crearEnjambre();
    }

    public void crearEnjambre() {
        for (int i = 0; i < getCantidad_agentes(); i++) {
            String id = getID() + "_Hormiga_Agente_" + i;
            Posicion p = new Posicion(getFila(), getColumna());
            addAgente(new Hormiga(p, id, getCantidadAlimentoRecoger(), isTienenVida(), getCantidadVida()));
        }
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

                while (juego_activo) {

                }
                // los agentes pueden morir ? -> la vida
                // capacidad maxima y minima de alimento en nido
                // duración de alimento en nido
                // cantidad de alimento a recoger

                return null;
            }
        };

        new Thread(task).start();
    }
}

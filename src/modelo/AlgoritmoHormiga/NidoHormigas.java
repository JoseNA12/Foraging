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
import java.util.Random;

import static controlador.C_Inicio.*;


public class NidoHormigas extends Nido {

    private double feromonaHormiga = 500;
    private double evaporacion = 0.5;
    private final double visibilidad = 3.1;
    private final double alpha = 1; // importancia de feromona
    private final double beta = 5;  // prioridad de distancia
    private final double feromonaInicial = 0.1;

    private Random random = new Random();
    private double randomFactor = 0.01;

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
                randomMatrix[i][j] = feromonaInicial;
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
                            moverHormiga(h_); Thread.sleep(5000);
                        }
                        else { // regresando al nido
                            actualizarFeromonaCelda(h_.getPosicion().getFila(), h_.getPosicion().getColumna());
                        }

                        if (isTienenVida()) {
                            h.restarVida(1);
                            if (h.getVida() < 0) {
                                removeAgente(h);
                            }
                        }
                    }

                    reproducirHormigas();

                    long diferencia = System.currentTimeMillis()-inicio;
                    if (consumirAlimentoNido(diferencia)) {
                        inicio = System.currentTimeMillis();
                    }

                    Thread.sleep((long) lapsos_tiempo_ejecucion);

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
        ArrayList<Posicion> celdasDisponibles = celdasVecinasDisponibles(h.getPosicion().getFila(), h.getPosicion().getColumna());
        for (Posicion i: celdasDisponibles) {
            System.out.println(i.getFila() + "/" + i.getColumna());
        }
        Posicion p = caminoMasEfectivo(celdasDisponibles, h);

        if (p != null) {
            boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(p.getFila(), p.getColumna());
            boolean pudoBorrarPosAnterior = C_Inicio.matriz.setVacioCasilla(h.getPosicion().getFila(), h.getPosicion().getColumna());

            // al ser un metodo que toodo mundo usa (y es synchronized), prefiero asegurar
            // .. que el agente si se pudo poner en la matriz general de juego
            if (pudoPonerAgente) {
                System.out.println("NUEVA POSICION: " + p.getFila() + "/" + p.getColumna());
                System.out.println("HORMIGA: " + h.getPosicion().getFila() + "/" + h.getPosicion().getColumna());

                if (!C_Inicio.matriz.isNido(h.getPosicion().getFila(), h.getPosicion().getColumna())) {
                    mi_canvas.limpiarCasilla(h.getPosicion().getFila(), h.getPosicion().getColumna());
                }
                mi_canvas.dibujar_canvas(mi_canvas.getImg_agente(), 0, 28);
                h.setPosicion(p);
                h.recordarPosicion(p);
            }
        }
    }

    private Posicion caminoMasEfectivo(ArrayList<Posicion> celdasDisponibles, Hormiga h) {
        /**
         * Selección de arista
         *
         * p^k = (t^α)(n^β) / Σ(t^α)(n^β)
         *
         * ->> t^α es la cantidad de feromonas depositadas
         * ->> n^β visibilidad o conveniencia del estado de transición (1 / peso de la distancia)
         */

        ArrayList<Double> celdasConFeromonas = new ArrayList<>();
        double sumatoria = 0.0;

        for (Posicion i: celdasDisponibles) {
            double t = Math.pow(matriz[i.getFila()][i.getColumna()], alpha);
            double n = Math.pow(1 / visibilidad, beta);
            double tn = t * n;  // (t^α)(n^β)
            celdasConFeromonas.add(tn);
            sumatoria += tn;    // Σ(t^α)(n^β)
        }

        ArrayList<Double> probabilidadCeldas = new ArrayList<>();
        for (Double i: celdasConFeromonas) {
            probabilidadCeldas.add(i / sumatoria); // p^k = (t^α)(n^β) / Σ(t^α)(n^β)
        }

        double r = random.nextDouble();
        double total = 0.0;
        for (int i = 0; i < probabilidadCeldas.size(); i++) {
            total += probabilidadCeldas.get(i);
            if (total >= r)
                System.out.println("RETURN: " + celdasDisponibles.get(i).getFila() + "/" + celdasDisponibles.get(i).getColumna());
                return celdasDisponibles.get(i);
        }
        return null;
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

    private void actualizarFeromonaCelda(int x, int y) {
        matriz[x][y] = (1 - evaporacion) * matriz[x][y];
    }

    private void reproducirHormigas() {
        // si la cantidad de alimentos recolectados supera el máximo del nido
        // .. agregue una nueva hormiga
        if (getAlimentoRecolectado() > getCapacidad_maxima_alimento()) {
            super.addAgente(crearHormiga("bastarda"));
            super.consumirAlimentoRecolectado();
        }
    }

    private boolean consumirAlimentoNido(long tiempo) {
        if ((tiempo >= getDuracion_alimento()) && getAlimentoRecolectado() > 0) {
            super.consumirAlimentoRecolectado(); // le resta 1 a la cantidad de alimentos en nido
            return true;
        }
        return false;
    }
}

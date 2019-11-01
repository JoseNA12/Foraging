package modelo.AlgoritmoHormiga;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import modelo.Agente;
import modelo.FuenteAlimento;
import modelo.Nido;
import modelo.Posicion;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;

import java.util.ArrayList;
import java.util.Random;

import static controlador.C_Inicio.*;


public class NidoHormigas extends Nido {

    private double feromonaHormiga = 500;
    private double feromonaInicial = 0.1;
    private double evaporacion = 0.5; // entre mas bajo este valor, más feronomonas restantes
    private final double visibilidad = 3.1;
    private final double alpha = 1; // importancia de feromona
    private final double beta = 5;  // prioridad de distancia

    private Random random = new Random();

    private double matrizFeromonas[][];


    public NidoHormigas(Celda pCelda, int ID, int capacidad_maxima_alimento, int capacidad_minima_alimento, int duracion_alimento, int cantidad_agentes, boolean pTienenVida, int pCantidaAlimentoRecoger, int pCantidadVida, boolean pReproduccionAgentes) {
        super(pCelda, ID, capacidad_maxima_alimento, capacidad_minima_alimento, duracion_alimento, cantidad_agentes, pTienenVida, pCantidaAlimentoRecoger, pCantidadVida, pReproduccionAgentes);

        matrizFeromonas = generarMatrizFeromonas();
        crearEnjambre();
        iniciar();
    }

    public void crearEnjambre() {
        for (int i = 0; i < getCantidad_agentes(); i++) {
            String id = super.getID() + "_Hormiga_Agente_" + i;
            addAgente(crearHormiga(id));
        }
    }

    private Hormiga crearHormiga(String id) {
        return new Hormiga(super.getPosicion(), id, super.getCantidadAlimentoRecoger(), super.isTienenVida(), super.getCantidadVida());
    }

    private double[][] generarMatrizFeromonas() {
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
                handlerFeromonas(); // se encarga de actualizar las feromonas de las matriz

                while (juego_activo) {
                    for (Agente h: getAgentes()) {
                        Hormiga h_ = (Hormiga) h;

                        if (h_.isBuscandoComida()) {
                            buscarComida(h_);
                        }
                        else {
                            irANido(h_);
                        }

                        if (isTienenVida()) {
                            h.restarVida(1);
                            if (h.getVida() < 0) {
                                removeAgente(h);
                            }
                        }
                    }
                    reproducirHormigas();

                    long diferencia = System.currentTimeMillis() - inicio;
                    if (consumirAlimentoNido(diferencia)) {
                        inicio = System.currentTimeMillis();
                    }

                    Thread.sleep((long) lapsos_tiempo_ejecucion);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void buscarComida(Hormiga h) {
        ArrayList<Posicion> fuentesAlimentoDisponibles = fuentesAlimentoVecinas(h.getPosicionActual());

        if (fuentesAlimentoDisponibles.size() > 0) { // existe una fuente de alimento a su alrededor. Disponible y con cantidad > 0
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(fuentesAlimentoDisponibles.size()); // si hay varias escoja 1 al azar
            Posicion p = fuentesAlimentoDisponibles.get(randomInt);

            FuenteAlimento fa = ((FuenteAlimento) C_Inicio.matriz.get(p.getFila(), p.getColumna())); // obtengo el recurso
            h.setCantidad_alimento_encontrado(fa.consumirAlimento(h.getCantidad_alimento_recoger()));
            h.setBuscandoComida(false);
        }
        else {
            ArrayList<Posicion> celdasDisponibles = celdasVecinasDisponibles(h.getPosicionActual());
            Posicion p = caminoMasEfectivo(celdasDisponibles);

            if (p != null) {
                boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(mi_canvas.getImg_agente(), p, h.getPosicionActual());

                // al ser un metodo que toodo mundo usa (y es synchronized), prefiero asegurar
                // .. que el agente si se pudo poner en la matriz general de juego
                if (pudoPonerAgente) {
                    h.setPosicionActual(p);
                    h.recordarPosicion(p);

                    if (matrizFeromonas[p.getFila()][p.getFila()] > 0) { // Q/C^k (if component(i, j) was used by ant)
                        h.addFeromonasPercibidas(feromonaHormiga / super.getCantidad_agentes());
                    }
                }
            }
        }
    }

    private void irANido(Hormiga h) {
        ArrayList<Posicion> caminoNido = h.getCaminoACasa();

        if (caminoNido.size() > 0) {
            //actualizarFeromonaCelda(h.getPosicionActual());
            boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(mi_canvas.getImg_agente_alimento_1(), caminoNido.get(0), h.getPosicionActual());

            if (pudoPonerAgente) {
                h.setPosicionActual(caminoNido.get(0));
                h.removeCeldaCaminoCasa(0); // remover de caminoNido la ubicacion utilizada
                depositarFeromonas(h, h.getPosicionActual());
            }
        }
        else {
            super.depositarAlimentoRecolectado(h.getCantidad_alimento_encontrado());
            h.setCantidad_alimento_encontrado(0);
            h.setBuscandoComida(true);
            h.recordarPosicion(h.getPosicionActual());
        }
    }

    private Posicion caminoMasEfectivo(ArrayList<Posicion> celdasDisponibles) {
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
            double t = Math.pow(matrizFeromonas[i.getFila()][i.getColumna()], alpha);
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

            if (total >= r) {
                //System.out.println("TOTAL: " + total + " | r: " + r + " | " + i);
                return celdasDisponibles.get(i);
            }
        }
        // si retorna null, el agente esta bloquedo por algo. Pero puede esperar hasta su disponibilidad
        return null;
    }

    private ArrayList<Posicion> celdasVecinasDisponibles(Posicion p) {
        int i = p.getFila();
        int j = p.getColumna();
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

    private ArrayList<Posicion> fuentesAlimentoVecinas(Posicion p) {
        int i = p.getFila();
        int j = p.getColumna();
        int rowLimit = cant_filas - 1;
        int columnLimit = cant_columnas - 1;
        ArrayList<Posicion> celdasDisponibles = new ArrayList<>();

        for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, rowLimit); x++) {
            for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, columnLimit); y++) {
                if (x != i || y != j) {
                    if (C_Inicio.matriz.get(x, y).getTipo_objeto() == Objeto_IU.ALIMENTO &&
                            !((FuenteAlimento) C_Inicio.matriz.get(x, y)).isRegenerando()) {
                        celdasDisponibles.add(new Posicion(x, y));
                    }
                }
            }
        }
        return celdasDisponibles;
    }

    private void depositarFeromonas(Hormiga h, Posicion p) {
        matrizFeromonas[p.getFila()][p.getColumna()] += h.getFeromonasPercibidas() + feromonaHormiga;
    }

    private void handlerFeromonas() {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {

                while (juego_activo) {
                    for (int i = 0; i < matrizFeromonas.length; i++) {
                        for (int j = 0; j < matrizFeromonas[0].length; j++) {
                            actualizarFeromonaCelda(i, j);
                        }
                    }
                    Thread.sleep((long) lapsos_tiempo_ejecucion);
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void actualizarFeromonaCelda(int i,int j) { // t_ij = (1 - p) * t_ij
        matrizFeromonas[i][j] = (1 - evaporacion) * matrizFeromonas[i][j];
    }

    private void reproducirHormigas() {
        // si la cantidad de alimentos recolectados supera el máximo del nido
        // .. agregue una nueva hormiga
        if (super.getAlimentoRecolectado() > super.getCapacidad_maxima_alimento()) {
            super.addAgente(crearHormiga("bastarda"));
            super.consumirAlimentoRecolectado();
        }
    }

    private synchronized boolean consumirAlimentoNido(long tiempo) {
        if ((tiempo >= getDuracion_alimento()) && getAlimentoRecolectado() > 0) {
            super.consumirAlimentoRecolectado(); // le resta 1 a la cantidad de alimentos en nido
            return true;
        }
        return false;
    }


}

package modelo;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import modelo.Agente;
import modelo.FuenteAlimento;
import modelo.Nido;
import modelo.Posicion;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;

import java.util.ArrayList;
import java.util.Random;

import static controlador.C_Inicio.*;

public class NidoFiducial extends Nido{

    public NidoFiducial(Celda pCelda, int ID, int capacidad_maxima_alimento, int capacidad_minima_alimento, int duracion_alimento, int cantidad_agentes, boolean pTienenVida, int pCantidaAlimentoRecoger, int pCantidadVida, boolean pReproduccionAgentes) {
        super(pCelda, ID, capacidad_maxima_alimento, capacidad_minima_alimento, duracion_alimento, cantidad_agentes, pTienenVida, pCantidaAlimentoRecoger, pCantidadVida, pReproduccionAgentes);
        crearEnjambre();
        iniciar();
    }

    public void crearEnjambre() {
        for (int i = 0; i < getCantidad_agentes(); i++) {
            String id = getID() + "_Fiducial Agente_" + i;
            addAgente(crearAgente(id));
        }
    }

    private AgenteFiducial crearAgente(String id) {
        return new AgenteFiducial(getPosicion(), id, getCantidadAlimentoRecoger(), isTienenVida(), getCantidadVida());
    }

    public void iniciar() {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                try {
                    long inicio = System.currentTimeMillis();
                    while (juego_activo) {
                        if (getAlimentoRecolectado() < getCapacidad_minima_alimento()) {
                            despertarAgente();
                        }

                        for (int i = 0; i < getAgentes().size(); i++) {
                            AgenteFiducial agente = (AgenteFiducial) getAgentes().get(i);
                            if (agente.isBuscandoComida()) {
                                long tiempo_buscando_inicio = System.currentTimeMillis(); // bitacora
                                moverAgente(agente);
                                agente.addBITACORA_tiempo_de_busqueda(tiempo_buscando_inicio); // bitacora
                            }
                            else {
                                irANido(agente);
                            }
                            agente.addBITACORA_distancia_total_recorrida(1); // bitacora
                            if (isTienenVida()) {
                                agente.restarVida(1);
                                if (agente.getVida() < 0) {
                                    eliminarAgenteNido(agente);
                                }
                            }
                        }

                        long diferencia = System.currentTimeMillis() - inicio;
                        if (consumirAlimentoNido(diferencia)) {
                            inicio = System.currentTimeMillis();
                        }
                        try {
                            Thread.sleep((long) lapsos_tiempo_ejecucion);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    escribirEnBitacora();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void irANido(AgenteFiducial agente){
        ArrayList<Posicion> caminoNido = agente.getCaminoACasa();
        if (caminoNido.size() > 0) {
            boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(mi_canvas.getImg_agente_alimento_2(), caminoNido.get(0),agente.getPosicionActual());
            if (pudoPonerAgente) {
                agente.setPosicionActual(caminoNido.get(0));
                agente.removeCeldaCaminoCasa(0); // remover de caminoNido la ubicacion utilizada
            }
        }
        else {
            depositarAlimentoRecolectado(agente.getCantidad_alimento_encontrado());
            agente.setCantidad_alimento_encontrado(0);
            agente.setBuscandoComida(true);
            agente.recordarPosicion(agente.getPosicionActual());

            if (getAlimentoRecolectado() > getCapacidad_minima_alimento()) {
                dormirAgente(agente);
            }
        }
    }

    private void moverAgente(AgenteFiducial agente){
        Posicion pos = agente.getPosicionActual();
        Object[] returnArrayList = celdasVecinasDisponibles(pos.getFila(), pos.getColumna());
        ArrayList<Posicion> celdasDisponibles = (ArrayList<Posicion>) returnArrayList[0];
        ArrayList<Posicion> celdasAgentesVecinos = (ArrayList<Posicion>) returnArrayList[1];
        ArrayList<Posicion> celdasAlimento = (ArrayList<Posicion>) returnArrayList[2];
        if(celdasAlimento.size() > 0){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(celdasAlimento.size()); // si hay varias escoja 1 al azar
            Posicion p = celdasAlimento.get(randomInt);
            int fila = p.getFila();
            int col = p.getColumna();
            FuenteAlimento fa = (FuenteAlimento) C_Inicio.matriz.get(fila, col);
            agente.setCantidad_alimento_encontrado(fa.consumirAlimento(agente.getCantidad_alimento_recoger()));
            agente.setBuscandoComida(false);
            agente.addBITACORA_cantidad_alimento_transportado(agente.getCantidad_alimento_encontrado());
        }
        else{
            Posicion p = caminoMasEfectivo(celdasDisponibles, celdasAgentesVecinos, agente);
            if (p != null) {
                boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(mi_canvas.getImg_agente_2(), p, agente.getPosicionActual());
                if (pudoPonerAgente) {
                    agente.setPosicionActual(p);
                    agente.recordarPosicion(p);
                }
            }
        }
    }

    private Object[] celdasVecinasDisponibles(int i, int j) {
        int rowLimit = cant_filas - 1;
        int columnLimit = cant_columnas - 1;
        ArrayList<Posicion> celdasDisponibles = new ArrayList<>();
        ArrayList<Posicion> celdasAgentes = new ArrayList<>();
        ArrayList<Posicion> celdasAlimento = new ArrayList<>();

        for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, rowLimit); x++) {
            for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, columnLimit); y++) {
                if (x != i || y != j) {
                    if (C_Inicio.matriz.get(x, y).getTipo_objeto() == Objeto_IU.VACIO) {
                        celdasDisponibles.add(new Posicion(x, y));
                    }
                    else if(C_Inicio.matriz.get(x, y).getTipo_objeto() == Objeto_IU.AGENTE) {
                        celdasAgentes.add(new Posicion(x, y));
                    }
                    else if(C_Inicio.matriz.get(x, y).getTipo_objeto() == Objeto_IU.ALIMENTO) {
                        celdasAlimento.add(new Posicion(x, y));
                    }
                }
            }
        }
        return new Object[]{celdasDisponibles, celdasAgentes, celdasAlimento};
    }

    private Posicion caminoMasEfectivo(ArrayList<Posicion> celdasDisponibles, ArrayList<Posicion> celdasAgentes, AgenteFiducial agente) {
        Posicion posNueva, posAgente = agente.getPosicionActual();
        for(Posicion posAgenteVecino : celdasAgentes){
                posNueva = this.calcularPosicionContraria(posAgente, posAgenteVecino);
                if (this.comprobarCeldaDisponible(celdasDisponibles, posNueva.getFila(), posNueva.getColumna()))
                    return posNueva;
        }
        //Posicion
        return obtenerRandomMovSinRepetir(agente, celdasDisponibles);//(this.comprobarCeldaDisponible(celdasDisponibles, posNueva.getFila(), posNueva.getColumna())) ? posNueva : null;
    }

    private ArrayList celdasDisponiblesSinRepetir(ArrayList<Posicion> celdasDisponibles, AgenteFiducial agente){
        ArrayList<Posicion> celdasSinRepetir = new ArrayList<>();
        for(Posicion pos : celdasDisponibles){
            if (!esMovimientoRepetido(agente, pos))
                celdasSinRepetir.add(pos);
        }
        return celdasSinRepetir;
    }


    private Posicion obtenerRandomMovSinRepetir(AgenteFiducial agente, ArrayList<Posicion> celdasDisponibles){
        ArrayList<Posicion> celdasSinRepetir = celdasDisponiblesSinRepetir(celdasDisponibles, agente);
        Posicion posicionCandidata = desplazamientoAleatorio(agente.getPosicionActual());
        if (celdasSinRepetir.size() > 0){
            if (!comprobarCeldaDisponible(celdasSinRepetir, posicionCandidata.getFila(), posicionCandidata.getColumna()))
                return obtenerRandomMovSinRepetir(agente, celdasDisponibles);
        }
        return posicionCandidata;
    }

    private Posicion desplazamientoAleatorio(Posicion posAgente){
        Random r = new Random();
        int desplazamientoFila = r.nextInt(2 + 1) - 1;
        int desplazamientoColumna = r.nextInt(2 + 1) - 1;
        if (posAgente.getFila() == 0 && desplazamientoFila < 0) desplazamientoFila = 1;
        if (posAgente.getFila() == cant_filas - 1 && desplazamientoFila > 0) desplazamientoFila = -1;
        if (posAgente.getColumna() == 0 && desplazamientoColumna < 0) desplazamientoColumna = 1;
        if (posAgente.getColumna() == cant_columnas - 1 && desplazamientoColumna > 0) desplazamientoColumna = -1;
        return new Posicion(posAgente.getFila()+desplazamientoFila, posAgente.getColumna()+desplazamientoColumna);
    }

    private Posicion calcularPosicionContraria(Posicion posAgente, Posicion posAgenteVecino){
        int filaAgenteContraria = posAgente.getFila() - posAgenteVecino.getFila() + posAgente.getFila() ;
        int colAgenteContraria = posAgente.getColumna() - posAgenteVecino.getColumna() + posAgente.getColumna();
        return new Posicion(filaAgenteContraria, colAgenteContraria);
    }

    private boolean comprobarCeldaDisponible(ArrayList<Posicion> celdasDisponibles, int fila, int columna){
        for(Posicion pos : celdasDisponibles){
            if((fila == pos.getFila()) && (columna == pos.getColumna()))
                return true;
        }
        return false;
    }

    private boolean esMovimientoRepetido(AgenteFiducial agenteFiducial, Posicion agentePosicion){
        for(Posicion pos: agenteFiducial.getCaminoACasa()){
            if(pos.getFila() == agentePosicion.getFila() && pos.getColumna() == agentePosicion.getColumna()){
                return true;
            }
        }
        return false;
    }

    /**
     * Los recursos de alimento no son para siempre, dado un lapso de tiempo, dentro del nido
     * .. la comida se empezará a agotar
     * @param tiempo
     * @return
     */
    private synchronized boolean consumirAlimentoNido(long tiempo) {
        if ((tiempo >= getDuracion_alimento()) && getAlimentoRecolectado() > 0) {
            super.consumirAlimentoRecolectado(); // le resta 1 a la cantidad de alimentos en nido
            return true;
        }
        return false;
    }



}

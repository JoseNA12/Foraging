package modelo;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;
import modelo.FuenteAlimento;
import java.util.ArrayList;
import java.util.Random;
import static controlador.C_Inicio.*;
import static controlador.C_Inicio.mi_canvas;

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
                long inicio = System.currentTimeMillis();
                while (juego_activo) {
                    for (int i = 0; i < getAgentes().size(); i++) {
                        AgenteFiducial agente = (AgenteFiducial) getAgentes().get(i);
                        if (agente.isBuscandoComida()) {
                            moverAgente(agente);
                        }
                        else {
                            irANido(agente);
                        }
                    }

                    long diferencia = System.currentTimeMillis() - inicio;
                    Thread.sleep((long) lapsos_tiempo_ejecucion);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void irANido(AgenteFiducial agente){
        System.out.println("Nido");
        ArrayList<Posicion> caminoNido = agente.getCaminoACasa();
        if (caminoNido.size() > 0) {
            boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(mi_canvas.getImg_agente_alimento_1(), caminoNido.get(0),agente.getPosicionActual());
            if (pudoPonerAgente) {
                agente.setPosicionActual(caminoNido.get(0));
                agente.removeCeldaCaminoCasa(0); // remover de caminoNido la ubicacion utilizada
            }
        }
    }

    private void moverAgente(AgenteFiducial agente){
        Posicion pos = agente.getPosicionActual();
        Object[] returnArrayList = celdasVecinasDisponibles(pos.getFila(), pos.getColumna());
        ArrayList<Posicion> celdasDisponibles = (ArrayList<Posicion>) returnArrayList[0];
        ArrayList<Posicion> celdasAgentesVecinos = (ArrayList<Posicion>) returnArrayList[1];
        ArrayList<Posicion> celdasAlimento = (ArrayList<Posicion>) returnArrayList[2];
        if(!celdasAlimento.isEmpty()){
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(celdasAlimento.size()); // si hay varias escoja 1 al azar
            Posicion p = celdasAlimento.get(randomInt);
            int fila = p.getFila();
            int col = p.getColumna();
            FuenteAlimento fa = null; // obtengo el recurso
            try {
                fa = (FuenteAlimento) C_Inicio.matriz.get(fila, col);
            }catch(Exception e){
                System.out.println(e);
            }
            agente.setCantidad_alimento_encontrado(fa.consumirAlimento(agente.getCantidad_alimento_recoger()));
            agente.setBuscandoComida(false);
            System.out.println("Set as false");
        }
        else{
            Posicion p = caminoMasEfectivo(celdasDisponibles, celdasAgentesVecinos, pos);
            if (p != null) {
                boolean pudoPonerAgente = C_Inicio.matriz.setAgenteCasilla(mi_canvas.getImg_agente(), p, agente.getPosicionActual());
                if (pudoPonerAgente) {
                    agente.setPosicionActual(p);
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
                        System.out.println("Agregando agente a la lista de vecinos");
                        celdasAgentes.add(new Posicion(x, y));
                    }
                    else if(C_Inicio.matriz.get(x, y).getTipo_objeto() == Objeto_IU.ALIMENTO) {
                        System.out.println("Agregando alimento a la lista de alimentos");
                        celdasAlimento.add(new Posicion(x, y));
                    }
                }
            }
        }
        return new Object[]{celdasDisponibles, celdasAgentes, celdasAlimento};
    }

    private Posicion caminoMasEfectivo(ArrayList<Posicion> celdasDisponibles, ArrayList<Posicion> celdasAgentes, Posicion posAgente) {
        Posicion posNueva;
        for(Posicion posAgenteVecino : celdasAgentes){
            posNueva = this.calcularPosicionContraria(posAgente, posAgenteVecino);
            if (this.comprobarCeldaDisponible(celdasDisponibles, posNueva.getFila(), posNueva.getColumna()))
                return posNueva;
        }
        posNueva = this.desplazamientoAleatorio(posAgente);
        return (this.comprobarCeldaDisponible(celdasDisponibles, posNueva.getFila(), posNueva.getColumna())) ? posNueva : null;
    }

    private Posicion desplazamientoAleatorio(Posicion posAgente){
        Random r = new Random();
        int desplazamientoFila = r.nextInt(2 + 1) - 1;
        int desplazamientoColumna = r.nextInt(2 + 1) - 1;
        return new Posicion(posAgente.getFila()+desplazamientoFila, posAgente.getColumna()+desplazamientoColumna);
    }
    private Posicion calcularPosicionContraria(Posicion posAgente, Posicion posAgenteVecino){
        int filaAgenteContraria = posAgente.getFila() - posAgenteVecino.getFila() + 1;
        int colAgenteContraria = posAgente.getColumna() - posAgenteVecino.getColumna() + 1;
        return new Posicion(filaAgenteContraria, colAgenteContraria);
    }

    private boolean comprobarCeldaDisponible(ArrayList<Posicion> celdasDisponibles, int fila, int columna){
        for(Posicion pos : celdasDisponibles){
            if((fila == pos.getFila()) && (columna == pos.getColumna()))
                return true;
        }
        return false;
    }



}

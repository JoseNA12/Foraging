package modelo;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import modelo.otros.Objeto_IU;

import java.util.ArrayList;
import java.util.Random;

import static controlador.C_Inicio.*;
import static controlador.C_Inicio.mi_canvas;

public class BusquedaFiducial {

    private Nido nido;

    public BusquedaFiducial(Nido nido){
        this.nido = nido;
        crearEnjambre();
        iniciar();
    }

    public void crearEnjambre() {
        for (int i = 0; i < this.nido.getCantidad_agentes(); i++) {
            String id = this.nido.getID() + "_Hormiga_Agente_" + i;
            this.nido.addAgente(crearAgente(id));
        }
    }

    private Agente crearAgente(String id) {
        return new Agente(this.nido.getPosicion(), id, this.nido.getCantidadAlimentoRecoger(), this.nido.isTienenVida(), this.nido.getCantidadVida());
    }

    public void iniciar() {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                while (juego_activo) {
                    ArrayList<Agente>  agentes = nido.getAgentes();
                    for (Agente agente: agentes)
                        moverAgente(agente);
                    Thread.sleep((long) lapsos_tiempo_ejecucion);

                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void moverAgente(Agente agente){
        Posicion pos = agente.getPosicion();
        Object[] returnArrayList = celdasVecinasDisponibles(pos.getFila(), pos.getColumna());
        ArrayList<Posicion> celdasDisponibles = (ArrayList<Posicion>) returnArrayList[0];
        ArrayList<Posicion> celdasAgentesVecinos = (ArrayList<Posicion>) returnArrayList[1];
       // System.out.println(celdasAgentesVecinos);
        System.out.println("Agente "+agente.getID());
        System.out.println("Fila "+pos.getFila() + "Colum " + pos.getColumna());
        if (!C_Inicio.matriz.isNido(pos.getFila(), pos.getColumna()))
            mi_canvas.limpiarCasilla(pos.getFila(), pos.getColumna());
        agente.setPosicion(caminoMasEfectivo(celdasDisponibles, celdasAgentesVecinos, pos));
        mi_canvas.dibujar_canvas(mi_canvas.getImg_agente(), agente.getPosicion().getFila(), agente.getPosicion().getColumna());
        System.out.println("Fila Nueva "+agente.getPosicion().getFila() + "Colum Nueva " + agente.getPosicion().getColumna());
        //Thread.sleep(1000);
    }

    private Object[] celdasVecinasDisponibles(int i, int j) {
        int rowLimit = cant_filas - 1;
        int columnLimit = cant_columnas - 1;
        ArrayList<Posicion> celdasDisponibles = new ArrayList<>();
        ArrayList<Posicion> celdasAgentes = new ArrayList<>();

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
                }
            }
        }
        return new Object[]{celdasDisponibles, celdasAgentes};
    }

    private Posicion caminoMasEfectivo(ArrayList<Posicion> celdasDisponibles, ArrayList<Posicion> celdasAgentes, Posicion posAgente) {
        Posicion posNueva;
        for(Posicion posAgenteVecino : celdasAgentes){
            posNueva = this.calcularPosicionContraria(posAgente, posAgenteVecino);
            if (this.comprobarCeldaDisponible(celdasDisponibles, posNueva.getFila(), posNueva.getColumna()))
                return posNueva;
        }
        posNueva = this.desplazamientoAleatorio(posAgente);
        return (this.comprobarCeldaDisponible(celdasDisponibles, posNueva.getFila(), posNueva.getColumna())) ? posNueva : posAgente;
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

package modelo;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;

import java.util.concurrent.ExecutionException;

import static controlador.C_Inicio.juego_activo;
import static controlador.C_Inicio.mi_canvas;


public class FuenteAlimento extends Celda {

    private int cantidad;   // cantidad que puede producir la fuente de alimento
    private int cantidadDisponible = 0;
    //private int tiempo_disponible;
    private int tiempo_regenerar;
    private boolean regenerando = false;


    public FuenteAlimento(Celda pCelda, int cantidad, int tiempo_regenerar) {
        super(pCelda.getFila(), pCelda.getColumna(), pCelda.getTipo_objeto());

        this.cantidad = cantidad;
        this.cantidadDisponible = cantidad;
        //this.tiempo_disponible = tiempo_disponible * 1000; // se almacenan en milisegundos
        this.tiempo_regenerar = tiempo_regenerar * 1000;
    }

    public synchronized int consumirAlimento(int pCantidadConsumir) {
        final int[] valorRetorno = {0};

        Task task = new Task<Object>() {

            @Override
            protected Object call() throws Exception {

                if (!regenerando) {
                    if (cantidadDisponible > 0) {
                        if (cantidadDisponible < pCantidadConsumir) { // en caso de que lo que se va a consumir satisface una parte
                            valorRetorno[0] = cantidadDisponible;
                            cantidadDisponible = 0;
                        }
                        else {
                            valorRetorno[0] = pCantidadConsumir;
                            cantidadDisponible -= pCantidadConsumir;
                        }
                        // Si la cantidad de alimentos es < 0, realizar la producción en otro hilo
                        // Generando el alimento en otro hilo asegura que el agente no se quede esperando su disponibilidad
                        // System.out.println("Cantidad Disponible: " + cantidadDisponible);

                        if (cantidadDisponible <= 0) {
                            generarAlimento();
                        }
                    }
                }
                return null;
            }
        };

        Thread o = new Thread(task);
        o.start();
        try {
            o.join(); // espero a que la tarea termine su ejecución
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return valorRetorno[0];
    }

    private void generarAlimento() {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                //System.out.println("REGENERANDO...");
                regenerando = true;
                mi_canvas.dibujar_canvas(mi_canvas.getImg_fuente_alimento_no_disp(), getFila(), getColumna());
                Thread.sleep(tiempo_regenerar);
                //System.out.println("LISTO, ALIMENTOS DISPONIBLES...");
                mi_canvas.dibujar_canvas(mi_canvas.getImg_fuente_alimento(), getFila(), getColumna());
                regenerando = false;
                cantidadDisponible = cantidad;
                return null;
            }
        };
        new Thread(task).start();
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public boolean isRegenerando() {
        return regenerando;
    }

    public void setRegenerando(boolean regenerando) {
        this.regenerando = regenerando;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTiempo_regenerar() {
        return tiempo_regenerar;
    }

    public void setTiempo_regenerar(int tiempo_regenerar) {
        this.tiempo_regenerar = tiempo_regenerar;
    }
}

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
    private boolean disponible = false;
    private boolean regenerando = false;


    public FuenteAlimento(Celda pCelda, int cantidad, int tiempo_regenerar) {
        super(pCelda.getFila(), pCelda.getColumna(), pCelda.getTipo_objeto());

        this.cantidad = cantidad;
        this.cantidadDisponible = cantidad;
        //this.tiempo_disponible = tiempo_disponible * 1000; // se almacenan en milisegundos
        this.tiempo_regenerar = tiempo_regenerar * 1000;
    }

    /**
     * Una vez que se crea una fuente de alimento, desde C_Inicio se llama a esta para iniciar
     * .. su comportamiento en un hilo
     * @param c: Canvas por el cual se pinta la fuente de alimento en la IU
     * @param img_disponible
     * @param img_no_disponible
     * @param x: Coordenada X en la matriz
     * @param y: Coordenada Y en la matriz
     */
    public void init(CanvasJuego c, Image img_disponible, Image img_no_disponible) {

        Task task = new Task<Object>() {
            @Override
            protected Object call() {
                long inicio = System.currentTimeMillis();

                while (juego_activo) {
                    if (cantidadDisponible <= 0) {
                        try {
                            Thread.sleep(tiempo_regenerar);
                            disponible = true;
                            cantidadDisponible = cantidad;
                            c.dibujar_canvas(img_disponible, getFila(), getColumna());
                        } catch (InterruptedException e) {
                            System.out.println("Error: tiempo_regenerar. FuenteAlimento"); //e.printStackTrace();
                        }
                    }
                    else {
                        c.dibujar_canvas(img_disponible, getFila(), getColumna());
                        disponible = true;
                    }
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public synchronized int consumirAlimento(int pCantidadConsumir) {
        Task task = new Task<Object>() {
            int valorRetorno = -1;

            @Override
            protected Object call() throws Exception {

                if (!regenerando) {
                    if (cantidadDisponible > 0) {
                        if (cantidadDisponible < pCantidadConsumir) { // en caso de que lo que se va a consumir satisface una parte
                            valorRetorno = cantidadDisponible;
                            cantidadDisponible = 0;
                        }
                        else {
                            valorRetorno = pCantidadConsumir;
                            cantidadDisponible -= pCantidadConsumir;
                        }

                        System.out.println("PASO 2: " + cantidadDisponible);

                        if (cantidadDisponible <= 0) {
                            System.out.println("PASO regenerar");
                            regenerando = true;
                            mi_canvas.dibujar_canvas(mi_canvas.getImg_fuente_alimento_no_disp(), getFila(), getColumna());
                            Thread.sleep(tiempo_regenerar);
                            mi_canvas.dibujar_canvas(mi_canvas.getImg_fuente_alimento(), getFila(), getColumna());
                            regenerando = false;
                            cantidadDisponible = cantidad;
                        }

                    }
                }
                return valorRetorno;
            }
        };

        new Thread(task).start();
        //return (boolean) task.getValue();
        return 1997;
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

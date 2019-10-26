package modelo;

import controlador.C_Inicio;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import static controlador.C_Inicio.juego_activo;


public class FuenteAlimento {
    private int cantidad;
    private int tiempo_disponible;
    private int tiempo_regenerar;
    private boolean disponible = true;


    public FuenteAlimento(int cantidad, int tiempo_disponible, int tiempo_regenerar) {
        this.cantidad = cantidad;
        this.tiempo_disponible = tiempo_disponible * 1000; // se almacenan en milisegundos
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
    public void init(CanvasJuego c, Image img_disponible, Image img_no_disponible, int x, int y) {

        Task task = new Task<Object>() {
            @Override
            protected Object call() {
                long inicio = System.currentTimeMillis();

                while (juego_activo) {
                    long diferencia = System.currentTimeMillis()-inicio;

                    if ((disponible) && (diferencia > tiempo_disponible)) {
                        inicio = System.currentTimeMillis();
                        c.dibujar_canvas(img_no_disponible, x, y);
                        disponible = false;
                    }
                    else {
                        if ((!disponible) && (diferencia > tiempo_regenerar)) {
                            inicio = System.currentTimeMillis();
                            c.dibujar_canvas(img_disponible, x, y);
                            disponible = true;
                        }
                    }
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTiempo_disponible() {
        return tiempo_disponible;
    }

    public void setTiempo_disponible(int tiempo_disponible) {
        this.tiempo_disponible = tiempo_disponible;
    }

    public int getTiempo_regenerar() {
        return tiempo_regenerar;
    }

    public void setTiempo_regenerar(int tiempo_regenerar) {
        this.tiempo_regenerar = tiempo_regenerar;
    }
}

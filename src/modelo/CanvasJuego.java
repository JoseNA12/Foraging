package modelo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static controlador.C_Inicio.height_btn_matriz;
import static controlador.C_Inicio.width_btn_matriz;

/**
 * La idea es manejar la referencia del canvas en un solo objeto estático en C_Inicio,
 * .. y así todos los hilos de agentes que se crean puedan dibujar en el canvas
 *
 *              NO ESTOY SEGURO SI ESTO FUNCIONARÁ
 */
public class CanvasJuego {
    private Canvas canvas;


    public CanvasJuego(Canvas canvas) {
        this.canvas = canvas;
    }

    public synchronized void dibujar_canvas(Image pImagen, int x, int y) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(x * width_btn_matriz, y * height_btn_matriz, width_btn_matriz, height_btn_matriz);
        gc.drawImage(pImagen, x * width_btn_matriz, y * height_btn_matriz, width_btn_matriz, height_btn_matriz);

    }
}

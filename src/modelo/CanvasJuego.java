package modelo;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import modelo.otros.Path_Imagenes;

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

    private Image img_obstaculo;
    private Image img_nido;
    private Image img_fuente_alimento;
    private Image img_fuente_alimento_no_disp;
    private Image img_agente_1, img_agente_alimento_1;
    private Image img_agente_2, img_agente_alimento_2;


    public CanvasJuego(Canvas canvas) {
        this.canvas = canvas;

        img_obstaculo = new Image(getClass().getResourceAsStream(Path_Imagenes.OBSTACULO.getContenido()));
        img_nido = new Image(getClass().getResourceAsStream(Path_Imagenes.NIDO.getContenido()));
        img_fuente_alimento = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO.getContenido()));
        img_fuente_alimento_no_disp = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO_NO_DISP.getContenido()));
        img_agente_1 = new Image(getClass().getResourceAsStream(Path_Imagenes.AGENTE.getContenido()));
        img_agente_alimento_1 = new Image(getClass().getResourceAsStream(Path_Imagenes.AGENTE_ALIMENTO.getContenido()));
        img_agente_2 = new Image(getClass().getResourceAsStream(Path_Imagenes.AGENTE_2.getContenido()));
        img_agente_alimento_2 = new Image(getClass().getResourceAsStream(Path_Imagenes.AGENTE_2_ALIMENTO.getContenido()));
    }

    public synchronized void dibujar_canvas(Image pImagen, int x, int y) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(y * width_btn_matriz, x * height_btn_matriz, width_btn_matriz, height_btn_matriz);
        gc.drawImage(pImagen, y * width_btn_matriz, x * height_btn_matriz, width_btn_matriz, height_btn_matriz);
    }

    public void limpiarCasilla(int x, int y) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(x * width_btn_matriz, y * height_btn_matriz, width_btn_matriz, height_btn_matriz);
    }

    public Image getImg_obstaculo() {
        return img_obstaculo;
    }

    public void setImg_obstaculo(Image img_obstaculo) {
        this.img_obstaculo = img_obstaculo;
    }

    public Image getImg_nido() {
        return img_nido;
    }

    public void setImg_nido(Image img_nido) {
        this.img_nido = img_nido;
    }

    public Image getImg_fuente_alimento() {
        return img_fuente_alimento;
    }

    public void setImg_fuente_alimento(Image img_fuente_alimento) {
        this.img_fuente_alimento = img_fuente_alimento;
    }

    public Image getImg_fuente_alimento_no_disp() {
        return img_fuente_alimento_no_disp;
    }

    public void setImg_fuente_alimento_no_disp(Image img_fuente_alimento_no_disp) {
        this.img_fuente_alimento_no_disp = img_fuente_alimento_no_disp;
    }

    public Image getImg_agente() {
        return img_agente_1;
    }

    public Image getImg_agente_2() {
        return img_agente_2;
    }


    public void setImg_agente(Image img_agente) {
        this.img_agente_1 = img_agente;
    }

    public Image getImg_agente_alimento_1() {
        return img_agente_alimento_1;
    }

    public Image getImg_agente_alimento_2() {
        return img_agente_alimento_2;
    }

    public void setImg_agente_alimento_1(Image img_agente_alimento_1) {
        this.img_agente_alimento_1 = img_agente_alimento_1;
    }
}

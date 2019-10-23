package controlador;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import modelo.Objeto_IU;
import modelo.Path_Imagenes;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;

public class C_Inicio {

    private int max_filas = 38;
    private int max_columnas = 52;
    private int min_filas = 10;
    private int min_columnas = 10;

    // indica que objeto poner en la matriz de botones
    private Objeto_IU obj_matriz_botones = null;
    private int tamanio_btn_matriz = 18;

    @FXML GridPane id_gridPane;
    @FXML Pane id_pane_matriz;

    @FXML TextField id_cant_filas;
    @FXML TextField id_cant_columnas;

    @FXML Text id_text_obj_agregar;

    @FXML Button id_btn_nidoAgentes;
    @FXML Button id_btn_fuenteAlimento;
    @FXML Button id_btn_obstaculos;
    @FXML Button id_btn_iniciar_simulacion;
    @FXML Button id_btn_eliminar_obj;

    public void initialize() throws Exception {
        init_componentes();
    }

    private void init_componentes() {

    }

    @FXML
    void onButtonClick_GenerarMatrizBotones(ActionEvent event) {
        id_gridPane.getChildren().clear();

        if ((id_cant_filas.getText().matches("[0-9]+") && id_cant_filas.getText().trim().length() > 0) &&
                (id_cant_columnas.getText().matches("[0-9]+") && id_cant_columnas.getText().trim().length() > 0)) {

            int cant_filas = Integer.parseInt(id_cant_filas.getText());
            int cant_columnas = Integer.parseInt(id_cant_columnas.getText());

            // validaciones de la cantidad de filas y columnas a generar
            if (cant_filas < min_filas) { cant_filas = min_filas; }
            else { if (cant_filas > max_filas) { cant_filas = max_filas; }}
            if (cant_columnas < min_columnas) { cant_columnas = min_columnas; }
            else { if (cant_columnas > max_columnas) { cant_columnas = max_columnas; }}

            // crear el grid con botones
            for (int y = 0; y < cant_filas; y++) {
                for (int x = 0; x < cant_columnas; x++) {
                    //Random rand = new Random();
                    //int value = rand.nextInt(2);
                    Button btn = new Button();
                    btn.setMaxSize(Math.abs(id_pane_matriz.getWidth() / cant_columnas), Math.abs(id_pane_matriz.getHeight() / cant_filas));
                    btn.setMinSize(Math.abs(id_pane_matriz.getWidth() / cant_columnas), Math.abs(id_pane_matriz.getHeight() / cant_filas));
                    btn.setAlignment(Pos.CENTER);
                    btn.setOnAction(btn_matriz_handler);

                    id_gridPane.setRowIndex(btn, y);
                    id_gridPane.setColumnIndex(btn, x);
                    id_gridPane.getChildren().add(btn);
                }
            }
        }

    }

    private EventHandler<ActionEvent> btn_matriz_handler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Button btn = ((Button)e.getSource());
            Image image = null;

            if (obj_matriz_botones != null) {
                switch (obj_matriz_botones) {
                    case NIDO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.HORMIGA.getContenido()), tamanio_btn_matriz, tamanio_btn_matriz, false, false);
                        break;
                    case ALIMENTO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO.getContenido()), tamanio_btn_matriz, tamanio_btn_matriz, false, false);
                        break;
                    case OBSTACULO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.OBSTACULO.getContenido()), tamanio_btn_matriz, tamanio_btn_matriz, false, false);
                        break;
                    case ELIMINAR:
                        break;
                    default:
                        break;
                }
                btn.setGraphic(new ImageView(image));
            }
            e.consume();
        }
    };

    @FXML
    void onButtonClick_NidoAgentes(ActionEvent event) {
        obj_matriz_botones = Objeto_IU.NIDO;
        id_text_obj_agregar.setText(Objeto_IU.NIDO.getContenido());
    }

    @FXML
    void onButtonClick_FuenteAlimento(ActionEvent event) {
        obj_matriz_botones = Objeto_IU.ALIMENTO;
        id_text_obj_agregar.setText(Objeto_IU.ALIMENTO.getContenido());
    }

    @FXML
    void onButtonClick_Obstaculos(ActionEvent event) {
        obj_matriz_botones = Objeto_IU.OBSTACULO;
        id_text_obj_agregar.setText(Objeto_IU.ALIMENTO.getContenido());
    }

    @FXML
    void onButtonClick_eliminar(ActionEvent event) {
        obj_matriz_botones = Objeto_IU.ELIMINAR;
        id_text_obj_agregar.setText(Objeto_IU.ELIMINAR.getContenido());
    }
}

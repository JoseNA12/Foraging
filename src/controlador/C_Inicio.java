package controlador;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import modelo.Celda;
import modelo.Objeto_IU;
import modelo.Path_Imagenes;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class C_Inicio {

    @FXML GridPane id_gridPane;
    @FXML Pane id_pane_matriz;

    @FXML TextField id_cant_filas;
    @FXML TextField id_cant_columnas;

    @FXML Text id_text_obj_agregar;

    @FXML Button id_btn_nidoAgentes;
    @FXML Button id_btn_fuenteAlimento;
    @FXML Button id_btn_obstaculos;
    @FXML Button id_btn_eliminar_obj;

    // ------ Parámetros de la configuración de juego ------
    // Agentes
    @FXML TextField id_text_cantidad_agentes_x_nido;
    @FXML TextField id_text_cantidad_alimento_recoger;
    @FXML CheckBox id_check_agentes_morir;
    @FXML TextField id_text_vida_agentes;
    @FXML CheckBox id_check_agentes_reproduccion;

    // Nidos
    @FXML TextField id_text_cantidad_alimento_max_x_nido;
    @FXML TextField id_text_cantidad_alimento_min_x_nido;
    @FXML TextField id_text_duración_alimento_en_nido;

    // Fuentes alimento
    @FXML TextField id_text_cantidad_alimento_disponible_x_ubicacion;
    @FXML TextField id_text_tiempo_disponible_alimento_x_ubicacion;

    @FXML TextField id_text_lapsos_tiempo_duracion;
    @FXML Button id_btn_iniciar_simulacion;
    // -----------------------------------------------------

    // limites de la matriz de juego
    private int max_filas = 38;
    private int max_columnas = 52;
    private int min_filas = 10;
    private int min_columnas = 10;

    private int cant_filas = 0;
    private int cant_columnas = 0;

    // indica que objeto poner en la matriz seleccionable
    private Objeto_IU obj_matriz_botones = null;

    // tamaños de los botones/imagenes en la matriz seleccionable
    private int height_btn_matriz = 0;
    private int width_btn_matriz = 0;

    // cuando el usuario pone un objeto en la matriz de botones, se usa esta matriz para
    // .. almacenar el enum y asi trabajar sobre esta
    private ArrayList<ArrayList<Objeto_IU>> matriz_objetos_resultante;


    public void initialize() throws Exception {
        init_componentes();
    }

    private void init_componentes() {
        matriz_objetos_resultante = new ArrayList<ArrayList<Objeto_IU>>();
    }

    // establecer el tamaño de los botones e imagenes de la matriz que se generar segun el tamaño
    private void setTamaniosBotones(int pWidth, int pHeight) {
        height_btn_matriz = pHeight;
        width_btn_matriz = pWidth;
    }

    @FXML
    void onButtonClick_GenerarMatrizBotones(ActionEvent event) {
        id_gridPane.getChildren().clear();

        if ((id_cant_filas.getText().matches("[0-9]+") && id_cant_filas.getText().trim().length() > 0) &&
                (id_cant_columnas.getText().matches("[0-9]+") && id_cant_columnas.getText().trim().length() > 0)) {

            cant_filas = Integer.parseInt(id_cant_filas.getText());
            cant_columnas = Integer.parseInt(id_cant_columnas.getText());

            // validaciones de la cantidad de filas y columnas a generar
            if (cant_filas < min_filas) { cant_filas = min_filas; }
            else { if (cant_filas > max_filas) { cant_filas = max_filas; }}
            if (cant_columnas < min_columnas) { cant_columnas = min_columnas; }
            else { if (cant_columnas > max_columnas) { cant_columnas = max_columnas; }}

            setTamaniosBotones((int) Math.abs(id_pane_matriz.getWidth() / cant_columnas), (int) Math.abs(id_pane_matriz.getHeight() / cant_filas));

            // crear el grid con botones
            for (int y = 0; y < cant_filas; y++) {
                ArrayList<Objeto_IU> pLista = new ArrayList<>();

                for (int x = 0; x < cant_columnas; x++) {
                    Button btn = new Button();
                    btn.setMaxSize(width_btn_matriz, height_btn_matriz);
                    btn.setMinSize(width_btn_matriz, height_btn_matriz);
                    btn.setAlignment(Pos.CENTER);
                    btn.setOnAction(btn_matriz_handler);

                    // almacenar en el boton su respectiva coordenada
                    Celda c = new Celda(y, x, Objeto_IU.VACIO);
                    btn.setUserData(c);

                    pLista.add(Objeto_IU.VACIO);

                    // poner en el grid pane el boton
                    id_gridPane.setRowIndex(btn, y);
                    id_gridPane.setColumnIndex(btn, x);
                    id_gridPane.getChildren().add(btn);
                }
                matriz_objetos_resultante.add(pLista);
            }
        }
    }

    private EventHandler<ActionEvent> btn_matriz_handler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Button btn = ((Button)e.getSource()); // obtener el objeto del boton que se presiona
            int fila = ((Celda) btn.getUserData()).getFila();
            int columna = ((Celda) btn.getUserData()).getColumna();

            Image image = null; // hace posible poner la imagen segun el objeto que se selecciona

            if (obj_matriz_botones != null) {
                switch (obj_matriz_botones) {
                    case NIDO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.HORMIGA.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.NIDO);

                        matriz_objetos_resultante.get(fila).set(columna, Objeto_IU.NIDO);
                        break;
                    case ALIMENTO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.ALIMENTO);
                        matriz_objetos_resultante.get(fila).set(columna, Objeto_IU.ALIMENTO);
                        break;
                    case OBSTACULO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.OBSTACULO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.OBSTACULO);
                        matriz_objetos_resultante.get(fila).set(columna, Objeto_IU.OBSTACULO);
                        break;
                    case ELIMINAR:
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.VACIO);
                        matriz_objetos_resultante.get(fila).set(columna, Objeto_IU.VACIO);
                        break;
                    default:
                        break;
                }
                btn.setGraphic(new ImageView(image));
                System.out.println(btn.getUserData().toString());
                System.out.println(matriz_objetos_resultante.get(fila).get(columna));
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
        id_text_obj_agregar.setText(Objeto_IU.OBSTACULO.getContenido());
    }

    @FXML
    void onButtonClick_eliminar(ActionEvent event) {
        obj_matriz_botones = Objeto_IU.ELIMINAR;
        id_text_obj_agregar.setText(Objeto_IU.ELIMINAR.getContenido());
    }

    @FXML
    void onButtonClick_IniciarSimulacion(ActionEvent event) {

    }
}

package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import modelo.FuenteAlimento;
import modelo.Nido;
import modelo.Obstaculo;
import modelo.otros.Celda;
import modelo.otros.Objeto_IU;
import modelo.otros.Path_Imagenes;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class C_Inicio {

    @FXML Canvas id_canvas_juego;
    @FXML GridPane id_gridPane;
    @FXML Pane id_pane_matriz;

    @FXML TextField id_cant_filas;
    @FXML TextField id_cant_columnas;

    @FXML Text id_text_obj_agregar;

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
    @FXML TextField id_text_tiempo_en_regenerar_alimento;

    @FXML TextField id_text_lapsos_tiempo_duracion;
    @FXML Button id_btn_iniciar_simulacion;

    @FXML ProgressBar id_progress_bar;
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
    // .. almacenar un objeto Celda y asi trabajar sobre esta.
    // Antes de presionar el boton "Iniciar simulación" esta matriz nada mas tiene objetos Celda con
    // .. valores: fila, columna y Objeto_IU, y el otro objeto que puede ser: Nido, FuenteAlimento u Obstaculo
    // .. se setea una vez presionado el boton "Iniciar simulación".
    private ArrayList<ArrayList<Celda>> matriz_objetos;


    public void initialize() throws Exception {
        init_componentes();
    }

    private void init_componentes() {

        // validaciones para que los inputs solo acepten valores numericos
        id_text_cantidad_agentes_x_nido.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                15, integerFilter));
        id_text_cantidad_alimento_recoger.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                3, integerFilter));
        id_text_vida_agentes.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                120, integerFilter));
        id_text_cantidad_alimento_max_x_nido.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                20, integerFilter));
        id_text_cantidad_alimento_min_x_nido.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                5, integerFilter));
        id_text_duración_alimento_en_nido.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                15, integerFilter));
        id_text_cantidad_alimento_disponible_x_ubicacion.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                20, integerFilter));
        id_text_tiempo_disponible_alimento_x_ubicacion.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                15, integerFilter));
        id_text_tiempo_en_regenerar_alimento.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                30, integerFilter));
        id_text_lapsos_tiempo_duracion.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                1, integerFilter));

        id_check_agentes_morir.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                id_text_vida_agentes.setDisable(!newValue);
            }
        });
    }

    // hacer que los input's reciban unicamente valores numericos
    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    // establecer el tamaño de los botones e imagenes de la matriz que se generar segun el tamaño
    private void setTamaniosBotones(int pWidth, int pHeight) {
        height_btn_matriz = pHeight;
        width_btn_matriz = pWidth;
    }

    @FXML
    void onButtonClick_GenerarMatrizBotones(ActionEvent event) {
        id_gridPane.getChildren().clear();
        matriz_objetos = new ArrayList<ArrayList<Celda>>();

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
                ArrayList<Celda> pLista = new ArrayList<>();

                for (int x = 0; x < cant_columnas; x++) {
                    Button btn = new Button();
                    btn.setMaxSize(width_btn_matriz, height_btn_matriz);
                    btn.setMinSize(width_btn_matriz, height_btn_matriz);
                    btn.setAlignment(Pos.CENTER);
                    btn.setOnAction(btn_matriz_handler);

                    // almacenar en el boton su respectiva coordenada
                    Celda c = new Celda(y, x, Objeto_IU.VACIO);
                    btn.setUserData(c);

                    pLista.add(c);

                    // poner en el grid pane el boton
                    id_gridPane.setRowIndex(btn, y);
                    id_gridPane.setColumnIndex(btn, x);
                    id_gridPane.getChildren().add(btn);
                }
                matriz_objetos.add(pLista);
            }
        }
    }

    private EventHandler<ActionEvent> btn_matriz_handler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            Button btn = ((Button)e.getSource()); // obtener el objeto del boton que se presiona
            int fila = ((Celda) btn.getUserData()).getFila();
            int columna = ((Celda) btn.getUserData()).getColumna();
            Celda c = null;
            Image image = null; // hace posible poner la imagen segun el objeto que se selecciona

            if (obj_matriz_botones != null) {
                switch (obj_matriz_botones) {
                    case NIDO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.HORMIGA.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.NIDO);
                        c = matriz_objetos.get(fila).get(columna);
                        c.setTipo_objeto(Objeto_IU.NIDO);
                        matriz_objetos.get(fila).set(columna, c);
                        break;
                    case ALIMENTO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.ALIMENTO);
                        c = matriz_objetos.get(fila).get(columna);
                        c.setTipo_objeto(Objeto_IU.ALIMENTO);
                        matriz_objetos.get(fila).set(columna, c);
                        break;
                    case OBSTACULO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.OBSTACULO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.OBSTACULO);
                        c = matriz_objetos.get(fila).get(columna);
                        c.setTipo_objeto(Objeto_IU.OBSTACULO);
                        matriz_objetos.get(fila).set(columna, c);
                        break;
                    case ELIMINAR:
                        ((Celda) btn.getUserData()).setTipo_objeto(Objeto_IU.VACIO);
                        c = matriz_objetos.get(fila).get(columna);
                        c.setTipo_objeto(Objeto_IU.VACIO);
                        matriz_objetos.get(fila).set(columna, c);
                        break;
                    default:
                        break;
                }
                btn.setGraphic(new ImageView(image));
                //System.out.println(btn.getUserData().toString());
                //System.out.println(matriz_objetos.get(fila).get(columna));
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
    void onButtonClick_IniciarSimulacion(ActionEvent event) throws InterruptedException {
        id_gridPane.getChildren().clear();
        GraphicsContext gc = id_canvas_juego.getGraphicsContext2D();

        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                Image image = null; // para probar las posiciones nada mas
                id_progress_bar.setVisible(true);

                for (int y = 0; y < cant_filas; y++) {
                    id_progress_bar.setProgress(1 - (1 / y));

                    for (int x = 0; x < cant_columnas; x++) {
                        Objeto_IU obj = matriz_objetos.get(y).get(x).getTipo_objeto();

                        switch (obj) {
                            case OBSTACULO:
                                image = new Image(getClass().getResourceAsStream(Path_Imagenes.OBSTACULO.getContenido()));

                                matriz_objetos.get(y).get(x).setObjeto_en_juego(new Obstaculo());
                                break;
                            case NIDO:
                                image = new Image(getClass().getResourceAsStream(Path_Imagenes.HORMIGA.getContenido()));

                                Nido nido = new Nido(x + y,
                                        Integer.parseInt(id_text_cantidad_alimento_max_x_nido.getText()),
                                        Integer.parseInt(id_text_cantidad_alimento_min_x_nido.getText()),
                                        Integer.parseInt(id_text_duración_alimento_en_nido.getText()),
                                        Integer.parseInt(id_text_cantidad_agentes_x_nido.getText())
                                );
                                nido.crearEnjambre(
                                        id_check_agentes_morir.isSelected(),
                                        Integer.parseInt(id_text_cantidad_alimento_recoger.getText()),
                                        Integer.parseInt(id_text_vida_agentes.getText())
                                );
                                matriz_objetos.get(y).get(x).setObjeto_en_juego(nido);
                                break;
                            case ALIMENTO:
                                image = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO.getContenido()));

                                matriz_objetos.get(y).get(x).setObjeto_en_juego(
                                        new FuenteAlimento(
                                                Integer.parseInt(id_text_cantidad_alimento_disponible_x_ubicacion.getText()),
                                                Integer.parseInt(id_text_tiempo_disponible_alimento_x_ubicacion.getText()),
                                                Integer.parseInt(id_text_tiempo_en_regenerar_alimento.getText())
                                        ));
                                break;
                            default:
                                image = null;
                                break;
                        }
                        gc.drawImage(image, x * width_btn_matriz, y * height_btn_matriz, width_btn_matriz, height_btn_matriz);
                    }
                }
                id_progress_bar.setVisible(false);

                return null;
            }
        };

        new Thread(task).start();
    }

    private void ejemplo_hilo() {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {

                // contenido ...

                return null;
            }
        };

        new Thread(task).start();
    }

}

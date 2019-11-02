package controlador;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import modelo.*;
import modelo.AlgoritmoHormiga.NidoHormigas;
import modelo.otros.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class C_Inicio {

    @FXML Canvas id_canvas_juego;
    @FXML GridPane id_gridPane;
    @FXML Pane id_pane_matriz;

    // Generar matriz seleccionable de botones
    @FXML TextField id_cant_filas;
    @FXML TextField id_cant_columnas;

    @FXML Button id_btn_generar_matriz_botones;

    @FXML Text id_text_obj_agregar;
    // -----------------------------------------------------

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
    //@FXML TextField id_text_tiempo_disponible_alimento_x_ubicacion;
    @FXML TextField id_text_tiempo_en_regenerar_alimento;

    @FXML TextField id_text_lapsos_tiempo_duracion;
    // -----------------------------------------------------
    @FXML Button id_btn_nido_agentes;
    @FXML Button id_btn_fuente_alimento;
    @FXML Button id_btn_obstaculo;
    @FXML Button id_btn_borrar;
    // -----------------------------------------------------
    @FXML Button id_btn_iniciar_simulacion;
    @FXML Button id_btn_detener_simulacion;

    @FXML ProgressBar id_progress_bar;
    // -----------------------------------------------------

    // limites de la matriz de juego
    private int max_filas = 38;
    private int max_columnas = 52;
    private int min_filas = 10;
    private int min_columnas = 10;

    public static int cant_filas = 0;
    public static int cant_columnas = 0;

    // indica que objeto poner en la matriz seleccionable
    private Objeto_IU obj_matriz_botones = null;

    // tamaños de los botones/imagenes en la matriz seleccionable de botones
    public static int height_btn_matriz = 0;
    public static int width_btn_matriz = 0;

    // cuando el usuario pone un objeto en la matriz de botones, se usa esta matriz para
    // .. almacenar un objeto Celda y asi trabajar sobre esta.
    public static Matriz_grafo matriz;

    // variable por la cual se imprimen cosas/objectos en la IU
    public static CanvasJuego mi_canvas;

    public static boolean juego_activo = false;
    public static double lapsos_tiempo_ejecucion;


    public void initialize() throws Exception {
        init_componentes();
    }

    private void init_componentes() {

        // validaciones para que los inputs solo acepten valores numericos
        id_text_cantidad_agentes_x_nido.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                5, integerFilter));
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
        id_text_tiempo_en_regenerar_alimento.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                30, integerFilter));
        id_text_lapsos_tiempo_duracion.setText("0.7");

        id_check_agentes_morir.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                id_text_vida_agentes.setDisable(!newValue);
            }
        });

        id_cant_filas.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                20, integerFilter));
        id_cant_columnas.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(),
                20, integerFilter));

        mi_canvas = new CanvasJuego(id_canvas_juego);
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
        matriz = new Matriz_grafo();

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
            for (int i = 0; i < cant_filas; i++) {
                ArrayList<Celda> pLista = new ArrayList<>();

                for (int j = 0; j < cant_columnas; j++) {
                    Button btn = new Button();
                    btn.setMaxSize(width_btn_matriz, height_btn_matriz);
                    btn.setMinSize(width_btn_matriz, height_btn_matriz);
                    btn.setAlignment(Pos.CENTER);
                    btn.setOnAction(btn_matriz_handler);

                    // almacenar en el boton su respectiva coordenada
                    Celda c = new Celda(i, j, Objeto_IU.VACIO);
                    btn.setUserData(c);
                    btn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(Path_Imagenes.BLOQUE.getContenido()), width_btn_matriz, height_btn_matriz, false, false)));

                    pLista.add(c);

                    // poner en el grid pane el boton
                    id_gridPane.setRowIndex(btn, i);
                    id_gridPane.setColumnIndex(btn, j);
                    id_gridPane.getChildren().add(btn);
                }
                matriz.add(pLista);
            }
            estadoBotones_generar_matriz_botones(false);
        }
    }

    private void estadoBotones_generar_matriz_botones(boolean pEstado) {
        id_btn_borrar.setDisable(pEstado);
        id_btn_nido_agentes.setDisable(pEstado);
        id_btn_fuente_alimento.setDisable(pEstado);
        id_btn_obstaculo.setDisable(pEstado);
        id_btn_iniciar_simulacion.setDisable(pEstado);
    }

    private void estadoBotones_iniciar_simulacion(boolean pEstado) {
        id_btn_generar_matriz_botones.setDisable(pEstado);
        id_btn_detener_simulacion.setDisable(!pEstado);
        estadoBotones_generar_matriz_botones(pEstado);
    }

    private void estadoBotones_detener_simulacion(boolean pEstado) {
        id_btn_generar_matriz_botones.setDisable(!pEstado);
        id_btn_detener_simulacion.setDisable(pEstado);
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
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.NIDO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        matriz.get(fila, columna).setTipo_objeto(Objeto_IU.NIDO);
                        break;
                    case ALIMENTO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.ALIMENTO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        matriz.get(fila, columna).setTipo_objeto(Objeto_IU.ALIMENTO);
                        break;
                    case OBSTACULO:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.OBSTACULO.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        matriz.get(fila, columna).setTipo_objeto(Objeto_IU.OBSTACULO);
                        break;
                    case ELIMINAR:
                        image = new Image(getClass().getResourceAsStream(Path_Imagenes.BLOQUE.getContenido()), width_btn_matriz, height_btn_matriz, false, false);
                        matriz.get(fila, columna).setTipo_objeto(Objeto_IU.VACIO);
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
        juego_activo = true;
        lapsos_tiempo_ejecucion = Double.valueOf(id_text_lapsos_tiempo_duracion.getText()) * 1000;
        List<TipoEnjambre> enjambresUsados = new ArrayList<>();

        estadoBotones_iniciar_simulacion(true);

        Image image = null;
        int fila = 0, col = 0;

        for (int i = 0; i < cant_filas; i++) {
            for (int j = 0; j < cant_columnas; j++) {
                Celda pCelda = matriz.get(i, j);

                switch (pCelda.getTipo_objeto()) {
                    case OBSTACULO:
                        image = mi_canvas.getImg_obstaculo();
                        matriz.set(i, j, new Obstaculo(pCelda));
                        break;

                    case NIDO:
                        image = mi_canvas.getImg_nido();
                        Nido nido = crearEnjambre(enjambresUsados, pCelda, i + j);
                        matriz.set(i, j, nido);
                        mi_canvas.dibujar_canvas(image, i, j);
                        fila = i++;
                        col = j++;
                        break;

                    case ALIMENTO:
                        image = mi_canvas.getImg_fuente_alimento();

                        FuenteAlimento fa = new FuenteAlimento(pCelda,
                                Integer.parseInt(id_text_cantidad_alimento_disponible_x_ubicacion.getText()),
                                Integer.parseInt(id_text_tiempo_en_regenerar_alimento.getText())
                        );
                        matriz.set(i, j, fa);
                        break;

                    default:
                        // por default las casillas son VACIAS
                        image = null;
                        break;
                }
                mi_canvas.dibujar_canvas(image, i, j);
                // dibujar los objetos que el usuario seleccionó
            }
        }
        image = mi_canvas.getImg_agente();
        mi_canvas.dibujar_canvas(image, fila, col);
    }

    @FXML
    void onButtonClick_DetenerSimulacion(ActionEvent event) {
        juego_activo = false;
        estadoBotones_detener_simulacion(true);
    }

    // retornar un nido a crear acorde a los algoritmos implementados
    private Nido crearEnjambre(List<TipoEnjambre> enjambresUsados, Celda pCelda, int ID) {
        System.out.println("Llegue a crear enjambre");
        TipoEnjambre enjambre_enum;
        List<TipoEnjambre> enjambresDisponibles = new ArrayList(Arrays.asList(TipoEnjambre.class.getEnumConstants()));

        for (TipoEnjambre i: enjambresUsados) {
            enjambresDisponibles.remove(i); // de todos los disponibles elimino los utilizados
        }

        if (enjambresDisponibles.size() > 0) {
            enjambre_enum = enjambresDisponibles.get(0);
            enjambresUsados.add(enjambresDisponibles.get(0));
        }
        else { // si todos los tipos de enjambres fueron usados, repita de nuevo la lista
            enjambresUsados = Arrays.asList(TipoEnjambre.class.getEnumConstants());
            enjambre_enum = enjambresUsados.get(0);
        }
        Nido nido = null;
        enjambre_enum = TipoEnjambre.FIDUCIAL;
        switch (enjambre_enum) {
            case HORMIGA:
                System.out.println("Hormiga");
                nido = new NidoHormigas(
                        pCelda, ID,
                        Integer.parseInt(id_text_cantidad_alimento_max_x_nido.getText()),
                        Integer.parseInt(id_text_cantidad_alimento_min_x_nido.getText()),
                        Integer.parseInt(id_text_duración_alimento_en_nido.getText()),
                        Integer.parseInt(id_text_cantidad_agentes_x_nido.getText()),
                        id_check_agentes_morir.isSelected(),
                        Integer.parseInt(id_text_cantidad_alimento_recoger.getText()),
                        Integer.parseInt(id_text_vida_agentes.getText()),
                        id_check_agentes_reproduccion.isSelected()
                );
                break;
            case FIDUCIAL:
                System.out.println("Fiducial");
                nido = new NidoFiducial( // <<<<<--------------- CAMBIAR
                        pCelda, ID,
                        Integer.parseInt(id_text_cantidad_alimento_max_x_nido.getText()),
                        Integer.parseInt(id_text_cantidad_alimento_min_x_nido.getText()),
                        Integer.parseInt(id_text_duración_alimento_en_nido.getText()),
                        Integer.parseInt(id_text_cantidad_agentes_x_nido.getText()),
                        id_check_agentes_morir.isSelected(),
                        Integer.parseInt(id_text_cantidad_alimento_recoger.getText()),
                        Integer.parseInt(id_text_vida_agentes.getText()),
                        id_check_agentes_reproduccion.isSelected()
                );
                break;
            default:
                break;
        }
        return nido;
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

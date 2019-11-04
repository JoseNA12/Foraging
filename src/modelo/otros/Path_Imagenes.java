package modelo.otros;

public enum Path_Imagenes {
    NIDO("../vista/imagenes/home.png"),
    ALIMENTO("../vista/imagenes/donut.png"),
    OBSTACULO("../vista/imagenes/wall.png"),
    BLOQUE("../vista/imagenes/bloque.png"),

    ALIMENTO_NO_DISP("../vista/imagenes/donut_no_disponible.png"),
    AGENTE("../vista/imagenes/hormiga.png"),
    AGENTE_ALIMENTO("../vista/imagenes/hormiga_alimento.png"),
    AGENTE_2("../vista/imagenes/robotic.png"),
    AGENTE_2_ALIMENTO("../vista/imagenes/robotic_alimento.png");

    ;

    private String contenido;

    Path_Imagenes(String pContenido) {
        this.contenido = pContenido;
    }

    public String getContenido() {
        return contenido;
    }
}

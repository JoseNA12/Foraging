package modelo.otros;

public enum Path_Imagenes {
    NIDO("../vista/imagenes/home.png"),
    ALIMENTO("../vista/imagenes/donut.png"),
    OBSTACULO("../vista/imagenes/wall.png"),
    BLOQUE("../vista/imagenes/bloque.png"),

    ALIMENTO_NO_DISP("../vista/imagenes/donut_no_disponible.png"),
    AGENTE("../vista/imagenes/agente.png"),
    AGENTE_ALIMENTO("../vista/imagenes/agente_alimento.png");

    ;

    private String contenido;

    Path_Imagenes(String pContenido) {
        this.contenido = pContenido;
    }

    public String getContenido() {
        return contenido;
    }
}

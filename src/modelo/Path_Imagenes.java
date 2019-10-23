package modelo;

public enum Path_Imagenes {
    HORMIGA("../vista/imagenes/ant.png"),
    ALIMENTO("../vista/imagenes/doughnut.png"),
    OBSTACULO("../vista/imagenes/wall.png")
    ;

    private String contenido;

    Path_Imagenes(String pContenido) {
        this.contenido = pContenido;
    }

    public String getContenido() {
        return contenido;
    }
}
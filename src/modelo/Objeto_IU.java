package modelo;

public enum Objeto_IU {
    NIDO("Nido de agentes"),
    ALIMENTO("Fuente de alimento"),
    OBSTACULO("Obstaculo"),
    ELIMINAR("Eliminar...")
    ;

    private String contenido;

    Objeto_IU(String pContenido) {
        this.contenido = pContenido;
    }

    public String getContenido() {
        return contenido;
    }
}

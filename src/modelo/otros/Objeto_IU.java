package modelo.otros;

public enum Objeto_IU {
    NIDO("Nido de agentes"),
    ALIMENTO("Fuente de alimento"),
    OBSTACULO("Obstaculo"),
    ELIMINAR("Eliminar..."),
    VACIO("Vacio"),
    AGENTE("Agente"),
    ;

    private String contenido;

    Objeto_IU(String pContenido) {
        this.contenido = pContenido;
    }

    public String getContenido() {
        return contenido;
    }
}

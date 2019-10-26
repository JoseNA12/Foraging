package modelo.otros;

public class Celda {

    private int fila;
    private int columna;
    private Objeto_IU tipo_objeto;
    private Object objeto_en_juego = null; // puede ser Nido, FuenteAlimento u Obstaculo (modelos)


    public Celda(int fila, int columna, Objeto_IU tipo_objeto, Object objeto_en_juego) {
        this.fila = fila;
        this.columna = columna;
        this.tipo_objeto = tipo_objeto;
        this.objeto_en_juego = objeto_en_juego;
    }

    public Celda(int fila, int columna, Objeto_IU tipo_objeto) {
        this.fila = fila;
        this.columna = columna;
        this.tipo_objeto = tipo_objeto;
    }

    public Object getObjeto_en_juego() {
        return objeto_en_juego;
    }

    public void setObjeto_en_juego(Object objeto_en_juego) {
        this.objeto_en_juego = objeto_en_juego;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public Objeto_IU getTipo_objeto() {
        return tipo_objeto;
    }

    public void setTipo_objeto(Objeto_IU tipo_objeto) {
        this.tipo_objeto = tipo_objeto;
    }

    public String toString() {
        if (tipo_objeto != null) {
            return tipo_objeto.getContenido() + ": " + String.valueOf(fila) + "," + String.valueOf(columna);
        }
        return "NULL: " + String.valueOf(fila) + "," + String.valueOf(columna);
    }
}

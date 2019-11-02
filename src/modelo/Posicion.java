package modelo;


public class Posicion {

    private int fila;
    private int columna;


    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
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

    public boolean mismasPosiciones(Posicion p) {
        return (this.fila == p.getFila() && this.columna == p.getColumna());
    }

    public String toString() {
        return "(" + this.fila + ", " + this.columna + ")";
    }
}

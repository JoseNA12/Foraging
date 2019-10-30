package modelo.otros;

import modelo.Posicion;

public class Celda {

    private Posicion posicion;
    private Objeto_IU tipo_objeto;


    public Celda(int fila, int columna, Objeto_IU tipo_objeto) {
        this.posicion = new Posicion(fila, columna);
        this.tipo_objeto = tipo_objeto;
    }

    public int getFila() {
        return this.posicion.getFila();
    }

    public void setFila(int fila) {
        this.posicion.setFila(fila);
    }

    public int getColumna() {
        return this.posicion.getColumna();
    }

    public void setColumna(int columna) {
        this.posicion.setColumna(columna);
    }

    public Objeto_IU getTipo_objeto() {
        return tipo_objeto;
    }

    public void setTipo_objeto(Objeto_IU tipo_objeto) {
        this.tipo_objeto = tipo_objeto;
    }

    public String toString() {
        if (tipo_objeto != null) {
            return tipo_objeto.getContenido() + ": " + String.valueOf(this.posicion.getFila()) + "," + String.valueOf(this.posicion.getColumna());
        }
        return "CELDA: " + String.valueOf(this.posicion.getFila()) + "," + String.valueOf(this.posicion.getColumna());
    }
}

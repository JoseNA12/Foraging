package modelo;

import modelo.otros.Celda;

public class Obstaculo extends Celda {

    public Obstaculo(Celda pCelda) {
        super(pCelda.getFila(), pCelda.getColumna(), pCelda.getTipo_objeto());
    }
}

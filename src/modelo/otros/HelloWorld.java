package modelo.otros;

import modelo.Posicion;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// https://www.geeksforgeeks.org/closest-pair-of-points-using-divide-and-conquer-algorithm/

public class HelloWorld {

    static double distance(Posicion p1, Posicion p2) {
        return Math.sqrt((p1.getFila() - p2.getFila())*(p1.getFila() - p2.getFila()) + (p1.getColumna() - p2.getColumna())*(p1.getColumna() - p2.getColumna()));
    }

    static ArrayList<Posicion> shortest_pair(ArrayList<Posicion> coordinates) {
        int m = coordinates.size();
        double shortest_distance = Double.MAX_VALUE;
        int[] shortest_pair = new int[2];
        ArrayList<Posicion> p = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            for (int j = i+1; j < m; j++) {
                double d = distance(coordinates.get(i), coordinates.get(j));

                if (d < shortest_distance) {
                    shortest_distance = d;
                    shortest_pair[0] = i;
                    shortest_pair[1] = j;
                    p.add(new Posicion(coordinates.get(i).getFila(), coordinates.get(i).getColumna()));
                }
            }
        }
        return p;
    }

    public static void initi(){
        int m = 30;
        Random rand = new Random();
        rand.setSeed(0);

        ArrayList<Posicion> coordinates = new ArrayList<>();
        coordinates.add(new Posicion(2,1));
        coordinates.add(new Posicion(1,0));
        coordinates.add(new Posicion(0,1));
        coordinates.add(new Posicion(1,1));
        coordinates.add(new Posicion(2,2));
        coordinates.add(new Posicion(3,2));
        coordinates.add(new Posicion(4,2));
        coordinates.add(new Posicion(4,3));
        coordinates.add(new Posicion(3,2));
        coordinates.add(new Posicion(3,3));
        coordinates.add(new Posicion(4,4));
        coordinates.add(new Posicion(3,4));
        coordinates.add(new Posicion(2,4));
        coordinates.add(new Posicion(3,5));
        coordinates.add(new Posicion(4,6));


        for (Posicion i: coordinates) {
            System.out.print(i.toString());
        }

        System.out.println("******************************");

        ArrayList<Posicion> r = shortest_pair(coordinates);

        for (Posicion i: r) {
            System.out.print(i.toString());
        }

    }
}

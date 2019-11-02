package controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TxtWriter {

    private static List<String> contenido = new ArrayList<>();
    private static String path = "src/controlador/bitacoras/";
    private static String nombre = "bitacora";
    private static String extension = ".txt";
    private static String fullPath;


    public static void init() {
        fullPath = path + nombre + extension;
    }

    public synchronized static void registrarBitacora(String msg) {
        contenido.add(msg);
    }

    public static void generarArchivo() {
        contenido.add("\n\n");
        try {
            Files.write(Paths.get(fullPath),
                    contenido,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            System.out.println("Bitacora generada");
        } catch (IOException e) {
            System.out.println("Error al generar el archivo .txt de la bitacora");
            e.printStackTrace();
        }
    }

}

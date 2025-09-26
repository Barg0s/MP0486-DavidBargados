package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class PR114linies {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "numeros.txt";

        // Crida al mètode que genera i escriu els números aleatoris
        generarNumerosAleatoris(camiFitxer);
    }

    // Mètode per generar 10 números aleatoris i escriure'ls al fitxer
    public static void generarNumerosAleatoris(String camiFitxer) {
/*        try {
            Files.delete(Path.of(camiFitxer));
        } catch (IOException e) {
            System.out.println("error al eliminar");
            e.printStackTrace();
        } */
        
        Random r = new Random(); 
        
        try (FileWriter fw = new FileWriter(camiFitxer,true)){
            for (int i = 0; i < 10;i++){
                int num = r.nextInt(100);
                fw.write(String.valueOf(num));
                if (i < 9) {
                    fw.write("\n");
                }
            }
        } catch (Exception e) {
                System.out.println("error al escriure el fitxer");
        }



    }

    public static void eliminarFitxer(String path) throws  IOException {
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            System.out.println("error al eliminar");
            e.printStackTrace();
        }
    }
}

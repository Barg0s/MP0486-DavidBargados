package com.project;

import java.io.FileWriter;
import java.io.IOException;

public class PR113append {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiFitxer = System.getProperty("user.dir") + "/data/frasesMatrix.txt";

        // Crida al mètode que afegeix les frases al fitxer
        afegirFrases(camiFitxer);
    }

    // Mètode que afegeix les frases al fitxer amb UTF-8 i línia en blanc final
    public static void afegirFrases(String camiFitxer) {
        String[] frases = {"I can only show you the door","You're the one that has to walk through it"};

        try (FileWriter fw = new FileWriter(camiFitxer,true)){
            for (String f : frases){
                fw.write(f + "\n");

            }
            } catch (IOException e) {
                System.out.println("error al escriure el fitxer");
            }

    }

}

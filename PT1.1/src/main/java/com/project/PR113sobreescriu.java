package com.project;

import java.io.FileWriter;
import java.io.IOException;

public class PR113sobreescriu {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiFitxer = System.getProperty("user.dir") + "/data/frasesMatrix.txt";

        // Crida al mètode que escriu les frases sobreescrivint el fitxer
        escriureFrases(camiFitxer);
    }

    // Mètode que escriu les frases sobreescrivint el fitxer amb UTF-8 i línia en blanc final
    public static void escriureFrases(String camiFitxer) {
                String[] frases = {"I can only show you the door","You're the one that has to walk through it"};

        try (FileWriter fw = new FileWriter(camiFitxer,false)){
            for (String f : frases){
                fw.write(f);
                fw.write("\n");

            }
            } catch (IOException e) {
                System.out.println("error al escriure el fitxer");
            }
    
    
    }

}

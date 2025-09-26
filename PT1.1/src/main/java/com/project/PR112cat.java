package com.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PR112cat {

    public static void main(String[] args) {
        // Comprovar que s'ha proporcionat una ruta com a paràmetre
        if (args.length == 0) {
            System.out.println("No s'ha proporcionat cap ruta d'arxiu.");
            return;
        }

        // Obtenir la ruta del fitxer des dels paràmetres
        String rutaArxiu = args[0];

        rutaArxiu = "";
        mostrarContingutArxiu(rutaArxiu);
    }

    // Funció per mostrar el contingut de l'arxiu o el missatge d'error corresponent
    public static void mostrarContingutArxiu(String rutaArxiu) {

        try {
                if (Files.isDirectory(Path.of(rutaArxiu))){
                    System.out.println("El path no correspon a un arxiu, sinó a una carpeta.");
                }else if (!Files.exists(Path.of(rutaArxiu))){
                    System.out.println("El fitxer no existeix o no és accessible.");

                }else{
                    llegirIMostrarFitxer(rutaArxiu);
                }

                
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
        public static void llegirIMostrarFitxer(String camiFitxer) {
        try {
            List<String> linies = Files.readAllLines(Paths.get(camiFitxer), StandardCharsets.UTF_8);
            mostrarLinies(linies);
        } catch (IOException e) {
            System.out.println("Error en la lectura del fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }
        public static void mostrarLinies(List<String> l){
        for (int i = 0; i < l.size();i++){
            System.out.println(i + 1 + ": " + l.get(i));
        }
    }
}

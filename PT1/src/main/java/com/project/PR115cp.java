package com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PR115cp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: Has d'indicar dues rutes d'arxiu.");
            System.out.println("Ús: PR115cp <origen> <destinació>");
            return;
        }

        // Ruta de l'arxiu origen
        String rutaOrigen = args[0];
        // Ruta de l'arxiu destinació
        String rutaDesti = args[1];

        // Crida al mètode per copiar l'arxiu
        copiarArxiu(rutaOrigen, rutaDesti);
    }

    // Mètode per copiar un arxiu de text de l'origen al destí
    public static void copiarArxiu(String rutaOrigen, String rutaDesti) {

        if (!Files.exists(Path.of(rutaOrigen))){
            System.out.println("L'arxiu no d'origen no existeix");

        }
        if (Files.exists(Path.of(rutaOrigen))){
            try {
                if (Files.exists(Path.of(rutaDesti))){
                    System.out.println("L'arxiu de destí ja existeix,es sobreesciurà");
                }
                Files.copy(Path.of(rutaOrigen), Path.of(rutaDesti));
                   
            } catch (IOException e) {
                // TODO: handle exception
            }
        }

    }
}

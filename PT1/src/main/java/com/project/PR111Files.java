package com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PR111Files {

    public static void main(String[] args) {
        String camiFitxer = System.getProperty("user.dir") + "/data/pr111";
        gestionarArxius(camiFitxer);
    }

    public static void gestionarArxius(String camiFitxer) {

        try {

            Files.createDirectories(Path.of(camiFitxer));
            System.out.println("carpeta: creada a" + camiFitxer);

        } catch (IOException e) {
            System.out.println("Error en la creació de la carpeta: " + camiFitxer);
            e.printStackTrace();
        }
    }

}

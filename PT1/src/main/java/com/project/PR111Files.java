package com.project;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PR111Files {

    public static void main(String[] args) {

        String camiFitxer = System.getProperty("user.dir") + "/data/pr111/myFiles";
        gestionarArxius(camiFitxer);
    }

    public static void gestionarArxius(String camiFitxer) {
        String fitxer = camiFitxer + "/file1.txt";

        try {
            Files.createDirectories(Path.of(camiFitxer));
            System.out.println("carpeta: creada a" + camiFitxer);
            escriureFitxer(fitxer);
            fitxer = camiFitxer + "/file2.txt";
            escriureFitxer(fitxer);
            renombrarFitxer(camiFitxer);
            MostrarContingut(camiFitxer);
        } catch (IOException e) {
            System.out.println("Error en la creació de la carpeta: " + camiFitxer);
            e.printStackTrace();
        }
    }
        public static void escriureFitxer(String camiFitxer) throws IOException {
            try {
                Files.createFile(Path.of(camiFitxer));
                
            } catch (IOException e) {
            System.out.println("Error en la creació del fitxer a : " + camiFitxer);
            e.printStackTrace();
                    }
    }
    public  static  void renombrarFitxer(String camiFitxer) throws  IOException{
        String origen = camiFitxer + "/file2.txt";

        String target = camiFitxer + "/renamedFile.txt";
        try {
            Files.move(Path.of(origen), Path.of(target), StandardCopyOption.REPLACE_EXISTING); //https://stackoverflow.com/questions/53004799/how-to-rename-file-java-nio
        } catch (Exception e) {
            System.out.println("Error en la creació del fitxer a : " + camiFitxer);
            e.printStackTrace();

        }
    }

    public static void MostrarContingut(String camiFitxer) throws  IOException{ //https://www.javacodegeeks.com/2014/06/listing-and-filtering-directory-contents-in-nio-2.html
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(camiFitxer))){
            for (Path p : stream){
                System.out.println(p);
            }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    }

}

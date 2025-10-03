package com.project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR122persona;

public class PR122main {
    private static String filePath = System.getProperty("user.dir") + "/data/PR122persones.dat";

    public static void main(String[] args) {
        List<PR122persona> persones = new ArrayList<>();
        persones.add(new PR122persona("Maria", "López", 36));
        persones.add(new PR122persona("Gustavo", "Ponts", 63));
        persones.add(new PR122persona("Irene", "Sales", 54));

        try {
            serialitzarPersones(persones);
            List<PR122persona> deserialitzades = deserialitzarPersones();
            deserialitzades.forEach(System.out::println);  // Mostra la informació per pantalla
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Mètode per serialitzar la llista de persones
    public static void serialitzarPersones(List<PR122persona> persones) throws IOFitxerExcepcio {
        try (FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(persones);
    } catch (Exception e) {
        throw new IOFitxerExcepcio("Error al serialitzar l'objecte", e);
    }
}

        // *************** CODI PRÀCTICA **********************/
        
    

    // Mètode per deserialitzar la llista de persones
    public static List<PR122persona> deserialitzarPersones() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        List<PR122persona> al = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(filePath);
         ObjectInputStream ois = new ObjectInputStream(fis)) {
        al = (List<PR122persona>) ois.readObject();

        return al;

    } catch (FileNotFoundException e) {
        throw new IOFitxerExcepcio("Error en deserialitzar l'objecte HashMap: fitxer no trobat", e);
    } catch (IOException | ClassNotFoundException e) {
        throw new IOFitxerExcepcio("Error en deserialitzar l'objecte HashMap", e);
    }
}
        


    // Getter i Setter per a filePath (opcional)
    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }
}

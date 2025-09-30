package com.project;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR121hashmap;

public class PR121mainLlegeix {
    private static String filePath = System.getProperty("user.dir") + "/data/PR121HashMapData.ser";

    public static void main(String[] args) {
        try {
            PR121hashmap hashMap = deserialitzarHashMap();
            hashMap.getPersones().forEach((nom, edat) -> System.out.println(nom + ": " + edat + " anys"));
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error al llegir l'arxiu: " + e.getMessage());
        }
    }

    public static PR121hashmap deserialitzarHashMap() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        PR121hashmap pr121hashmap = null;
        try (FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            HashMap<String, Integer> hm = pr121hashmap.getPersones();
            pr121hashmap = (PR121hashmap) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al serialitzar l'objecte.");        }
            return new PR121hashmap();
    }

    // Getter
    public static String getFilePath() {
        return filePath;
    }

    // Setter
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }    
}
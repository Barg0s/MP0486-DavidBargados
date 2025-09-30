package com.project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.project.excepcions.IOFitxerExcepcio;

public class PR120mainPersonesHashmap {
    private static String filePath = System.getProperty("user.dir") + "/data/PR120persones.dat";
    private DataOutputStream dos = null;
    public static void main(String[] args) {
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Anna", 25);
        persones.put("Bernat", 30);
        persones.put("Carla", 22);
        persones.put("David", 35);
        persones.put("Elena", 28);

        try {
            escriurePersones(persones);
            llegirPersones();
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error en treballar amb el fitxer: " + e.getMessage());
        }
    }

    // Getter per a filePath
    public static String getFilePath() {
        return filePath;
    }

    // Setter per a filePath
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    // Mètode per escriure les persones al fitxer
public static void escriurePersones(HashMap<String, Integer> persones) throws IOFitxerExcepcio {
    // *************** CODI PRÀCTICA **********************/
    try (FileOutputStream fos = new FileOutputStream(filePath)){
        DataOutputStream dos = new DataOutputStream(fos);
        for (Map.Entry<String,Integer> persona : persones.entrySet()) { //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            String nom = persona.getKey();
            int edat = persona.getValue();
            dos.writeUTF(nom);
            dos.writeInt(edat);
        }
            dos.flush();

    } catch (IOException e) {
        throw new IOFitxerExcepcio("Error en escriure les persones al fitxer", e);
    }}

    // Mètode per llegir les persones des del fitxer
    public static void llegirPersones() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        try(FileInputStream fis = new FileInputStream(filePath)) {
            DataInputStream dis = new DataInputStream(fis);
            while (true) { 
                try{
                String nom = dis.readUTF();
                int edat = dis.readInt();

                System.out.println(nom + ": " + edat + " anys");
                         } catch (EOFException e) {
                break; 
            }   
            }


         

        } catch (IOException e) {
        throw new IOFitxerExcepcio("Error en llegir les persones del fitxer", e);
            }

}
}
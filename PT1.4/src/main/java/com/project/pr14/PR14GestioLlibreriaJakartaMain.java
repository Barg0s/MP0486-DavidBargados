package com.project.pr14;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.project.objectes.Llibre;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;


/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de llibres.
 */
public class PR14GestioLlibreriaJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioLlibreriaJSONPMain.
     *
     * @param dataFile Fitxer on es troben els llibres.
     */
    public PR14GestioLlibreriaJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        File dataFile = new File(System.getProperty("user.dir"), "data/pr14" + File.separator + "llibres_input.json");
        PR14GestioLlibreriaJakartaMain app = new PR14GestioLlibreriaJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades dels llibres.
     */
    public void processarFitxer() {
        List<Llibre> llibres = carregarLlibres();
        if (llibres != null) {
            modificarAnyPublicacio(llibres, 1, 1995);
            afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
            esborrarLlibre(llibres, 2);
            guardarLlibres(llibres);
        }
    }

    /**
     * Carrega els llibres des del fitxer JSON.
     *
     * @return Llista de llibres o null si hi ha hagut un error en la lectura.
     */
    public List<Llibre> carregarLlibres() {
        // *************** CODI PRÀCTICA **********************/
        
        List<Llibre> listLlibres = new ArrayList<>();

        try (JsonReader jsonReader = Json.createReader(new FileReader(dataFile))) {
            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject object : jsonArray.getValuesAs(JsonObject.class)){
                int id = object.getInt("id");
                String nom = object.getString("titol");
                String autor = object.getString("autor");
                int any = object.getInt("any");
                listLlibres.add(new Llibre(id, nom, autor, any));

            }

        
    }catch (IOException e){
        e.printStackTrace();
    }
    return listLlibres;

}

    /**
     * Modifica l'any de publicació d'un llibre amb un id específic.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a modificar.
     * @param nouAny Nou any de publicació.
     */
    public void modificarAnyPublicacio(List<Llibre> llibres, int id, int nouAny) {
        // *************** CODI PRÀCTICA **********************/
        for (Llibre l : llibres){
            if (l.getId() == id){
                l.setAny(nouAny);
            }
        }        
    }

    /**
     * Afegeix un nou llibre a la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param nouLlibre Nou llibre a afegir.
     */
    public void afegirNouLlibre(List<Llibre> llibres, Llibre nouLlibre) {
        // *************** CODI PRÀCTICA **********************/
        llibres.add(nouLlibre);
    }

    /**
     * Esborra un llibre amb un id específic de la llista de llibres.
     *
     * @param llibres Llista de llibres.
     * @param id Identificador del llibre a esborrar.
     */
    public void esborrarLlibre(List<Llibre> llibres, int id) {
        // *************** CODI PRÀCTICA **********************/
        for (int i = 0; i < llibres.size(); i++) {
            if (llibres.get(i).getId() == id){
                llibres.remove(i);
            }   
        }


    }

    /**
     * Guarda la llista de llibres en un fitxer nou.
     *
     * @param llibres Llista de llibres a guardar.
     */
    public void guardarLlibres(List<Llibre> llibres) {
        // *************** CODI PRÀCTICA **********************/

        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Llibre l : llibres){
                JsonObject llibreJson = Json.createObjectBuilder()
                .add("id", l.getId())
                .add("titol", l.getTitol())
                .add("autor", l.getAutor())
                .add("any", l.getAny())
                .build();
            jab.add(llibreJson);

        }
        JsonArray jsonArray = jab.build();
        try (JsonWriter jsonWriter = Json.createWriter(Files.newBufferedWriter(Paths.get(dataFile.getParent(), "llibres_output_jakarta.json")))) {
            jsonWriter.writeArray(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }    }
}
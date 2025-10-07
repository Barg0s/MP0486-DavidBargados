package com.project.pr13;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Classe principal que crea un document XML amb informació de llibres i el guarda en un fitxer.
 * 
 * Aquesta classe permet construir un document XML, afegir elements i guardar-lo en un directori
 * especificat per l'usuari.
 */
public class PR131Main {

    private File dataDir;

    /**
     * Constructor de la classe PR131Main.
     * 
     * @param dataDir Directori on es guardaran els fitxers de sortida.
     */
    public PR131Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Retorna el directori de dades actual.
     * 
     * @return Directori de dades.
     */
    public File getDataDir() {
        return dataDir;
    }

    /**
     * Actualitza el directori de dades.
     * 
     * @param dataDir Nou directori de dades.
     */
    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     * 
     * @param args Arguments passats a la línia de comandament (no s'utilitzen en aquest programa).
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir, "data" + File.separator + "pr13");

        PR131Main app = new PR131Main(dataDir);
        app.processarFitxerXML("biblioteca.xml");
    }

    /**
     * Processa el document XML creant-lo, guardant-lo en un fitxer i comprovant el directori de sortida.
     * 
     * @param filename Nom del fitxer XML a guardar.
     */
    public void processarFitxerXML(String filename) {
        if (comprovarIDirCrearDirectori(dataDir)) {
            Document doc = construirDocument();
            File fitxerSortida = new File(dataDir, filename);
            guardarDocument(doc, fitxerSortida);
        }
    }

    /**
     * Comprova si el directori existeix i, si no és així, el crea.
     * 
     * @param directori Directori a comprovar o crear.
     * @return True si el directori ja existeix o s'ha creat amb èxit, false en cas contrari.
     */
    private boolean comprovarIDirCrearDirectori(File directori) {
        if (!directori.exists()) {
            return directori.mkdirs();
        }
        return true;
    }

    /**
     * Crea un document XML amb l'estructura d'una biblioteca i afegeix un llibre amb els seus detalls.
     * 
     * @return Document XML creat o null en cas d'error.
     */
    private static Document construirDocument() {
        // *************** CODI PRÀCTICA **********************/

        try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        //BIBLIOTECA
        Element bibliotecaRoot = doc.createElement("biblioteca");
        //afegir biblioteca al doc
        doc.appendChild(bibliotecaRoot);

        //LLIBRE
        Element elmLlibre = doc.createElement("llibre");
        Attr attrId = doc.createAttribute("id");
        attrId.setValue("001");
        elmLlibre.setAttributeNode(attrId);
        bibliotecaRoot.appendChild(elmLlibre);


        //ELEMENTS
        Element tit = doc.createElement("titol");
        Element autor = doc.createElement("autor");
        Element anyPublicacio = doc.createElement("anyPublicacio");
        Element editorial = doc.createElement("editorial");
        Element genere = doc.createElement("genere");
        Element pagines = doc.createElement("pagines");
        Element disponible = doc.createElement("disponible");

        //AFEGIR TEXT A ELEMENTS
        
        Text nodeTit = doc.createTextNode("El viatge dels venturons");

        tit.appendChild(nodeTit);
        Text nodeAutor = doc.createTextNode("Joan Pla");
        autor.appendChild(nodeAutor);

        Text nodeAny = doc.createTextNode("1998");
        anyPublicacio.appendChild(nodeAny);
        Text nodeEditorial = doc.createTextNode("Edicions Mar");
        editorial.appendChild(nodeEditorial);
        Text nodeGenere = doc.createTextNode("Aventura");
        genere.appendChild(nodeGenere);
        Text nodePagines = doc.createTextNode("320");
        pagines.appendChild(nodePagines);
        Text nodeDisponible = doc.createTextNode("true");
        disponible.appendChild(nodeDisponible);


        //AFEGIR ELEMENTS
        elmLlibre.appendChild(tit);
        elmLlibre.appendChild(autor);
        elmLlibre.appendChild(anyPublicacio);
        elmLlibre.appendChild(editorial);
        elmLlibre.appendChild(genere);
        elmLlibre.appendChild(pagines);
        elmLlibre.appendChild(disponible);

       return doc; // Substitueix pel teu
        
            
        } catch (Exception e) {
            System.out.println("El fitxer biblioteca.xml hauria d'existir");
        }

        

     return  null;   
    }




    /**
     * Guarda el document XML proporcionat en el fitxer especificat.
     * 
     * @param doc Document XML a guardar.
     * @param fitxerSortida Fitxer de sortida on es guardarà el document.
     */
    private static void guardarDocument(Document doc, File fitxerSortida) {
        // *************** CODI PRÀCTICA **********************/

        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult sr =new StreamResult(fitxerSortida);
            t.transform(source, sr);
            
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}

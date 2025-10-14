package cat.iesesteveterradas;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class GestioDB {

    


    public static void crearDB(Path filePath) throws SQLException{
        try (Connection conn = UtilsSQLite.connect(filePath.toString())){
            //TAULA FACCIÓ
            UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS faccio");
            UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS faccio ("
                    + " id integer PRIMARY KEY AUTOINCREMENT,"
                    + " nom text NOT NULL,"
                    + " resum text NOT NULL);");
            //TAULA PERSONATGE
            UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS personatge");
            UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS personatge ("
                    + " id integer PRIMARY KEY AUTOINCREMENT,"
                    + " nom text NOT NULL,"
                    + " atac text NOT NULL,"
                    + " defensa text NOT NULL,"
                    + " idFaccio integer NOT NULL,"
                    + " FOREIGN KEY (idFaccio) REFERENCES faccio(id));");


            //POPULAR DADES(PER FER)

        } catch (SQLException e) {
            e.printStackTrace();

        }


    }


}

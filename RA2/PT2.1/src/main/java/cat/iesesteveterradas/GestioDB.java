package cat.iesesteveterradas;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class GestioDB {

    public static void crearDB(Path filePath) throws SQLException {
        try (Connection conn = UtilsSQLite.connect(filePath.toString())) {

            // TAULA FACCIÓ
            UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS faccio");
            UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS faccio ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " nom VARCHAR(15) NOT NULL,"
                    + " resum VARCHAR(500) NOT NULL);");

            // TAULA PERSONATGE
            UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS personatge");
            UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS personatge ("
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " nom VARCHAR(15) NOT NULL,"
                    + " atac INTEGER NOT NULL," 
                    + " defensa INTEGER NOT NULL,"
                    + " idFaccio INTEGER NOT NULL,"
                    + " FOREIGN KEY (idFaccio) REFERENCES faccio(id));");

            // Popular dades
            popularFaccions(conn);
            popularPersonatges(conn);
        }
    }

    public static void popularFaccions(Connection conn) throws SQLException {
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO faccio (nom, resum) VALUES (?, ?)","cavallers","Though seen as a single group, the Knights are hardly unified. There are many Legions in Ashfeld, the most prominent being The Iron Legion.");
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO faccio (nom, resum) VALUES (?, ?)","vikings","The vikings are a loose coalition of hundreds of clans and tribes, the most powerful being The Warborn.");
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO faccio (nom, resum) VALUES (?, ?)","samurai","The samurai are the most unified of the five factions, though this does not say much as the Daimyos were often battling each other for dominance.");
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO faccio (nom, resum) VALUES (?, ?)","wu lin","When the Cataclysm hit, China was not prepared for the civil war that would break out. A number of warriors ventured West.");
    }

    public static void popularPersonatges(Connection conn) throws SQLException {
        // cavallers
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Warden", "20", "50", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Conqueror", "15", "65", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Peacekeeper", "18", "40", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Lawbringer", "25", "60", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Centurion", "22", "50", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Gladiator", "18", "45", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Black Prior", "20", "55", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Warmonger", "22", "60", 1);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Gryphon", "24", "55", 1);

        // vikings
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Raider", "17", "60", 2);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Warlord", "15", "70", 2);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Berserker", "20", "50", 2);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Valkyrie", "16", "65", 2);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Highlander", "18", "60", 2);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Shaman", "19", "55", 2);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Jormungandr", "21", "50", 2);

        // samurai
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Kensei", "18", "62", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Shugoki", "16", "75", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Orochi", "21", "50", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Nobushi", "17", "58", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Shinobi", "22", "48", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Aramusha", "19", "60", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Hitokiri", "20", "65", 3);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Kyoshin", "18", "63", 3);

        // WU LIN
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Tiandi", "20", "60", 4);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Jiang Jun", "17", "70", 4);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Nuxia", "23", "45", 4);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Shaolin", "19", "62", 4);
        UtilsSQLite.queryUpdatePS(conn, "INSERT INTO personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)", "Zhanhu", "18", "65", 4);
    }
}

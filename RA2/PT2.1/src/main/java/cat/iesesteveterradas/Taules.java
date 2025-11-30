package cat.iesesteveterradas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Taules {

    private static final Logger logger = LoggerFactory.getLogger(Taules.class);

    public static void dibuixarTaula(Connection conn,String nom) {
        String query = "";
        try {
            List<String> taules = UtilsSQLite.listTables(conn);
            if (!taules.contains(nom)) {
            logger.error("La taula " + nom + " no existeix");
                
                return;
            }


            if (nom.equalsIgnoreCase("personatge")) {
                query = "SELECT p.nom, p.atac, p.defensa, f.nom AS faccio " +
                        "FROM personatge p " +
                        "LEFT JOIN faccio f ON p.idFaccio = f.id";
            } else {
                query = "SELECT * FROM " + nom;
            }
            try (ResultSet rs = UtilsSQLite.querySelect(conn, query)) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                List<String> capçaleres = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    capçaleres.add(meta.getColumnName(i));
                }

                List<List<String>> dades = new ArrayList<>();
                while (rs.next()) {
                    List<String> fila = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        fila.add(rs.getString(i));
                    }
                    dades.add(fila);
                }
               logger.info("Mostrant taula: {}",nom);

                AsciiTablePrinter.imprimirTaula(capçaleres, dades);
            }

        } catch (Exception e) {
            logger.error("S'ha produït un error en operar amb la base de dades.", e);
            e.printStackTrace();
        }
    }



    public static void mostrarPersonatgesPerFaccio(Connection conn, String nomFaccio) {

        String query = "SELECT p.nom, p.atac, p.defensa " +
                    "FROM personatge p " +
                    "LEFT JOIN faccio f ON p.idFaccio = f.id " +
                    "WHERE f.nom = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomFaccio.toLowerCase()); 

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                List<String> capçaleres = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    capçaleres.add(meta.getColumnName(i));
                }

                List<List<String>> dades = new ArrayList<>();
                while (rs.next()) {
                    List<String> fila = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        fila.add(rs.getString(i));
                    }
                    dades.add(fila);
                }
                logger.info("Mostrant peronatges de: {}",nomFaccio);

                AsciiTablePrinter.imprimirTaula(capçaleres, dades);
            }
        } catch (SQLException e) {
            logger.error("S'ha produït un error en operar amb la base de dades.", e);
            e.printStackTrace();
        }
    }

    public static void mostrarMillorAtacantFaccio(Connection conn, String nomFaccio) {
                String query =
                    "SELECT p.nom, p.atac " +
                    "FROM personatge p " +
                    "JOIN faccio f ON p.idFaccio = f.id " +
                    "WHERE f.nom = ? " +
                    "ORDER BY p.atac DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomFaccio.toLowerCase()); 

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                List<String> capçaleres = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    capçaleres.add(meta.getColumnName(i));
                }

                List<List<String>> dades = new ArrayList<>();
                while (rs.next()) {
                    List<String> fila = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        fila.add(rs.getString(i));
                    }
                    dades.add(fila);
                }
                logger.info("Mostrant millor atacant de: {}",nomFaccio);

                AsciiTablePrinter.imprimirTaula(capçaleres, dades);
            }
        } catch (SQLException e) {
            logger.error("S'ha produït un error en operar amb la base de dades.", e);
            e.printStackTrace();
        }
    }

    public static void mostrarMillorDefensorFaccio(Connection conn, String nomFaccio) {
                String query =
                    "SELECT p.nom, p.defensa " +
                    "FROM personatge p " +
                    "JOIN faccio f ON p.idFaccio = f.id " +
                    "WHERE f.nom = ? " +
                    "ORDER BY p.defensa DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomFaccio.toLowerCase()); 

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                List<String> capçaleres = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    capçaleres.add(meta.getColumnName(i));
                }

                List<List<String>> dades = new ArrayList<>();
                while (rs.next()) {
                    List<String> fila = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        fila.add(rs.getString(i));
                    }
                    dades.add(fila);
                }
                logger.info("Mostrant millor defensor de: {}",nomFaccio);

                AsciiTablePrinter.imprimirTaula(capçaleres, dades);
            }
        } catch (SQLException e) {
            logger.error("S'ha produït un error en operar amb la base de dades.", e);
            e.printStackTrace();
        }
    }        
}

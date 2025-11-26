package cat.iesesteveterradas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Taules {

    public static void dibuixarTaula(Connection conn,String nom) {
        try {
            List<String> taules = UtilsSQLite.listTables(conn);
            if (!taules.contains(nom)) {
                System.out.println("La taula " + nom + " no existeix.");
                return;
            }

            String query = "SELECT * FROM " + nom;
;
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

                AsciiTablePrinter.imprimirTaula(capçaleres, dades);
            }

        } catch (Exception e) {
            System.out.println("Error al dibuixar la taula " + nom + ": " + e.getMessage());
            e.printStackTrace();
        }
    }



        public static void mostrarPersonatgesPerFaccio(Connection conn, String nomFaccio) {
            System.out.println("Personatges de la faccio: " + nomFaccio);
            String consulta = "SELECT p.nom, p.atac, p.defensa, f.nom AS faccio " +
                            "FROM personatge p " +
                            "LEFT JOIN faccio f ON p.idFaccio = f.id " +
                            "WHERE f.nom = ?";

            try (PreparedStatement ps = conn.prepareStatement(consulta)) {
                ps.setString(1, nomFaccio);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.println(
                            rs.getString("nom") +
                            " - Atac: " + rs.getInt("atac") +
                            ", Defensa: " + rs.getInt("defensa")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


            public static void mostrarMillorAtacantFaccio(Connection conn, String nomFaccio) {
                String consulta =
                    "SELECT p.nom, p.atac " +
                    "FROM personatge p " +
                    "JOIN faccio f ON p.idFaccio = f.id " +
                    "WHERE f.nom = ? " +
                    "ORDER BY p.atac DESC LIMIT 1";

                try (PreparedStatement ps = conn.prepareStatement(consulta)) {
                    ps.setString(1, nomFaccio);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.println(
                                rs.getString("nom") +
                                " - Atac: " + rs.getInt("atac")
                            );
                        } else {
                            System.out.println("No hi ha personatges per aquesta facció.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
    }

            public static void mostrarMillorDefensorFaccio(Connection conn, String nomFaccio) {
                String consulta =
                    "SELECT p.nom, p.defensa " +
                    "FROM personatge p " +
                    "JOIN faccio f ON p.idFaccio = f.id " +
                    "WHERE f.nom = ? " +
                    "ORDER BY p.defensa DESC LIMIT 1";

                try (PreparedStatement ps = conn.prepareStatement(consulta)) {
                    ps.setString(1, nomFaccio);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.println(
                                rs.getString("nom") +
                                " - Defensa: " + rs.getInt("defensa")
                            );
                        } else {
                            System.out.println("No hi ha personatges per aquesta facció.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


        
}

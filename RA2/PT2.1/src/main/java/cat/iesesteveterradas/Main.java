package cat.iesesteveterradas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Path filePath = obtenirPathFitxer();

        try {
            GestioDB.crearDB(filePath);
            
            try (Connection conn = UtilsSQLite.connect(filePath.toString())) {
                Taules.mostrarPersonatgesPerFaccio(conn,"Cavallers");
                //Taules.dibuixarTaula("personatge", conn);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Path obtenirPathFitxer() {
        return Paths.get("RA2/PT2.1", "data", "forhonor.sqlite");
    }

    public static List<String> readFileContent(Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }
}

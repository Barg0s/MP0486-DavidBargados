package cat.iesesteveterradas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Path filePath = obtenirPathFitxer();

        try {
        GestioDB.crearDB(filePath);
            
        } catch (SQLException e) {
            // TODO: handle exception
        }
    }

    public static Path obtenirPathFitxer() {
        return Paths.get("RA2/PT2.1", "data", "forhonor.sqlite");
    }

    public static List<String> readFileContent(Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }
}

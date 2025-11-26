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
        try {
            Path filePath = obtenirPathFitxer();

            // Crear la BD i omplir-la
            GestioDB.crearDB(filePath);

            try (Connection conn = UtilsSQLite.connect(filePath.toString())) {
                Taules.mostrarPersonatgesPerFaccio(conn, "Cavallers");
                System.out.println("Millor atacant");

                Taules.mostrarMillorAtacantFaccio(conn, "Cavallers");
                System.out.println("Millor Defensor");
                Taules.mostrarMillorDefensorFaccio(conn, "Cavallers");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Path obtenirPathFitxer() throws IOException {
        Path path = Paths.get("RA2", "PT2.1", "data", "forhonor.sqlite");

        // Crear directoris si no existeixen
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        return path;
    }

    public static List<String> readFileContent(Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }
}

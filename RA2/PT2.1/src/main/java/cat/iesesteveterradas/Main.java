package cat.iesesteveterradas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Path path = Paths.get("RA2", "PT2.1", "data", "forhonor.sqlite");
        
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Escull una opcio (1-4)");
            String opcio = scanner.nextLine();
            menu(opcio,path);
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





    public static void menu(String opcio, Path filePath) {
        Scanner scan = new Scanner(System.in);
        String opcio2;

        switch (opcio) {
            case "mostrar taula":
            case "1":
                System.out.print("Introdueix la taula: ");
                opcio2 = scan.nextLine();
                try (Connection conn = UtilsSQLite.connect(filePath.toString())) {
                    Taules.dibuixarTaula(conn, opcio2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "mostrar personatges per faccio":
            case "2":
                System.out.print("Introdueix la facció: ");
                opcio2 = scan.nextLine();
                try (Connection conn = UtilsSQLite.connect(filePath.toString())) {
                    Taules.mostrarPersonatgesPerFaccio(conn, opcio2);                
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "millor atacant per faccio":
            case "3":
                System.out.print("Introdueix la facció: ");
                opcio2 = scan.nextLine();
                try (Connection conn = UtilsSQLite.connect(filePath.toString())) {
                    Taules.mostrarMillorAtacantFaccio(conn, opcio2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "millor defensor per faccio":
            case "4":
                System.out.print("Introdueix la facció: ");
                opcio2 = scan.nextLine();
                try (Connection conn = UtilsSQLite.connect(filePath.toString())) {
                    Taules.mostrarMillorDefensorFaccio(conn, opcio2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "sortir":
            case "0":
                System.out.println("Sortint del programa...");
                break;

            default:
                System.out.println("Opció no vàlida!");
        }
    }

}

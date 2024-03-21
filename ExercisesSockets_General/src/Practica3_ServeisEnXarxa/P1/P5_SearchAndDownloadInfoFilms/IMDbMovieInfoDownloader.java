package Practica3_ServeisEnXarxa.P1.P5_SearchAndDownloadInfoFilms;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class IMDbMovieInfoDownloader {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pedir al usuario que introduzca el título de la película
        System.out.print("Introduce el título de la película: ");
        String movieTitle = scanner.nextLine();

        // Construir la URL de búsqueda en IMDb
        String url = "https://www.imdb.com/find?q=" + URLEncoder.encode(movieTitle, StandardCharsets.UTF_8);

        try {
            // Realizar la solicitud HTTP GET
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Guardar la respuesta en un archivo HTML
            String fileName = movieTitle + ".html";
            String filePath = "E:/DAM/2n DAM/M09/UF3/Practiques senceres_preExamen/Practiques_PreExamen/src/Practica3_ServeisEnXarxa/P1/P5_SearchAndDownloadInfoFilms/Info_Films/" + fileName;
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(response.body());
            printWriter.close();

            System.out.println("La información de la película se ha guardado en el archivo: " + fileName);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error al realizar la solicitud HTTP.");
        }
    }
}

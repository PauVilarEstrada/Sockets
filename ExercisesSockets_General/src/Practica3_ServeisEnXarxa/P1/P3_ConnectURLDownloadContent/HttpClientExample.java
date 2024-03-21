package Practica3_ServeisEnXarxa.P1.P3_ConnectURLDownloadContent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class HttpClientExample {
    public static void main(String[] args) {
        // URL de la página web a la que se desea conectar
        String url = "https://escolagrevol.cat/";

        // Crear un objeto HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Crear un objeto HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la respuesta fue exitosa (código de estado 200)
            if (response.statusCode() == 200) {
                // Obtener el contenido de la respuesta
                String responseBody = response.body();
                System.out.println("Contenido descargado:");
                System.out.println(responseBody);
            } else {
                System.out.println("Error al realizar la solicitud. Código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package Practica3_ServeisEnXarxa.P1.P4_SearchDefinitionRAE;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class RAEHttpClientExample {
    public static void main(String[] args) {
        // URL base de la RAE para buscar definiciones
        String baseURL = "https://dle.rae.es/";
      //  String baseURL = "https://www.wordreference.com/definicion/";
        // Crear un cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Pedir al usuario la palabra a buscar
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la palabra que deseas buscar en la RAE: ");
        String word = scanner.nextLine();

        // Construir la URL de búsqueda
        String searchURL = baseURL + word;

        // Crear la solicitud HTTP GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(searchURL))
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la solicitud fue exitosa (código de estado 200)
            if (response.statusCode() == 200) {
                // Obtener el contenido de la respuesta
                String responseBody = response.body();

                // Buscar la definición en el contenido de la respuesta
                String definition = extractDefinition(responseBody);

                // Mostrar la definición
                if (definition != null) {
                    System.out.println("Definición de '" + word + "':");
                    System.out.println(definition);
                } else {
                    System.out.println("La palabra '" + word + "' no fue encontrada en el diccionario de la RAE.");
                }
            } else {
                System.out.println("Error al realizar la solicitud. Código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para extraer la definición de la página HTML
    private static String extractDefinition(String htmlContent) {
        // Implementa la lógica para extraer la definición de la página HTML
        // En este ejemplo, simplemente se devuelve la primera aparición de la etiqueta <article> como definición
        int start = htmlContent.indexOf("<article");
        int end = htmlContent.indexOf("</article>");
        if (start != -1 && end != -1) {
            return htmlContent.substring(start, end + 10); // Agregar 10 caracteres para incluir la etiqueta de cierre
        } else {
            return null;
        }
    }
}

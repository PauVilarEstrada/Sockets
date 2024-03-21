package Practica3_ServeisEnXarxa.P1.P1_ProgramaMostrarPartesDeURL;

import java.net.URL;
import java.util.Scanner;

public class URLInfo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce una URL: ");
        String urlString = scanner.nextLine();

        try {
            URL url = new URL(urlString);
            System.out.println("Protocolo: " + url.getProtocol());
            System.out.println("Host: " + url.getHost());
            System.out.println("Puerto: " + url.getPort());
            System.out.println("Ruta: " + url.getPath());
            System.out.println("Query: " + url.getQuery());
            System.out.println("Referencia: " + url.getRef());
        } catch (Exception e) {
            System.out.println("Error al analizar la URL: " + e.getMessage());
        }
    }
}

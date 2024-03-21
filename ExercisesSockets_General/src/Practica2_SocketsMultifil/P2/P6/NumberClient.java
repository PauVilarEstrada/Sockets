package Practica2_SocketsMultifil.P2.P6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NumberClient {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost"; // Cambia esto si el servidor está en otra dirección
        final int SERVER_PORT = 12345; // Asegúrate de que coincida con el puerto del servidor

        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Introduce un número (o 'exit' para salir): ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    int number = Integer.parseInt(input);
                    writer.println(number);

                    String response = reader.readLine();
                    System.out.println("Respuesta del servidor: " + response);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, introduce un número válido.");
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

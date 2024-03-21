package Practica2_SocketsMultifil.P2.P7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NumberGuessClient {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost"; // Cambia esto si el servidor está en otra dirección
        final int SERVER_PORT = 12345; // Asegúrate de que coincida con el puerto del servidor

        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.print("Intenta adivinar el número (1-20): ");
            int guessedNumber = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea pendiente

            writer.println(guessedNumber);

            String response = reader.readLine();
            System.out.println("Respuesta del servidor: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

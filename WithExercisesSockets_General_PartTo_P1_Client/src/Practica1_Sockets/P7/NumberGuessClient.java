package Practica1_Sockets.P7;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class NumberGuessClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 6000;

    public void playGame() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Adivina un número entre 1 y 20: ");
                int guessedNumber = scanner.nextInt();
                writer.println(guessedNumber);
                String response = reader.readLine();
                System.out.println(response);
                if (response.startsWith("¡Felicidades!")) {
                    break; // Si el cliente ha acertado, termina el juego
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NumberGuessClient client = new NumberGuessClient();
        client.playGame();
    }
}


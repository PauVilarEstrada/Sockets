package Practica1_Sockets.P7;

import java.io.*;
import java.net.*;
import java.util.Random;

public class NumberGuessServer {
    private ServerSocket serverSocket;
    private int randomNumber;
    public static final int PORT = 6000;

    public NumberGuessServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);
            generateRandomNumber();
            while (true) {
                Socket socket = serverSocket.accept();
                handleClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateRandomNumber() {
        Random random = new Random();
        randomNumber = random.nextInt(20) + 1;
        System.out.println("Número a adivinar generado: " + randomNumber);
    }

    private void handleClient(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int guessedNumber = Integer.parseInt(reader.readLine());
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            if (guessedNumber == randomNumber) {
                writer.println("¡Felicidades! Has acertado el número " + randomNumber);
            } else {
                writer.println("Lo siento, el número " + guessedNumber + " no es el correcto. Inténtalo de nuevo.");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NumberGuessServer();
    }
}


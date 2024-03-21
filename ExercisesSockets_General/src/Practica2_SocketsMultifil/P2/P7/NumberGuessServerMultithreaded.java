package Practica2_SocketsMultifil.P2.P7;

import java.io.*;
import java.net.*;
import java.util.Random;

public class NumberGuessServerMultithreaded {
    private ServerSocket serverSocket;
    private int randomNumber;
    public static final int PORT = 12345;

    public NumberGuessServerMultithreaded() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);
            generateRandomNumber();
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
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

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
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
    }

    public static void main(String[] args) {
        new NumberGuessServerMultithreaded();
    }
}

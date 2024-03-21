package P2_Similars.P11_Cortos.P2_AdivinaNumero;

import java.io.*;
import java.net.*;
import java.util.Random;

public class GuessNumberServer {
    private static final int PORT = 6000;
    private static final int MAX_NUMBER = 20;
    private int randomNumber;

    public GuessNumberServer() {
        // Generar un número aleatorio entre 1 y 20
        Random random = new Random();
        randomNumber = random.nextInt(MAX_NUMBER) + 1;
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor del juego Adivina el Número iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                // Leer el número enviado por el cliente
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                int guessedNumber = Integer.parseInt(reader.readLine());

                // Comprobar si el número es correcto
                String response = (guessedNumber == randomNumber) ? "¡Felicidades, has acertado!"
                                                                    : "Lo siento, intenta de nuevo.";
                
                // Enviar la respuesta al cliente
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println(response);

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GuessNumberServer server = new GuessNumberServer();
        server.startServer();
    }
}

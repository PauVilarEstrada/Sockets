package P2_Similars.P11_Cortos.P2_AdivinaNumero;

import java.io.*;
import java.net.*;

public class GuessNumberClient {
    public static final String SERVER_ADDRESS = "localhost";
    static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Solicitar al usuario adivinar el número
            System.out.print("Adivina el número (entre 1 y 20): ");
            int guessedNumber = Integer.parseInt(userInput.readLine());

            // Enviar el número al servidor
            writer.println(guessedNumber);

            // Leer la respuesta del servidor
            String response = reader.readLine();
            System.out.println("Respuesta del servidor: " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


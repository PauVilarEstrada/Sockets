package P2_Similars.P11_Cortos.P1_Acumulador;

import java.io.*;
import java.net.*;

public class AccumulationClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Solicitar al usuario un número
            System.out.print("Ingrese un número: ");
            int number = Integer.parseInt(userInput.readLine());

            // Enviar el número al servidor
            writer.println(number);

            // Leer la respuesta del servidor
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

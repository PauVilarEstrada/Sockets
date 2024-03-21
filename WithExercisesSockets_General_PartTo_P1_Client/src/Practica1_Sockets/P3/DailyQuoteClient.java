package Practica1_Sockets.P3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DailyQuoteClient {
    public static final String SERVER_ADDRESS = "localhost"; // Cambia esto por la dirección IP del servidor si es remoto
    public static final int PORT = 6000;

    public void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedQuote = reader.readLine();
            System.out.println("Frase del día: " + receivedQuote);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DailyQuoteClient client = new DailyQuoteClient();
        client.connectToServer();
    }
}

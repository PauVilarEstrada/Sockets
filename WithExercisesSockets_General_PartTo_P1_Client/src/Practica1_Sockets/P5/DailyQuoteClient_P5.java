package Practica1_Sockets.P5;

import java.io.*;
import java.net.*;

public class DailyQuoteClient_P5 {
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
        DailyQuoteClient_P5 client = new DailyQuoteClient_P5();
        client.connectToServer();
    }
}

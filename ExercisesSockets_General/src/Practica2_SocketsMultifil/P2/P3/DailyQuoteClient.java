package Practica2_SocketsMultifil.P2.P3;

import java.io.*;
import java.net.Socket;

public class DailyQuoteClient {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 12345;

    public void requestQuote() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String quote = reader.readLine();
            System.out.println("Frase del día: " + quote);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DailyQuoteClient client = new DailyQuoteClient();
        client.requestQuote();
    }
}

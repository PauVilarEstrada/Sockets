package Practica2_SocketsMultifil.P2.P1_P2;

import java.io.*;
import java.net.*;

public class WelcomeClient {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 12345;

    public void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WelcomeClient client = new WelcomeClient();
        client.connectToServer();
    }
}

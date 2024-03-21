package Practica1_Sockets.P1_P2;

import java.io.*;
import java.net.*;

public class WelcomeClient_P2 {
    private Socket socket;
    private InputStream input;

    public void connectToServer(String address, int port) {
        try {
            socket = new Socket(address, port);
            input = socket.getInputStream();
            readWelcomeMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readWelcomeMessage() {
        try {
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String message = bufferedReader.readLine();
            System.out.println("Mensaje del servidor:");
            System.out.println(message);
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WelcomeClient_P2 client = new WelcomeClient_P2();
        client.connectToServer("localhost", 6000); // Aquí especifica la dirección IP y el puerto del servidor
    }
}


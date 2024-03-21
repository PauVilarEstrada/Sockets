package Practica1_Sockets.P6;
import java.io.*;
import java.net.*;
import java.util.Random;

public class NumberClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 6000;

    public void connectToServer() {
        try {
            Random random = new Random();
            int numberToSend = random.nextInt(50) + 1; // Genera un número aleatorio entre 1 y 50
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            sendNumber(socket, numberToSend);
            String response = readResponse(socket);
            System.out.println(response);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNumber(Socket socket, int numberToSend) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(numberToSend);
    }

    private String readResponse(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader.readLine();
    }

    public static void main(String[] args) {
        NumberClient client = new NumberClient();
        client.connectToServer();
    }
}

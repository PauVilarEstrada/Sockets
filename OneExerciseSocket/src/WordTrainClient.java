import java.io.*;
import java.net.*;
import java.util.Random;

public class WordTrainClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 6000;

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            String clientName = "Client" + i;
            Thread clientThread = new Thread(new ClientRunnable(clientName));
            clientThread.start();
        }
    }

    static class ClientRunnable implements Runnable {
        private String clientName;

        public ClientRunnable(String clientName) {
            this.clientName = clientName;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                System.out.println(clientName + " conectado al servidor en " + SERVER_ADDRESS + ":" + PORT);

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                writer.println(clientName);

                String train = reader.readLine();
                System.out.println(clientName + " - Tren de palabras: " + train);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

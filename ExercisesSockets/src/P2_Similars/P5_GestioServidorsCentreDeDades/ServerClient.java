package P13_Similars.P5_GestioServidorsCentreDeDades;

import java.io.*;
import java.net.*;

public class ServerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6000;

    public void sendDataToServer(String data) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Enviar datos de monitoreo al servidor
            writer.println(data);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerClient client = new ServerClient();

        // Simulación de datos de monitoreo del servidor
        String serverData = "Server1, CPU: 80%, RAM: 60%, Disk: 70%";
        client.sendDataToServer(serverData);
    }
}

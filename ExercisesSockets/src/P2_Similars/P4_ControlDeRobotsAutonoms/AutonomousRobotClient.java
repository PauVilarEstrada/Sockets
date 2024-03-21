package P13_Similars.P4_ControlDeRobotsAutonoms;

import java.io.*;
import java.net.*;

public class AutonomousRobotClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6000;

    public void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Enviar mensajes al servidor
            writer.println("INITIALIZE"); // Envía un mensaje de inicialización al servidor

            // Escuchar respuestas del servidor
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("Respuesta del servidor: " + response);
                // Aquí puedes manejar las respuestas del servidor según las instrucciones enviadas
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AutonomousRobotClient client = new AutonomousRobotClient();
        client.connectToServer();
    }
}

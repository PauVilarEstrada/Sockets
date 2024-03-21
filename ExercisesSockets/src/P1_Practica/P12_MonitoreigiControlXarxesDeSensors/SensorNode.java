package P12_MonitoreigiControlXarxesDeSensors;

import java.io.*;
import java.net.*;

public class SensorNode {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 5000; // Puerto del servidor central

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Envía datos de sensor al servidor central
            String sensorData = "Datos del sensor...";
            writer.println(sensorData);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package P12_MonitoreigiControlXarxesDeSensors;

import java.io.*;
import java.net.*;

public class SensorClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 5000; // Puerto del servidor central

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Lee datos del servidor central
            String sensorData;
            while ((sensorData = reader.readLine()) != null) {
                System.out.println("Datos recibidos del servidor central: " + sensorData);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package P13_Similars.P2_MonitoreigDeTraficEnTempsReal;

import java.io.*;
import java.net.*;

public class SensorTrafficClient {
    public static final String SERVER_ADDRESS = "localhost"; // Cambia esto si el servidor está en otra dirección
    public static final int SERVER_PORT = 6000;

    public void sendTrafficData(String sensorName, int velocidad, int densidad) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Enviar el nombre del sensor
            writer.println(sensorName);

            // Enviar los datos del tráfico al servidor
            writer.println(velocidad + "," + densidad);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SensorTrafficClient client = new SensorTrafficClient();

        // Simulamos datos del tráfico
        String sensorName = "Sensor1";
        int velocidad = 60; // km/h
        int densidad = 30; // vehículos/km

        // Enviamos los datos del tráfico al servidor
        client.sendTrafficData(sensorName, velocidad, densidad);

        try {
            Thread.sleep(1000); // Esperar 1 segundo antes de finalizar la ejecución
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

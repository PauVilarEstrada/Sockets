package P12_MonitoreigiControlXarxesDeSensors;

import java.io.*;
import java.net.*;
import java.util.*;

public class SensorServer {
    private ServerSocket serverSocket;
    private List<SensorNodeHandler> sensorNodes;

    public SensorServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            sensorNodes = new ArrayList<>();
            System.out.println("Servidor central iniciado en el puerto " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                SensorNodeHandler handler = new SensorNodeHandler(clientSocket);
                sensorNodes.add(handler);
                new Thread(handler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SensorNodeHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;

        public SensorNodeHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String sensorData;
                while ((sensorData = reader.readLine()) != null) {
                    System.out.println("Datos recibidos de un nodo de sensor: " + sensorData);
                    // Aquí puedes procesar los datos recibidos según sea necesario
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SensorServer server = new SensorServer(5000); // Puerto 5000 para la conexión de los nodos de sensor
        server.start();
    }
}

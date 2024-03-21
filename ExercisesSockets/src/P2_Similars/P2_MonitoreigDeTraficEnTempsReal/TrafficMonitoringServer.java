package P13_Similars.P2_MonitoreigDeTraficEnTempsReal;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TrafficMonitoringServer {
    public static final int PORT = 6000;
    private ConcurrentHashMap<String, TrafficData> trafficDataMap = new ConcurrentHashMap<>();
    private List<Socket> sensorSockets = new ArrayList<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de monitoreo de tráfico iniciado en el puerto: " + PORT);

            while (true) {
                Socket sensorSocket = serverSocket.accept();
                sensorSockets.add(sensorSocket); // Añadir el nuevo socket a la lista
                System.out.println("Sensor de tráfico conectado desde " + sensorSocket.getInetAddress().getHostAddress());

                // Iniciar un nuevo hilo para manejar la conexión con el sensor de tráfico
                new Thread(new SensorHandler(sensorSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SensorHandler implements Runnable {
        private Socket sensorSocket;
        private String sensorName;

        public SensorHandler(Socket sensorSocket) {
            this.sensorSocket = sensorSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(sensorSocket.getInputStream()));

                // Leer el nombre del sensor
                sensorName = reader.readLine();

                // Leer los datos del sensor (flujo de vehículos)
                String trafficInfo = reader.readLine();
                String[] data = trafficInfo.split(",");
                int velocidad = Integer.parseInt(data[0]);
                int densidad = Integer.parseInt(data[1]);

                // Actualizar los datos de tráfico en el mapa
                trafficDataMap.put(sensorName, new TrafficData(velocidad, densidad));

                // Calcular la velocidad promedio y la densidad total del tráfico
                int totalVelocidad = 0;
                int totalDensidad = 0;
                for (TrafficData trafficData : trafficDataMap.values()) {
                    totalVelocidad += trafficData.getVelocidad();
                    totalDensidad += trafficData.getDensidad();
                }
                int velocidadPromedio = totalVelocidad / trafficDataMap.size();
                int densidadTotal = totalDensidad;

                // Enviar datos actualizados al cliente
                PrintWriter writer = new PrintWriter(sensorSocket.getOutputStream(), true);
                writer.println("Velocidad promedio: " + velocidadPromedio);
                writer.println("Densidad total de tráfico: " + densidadTotal);

                sensorSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TrafficMonitoringServer server = new TrafficMonitoringServer();
        server.startServer();
    }
}

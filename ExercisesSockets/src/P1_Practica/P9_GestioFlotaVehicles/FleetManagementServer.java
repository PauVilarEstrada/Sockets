package P9_GestioFlotaVehicles;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class FleetManagementServer {
    public static final int PORT = 8000;
    private HashMap<String, Vehicle> vehicleMap;

    public FleetManagementServer() {
        vehicleMap = new HashMap<>();
        // Agregamos algunos vehículos inventados para demostración
        vehicleMap.put("Car1", new Vehicle("Car1", "Car", 40, 42.8762, -8.5449, "Running"));
        vehicleMap.put("Car2", new Vehicle("Car2", "Car", 35, 42.8629, -8.5395, "Stopped"));
        vehicleMap.put("Car3", new Vehicle("Car3", "Car", 50, 42.8708, -8.5120, "Idle"));
        vehicleMap.put("Bike1", new Vehicle("Bike1", "Motorcycle", 60, 42.8750, -8.5360, "Running"));
        vehicleMap.put("Bike2", new Vehicle("Bike2", "Motorcycle", 55, 42.8700, -8.5280, "Stopped"));
        vehicleMap.put("Boat1", new Vehicle("Boat1", "Boat", 25, 42.8888, -8.5500, "Running"));
        vehicleMap.put("Boat2", new Vehicle("Boat2", "Boat", 20, 42.8866, -8.5400, "Idle"));

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para manejar la solicitud de actualización de posición de un vehículo
    public synchronized void updateVehiclePosition(String vehicleID, double latitude, double longitude) {
        if (vehicleMap.containsKey(vehicleID)) {
            vehicleMap.get(vehicleID).setLatitude(latitude);
            vehicleMap.get(vehicleID).setLongitude(longitude);
            System.out.println("Updated position of " + vehicleID + ": Latitude=" + latitude + ", Longitude=" + longitude);
        }
    }

    // Método para manejar la solicitud de cambio de estado de un vehículo
    public synchronized void updateVehicleStatus(String vehicleID, String status) {
        if (vehicleMap.containsKey(vehicleID)) {
            vehicleMap.get(vehicleID).setStatus(status);
            System.out.println("Updated status of " + vehicleID + ": " + status);
        }
    }

    // Método para obtener información sobre todos los vehículos
    public synchronized String getFleetInfo() {
        StringBuilder info = new StringBuilder();
        for (Vehicle vehicle : vehicleMap.values()) {
            info.append(vehicle.toString()).append("\n");
        }
        return info.toString();
    }

    // Método principal
    public static void main(String[] args) {
        new FleetManagementServer();
    }

    // Clase interna para manejar las solicitudes de clientes en hilos separados
    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String request;
                while ((request = reader.readLine()) != null) {
                    String[] tokens = request.split(",");
                    if (tokens.length == 4 && tokens[0].equals("POSITION")) {
                        String vehicleID = tokens[1];
                        double latitude = Double.parseDouble(tokens[2]);
                        double longitude = Double.parseDouble(tokens[3]);
                        updateVehiclePosition(vehicleID, latitude, longitude);
                    } else if (tokens.length == 3 && tokens[0].equals("STATUS")) {
                        String vehicleID = tokens[1];
                        String status = tokens[2];
                        updateVehicleStatus(vehicleID, status);
                    } else if (tokens.length == 2 && tokens[0].equals("INFO")) {
                        String vehicleID = tokens[1];
                        Vehicle vehicle = vehicleMap.get(vehicleID);
                        if (vehicle != null) {
                            writer.println(vehicle.toString());
                        } else {
                            writer.println("Vehicle with ID " + vehicleID + " not found.");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

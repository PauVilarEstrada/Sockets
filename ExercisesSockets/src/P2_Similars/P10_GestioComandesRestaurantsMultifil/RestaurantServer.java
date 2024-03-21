package P2_Similars.P10_GestioComandesRestaurantsMultifil;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class RestaurantServer {
    private static final int PORT = 6000;
    private ConcurrentHashMap<Integer, String> orderStatusMap = new ConcurrentHashMap<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor del restaurante iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                // Iniciar un nuevo hilo para manejar la conexión con el cliente
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer el número de mesa del cliente
                int tableNumber = Integer.parseInt(reader.readLine());

                // Verificar si el cliente ya ha realizado un pedido anteriormente
                if (orderStatusMap.containsKey(tableNumber)) {
                    // Si el cliente ya ha realizado un pedido, enviar el estado actual del pedido
                    writer.println("Número de mesa: " + tableNumber);
                    writer.println("Estado actual del pedido: " + orderStatusMap.get(tableNumber));
                } else {
                    // Si es la primera vez que el cliente realiza un pedido, solicitar el pedido
                    writer.println("Número de mesa: " + tableNumber);
                    writer.println("Por favor, realice su pedido:");
                    String order = reader.readLine();
                    String confirmation = processOrder(order);
                    orderStatusMap.put(tableNumber, confirmation); // Actualizar el estado del pedido
                    writer.println("Confirmación del pedido: " + confirmation);
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processOrder(String order) {
            // Estados posibles del pedido
            String[] orderStatus = {"Pedido en tránsito", "Pedido no atendido", "Pedido en cocina", "Pedido en rider", "Pedido en cola"};

            // Generar un estado aleatorio del pedido
            Random random = new Random();
            int index = random.nextInt(orderStatus.length);

            return orderStatus[index] + ": " + order;
        }
    }

    public static void main(String[] args) {
        RestaurantServer server = new RestaurantServer();
        server.startServer();
    }
}

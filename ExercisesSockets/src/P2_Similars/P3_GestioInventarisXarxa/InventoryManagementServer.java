package P13_Similars.P3_GestioInventarisXarxa;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class InventoryManagementServer {
    public static final int PORT = 6000;
    private ConcurrentHashMap<String, Integer> inventory = new ConcurrentHashMap<>();

    public InventoryManagementServer() {
        // Inicializar el inventario con algunos productos y sus stocks
        inventory.put("Producto1", 50);
        inventory.put("Producto2", 30);
        inventory.put("Producto3", 10);
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de inventarios iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexión aceptada desde " + clientSocket.getInetAddress().getHostAddress());

                // Iniciar un nuevo hilo para manejar la conexión con el cliente
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer la operación del cliente
                String operation = reader.readLine();

                // Procesar la operación del cliente
                if (operation.equals("REQUEST_STOCK")) {
                    // Manejar solicitud de stock
                    String product = reader.readLine();
                    int stock = inventory.getOrDefault(product, 0);
                    writer.println(stock);
                } else if (operation.equals("UPDATE_STOCK")) {
                    // Manejar actualización de stock
                    String product = reader.readLine();
                    int quantity = Integer.parseInt(reader.readLine());
                    int currentStock = inventory.getOrDefault(product, 0);
                    int updatedStock = currentStock - quantity;
                    inventory.put(product, updatedStock);
                    writer.println("Stock actualizado correctamente.");
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        InventoryManagementServer server = new InventoryManagementServer();
        server.startServer();
    }
}

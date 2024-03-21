package P13_Similars.P6_GestioInventariMultifil;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryServer {
    private static final int PORT = 5000;
    private static final int INITIAL_STOCK = 100;
    private static final Map<String, AtomicInteger> inventory = new HashMap<>();

    public static void main(String[] args) {
        initializeInventory();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto " + PORT);

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

    private static void initializeInventory() {
        inventory.put("producto1", new AtomicInteger(INITIAL_STOCK));
        inventory.put("producto2", new AtomicInteger(INITIAL_STOCK));
        inventory.put("producto3", new AtomicInteger(INITIAL_STOCK));
    }

    private static class ClientHandler implements Runnable {
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

                // Leer la solicitud del cliente y procesarla
                String request;
                while ((request = reader.readLine()) != null) {
                    processRequest(request);
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void processRequest(String request) {

            String[] data = request.trim().split(",");
            if (data.length >= 2) {
                String productName = data[0];
                int quantity = Integer.parseInt(data[1].trim());

                // Verificar si el producto existe en el inventario
                if (inventory.containsKey(productName)) {
                    AtomicInteger stock = inventory.get(productName);

                    // Sincronizar el acceso al inventario para evitar condiciones de carrera
                    synchronized (stock) {
                        // Realizar la operación de agregar o restar al inventario
                        if (quantity < 0 && stock.get() < Math.abs(quantity)) {
                            writer.println("No hay suficiente stock para realizar la operación.");
                            return;
                        }
                        stock.addAndGet(quantity);

                        // Enviar la respuesta al cliente con el número de cliente y el nuevo stock
                        writer.println("Operación completada para el producto " + productName +
                                ". Nuevo stock: " + stock.get());
                    }
                } else {
                    writer.println("El producto " + productName + " no existe en el inventario.");
                }
            }
        }
    }
}

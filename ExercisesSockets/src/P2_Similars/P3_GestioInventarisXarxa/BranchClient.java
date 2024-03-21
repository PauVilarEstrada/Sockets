package P13_Similars.P3_GestioInventarisXarxa;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BranchClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT = 6000;

    public void requestStock(String product) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Enviar la operación y el nombre del producto al servidor
            writer.println("REQUEST_STOCK");
            writer.println(product);

            // Leer la respuesta del servidor (stock antes de la actualización)
            String initialStock = reader.readLine();
            System.out.println("Stock de " + product + " antes de la actualización: " + initialStock);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateStock(String product, int quantity) {
        try {
            // Solicitar y mostrar el stock antes de la actualización
            requestStock(product);

            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Enviar la operación, el nombre del producto y la cantidad al servidor
            writer.println("UPDATE_STOCK");
            writer.println(product);
            writer.println(quantity);

            // Leer la respuesta del servidor
            String response = reader.readLine();
            System.out.println(response);

            // Solicitar el stock después de la actualización
            requestStock(product);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BranchClient client = new BranchClient();
        Scanner scanner = new Scanner(System.in);

        // Solicitar al usuario los productos y cantidades deseadas
        System.out.print("Producto 1 - Cantidad: ");
        int quantity1 = scanner.nextInt();
        System.out.print("Producto 2 - Cantidad: ");
        int quantity2 = scanner.nextInt();
        System.out.print("Producto 3 - Cantidad: ");
        int quantity3 = scanner.nextInt();

        // Actualizar el stock en el servidor para cada producto
        client.updateStock("Producto1", quantity1);
        client.updateStock("Producto2", quantity2);
        client.updateStock("Producto3", quantity3);
    }
}

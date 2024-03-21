package P2_Similars.P10_GestioComandesRestaurantsMultifil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RestaurantClient {
    public static final String SERVER_ADDRESS = "localhost"; // Dirección del servidor
    public static final int SERVER_PORT = 6000; // Puerto del servidor

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Solicitar al usuario el número de mesa
            System.out.print("Ingrese el número de mesa: ");
            String tableNumber = userInput.readLine();

            // Solicitar al usuario el pedido
            System.out.println("Seleccione su pedido:");
            System.out.println("1. Pizza");
            System.out.println("2. Hamburguesa");
            System.out.println("3. Ensalada");
            System.out.print("Ingrese el número del pedido: ");
            int orderChoice = Integer.parseInt(userInput.readLine());

            String order;
            switch (orderChoice) {
                case 1:
                    order = "Pizza";
                    break;
                case 2:
                    order = "Hamburguesa";
                    break;
                case 3:
                    order = "Ensalada";
                    break;
                default:
                    System.out.println("Pedido no válido.");
                    socket.close();
                    return;
            }

            // Enviar el pedido al servidor
            writer.println(tableNumber);
            writer.println(order);

            // Leer la respuesta del servidor
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("Respuesta del servidor: " + response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

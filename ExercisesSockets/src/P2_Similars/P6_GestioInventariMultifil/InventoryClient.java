package P13_Similars.P6_GestioInventariMultifil;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class InventoryClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Ingrese el nombre del producto y la cantidad (producto,cantidad): ");
                String request = scanner.nextLine();

                if ("salir".equals(request)) {
                    break;
                }

                // Enviar la solicitud al servidor
                writer.println(request);

                // Recibir la respuesta del servidor
                String response = reader.readLine();
                System.out.println("Respuesta del servidor: " + response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

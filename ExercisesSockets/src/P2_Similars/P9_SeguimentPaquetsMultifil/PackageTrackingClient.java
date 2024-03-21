package P2_Similars.P9_SeguimentPaquetsMultifil;// PackageTrackingClient.java
import java.io.*;
import java.net.*;

public class PackageTrackingClient {
    public static final String SERVER_ADDRESS = "localhost"; // Dirección del servidor
    public static final int SERVER_PORT = 6000; // Puerto del servidor

    public static void main(String[] args) {
        try {
            // Establecer conexión con el servidor
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            // Configurar flujos de entrada y salida
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Solicitar al usuario el número de seguimiento del paquete
            System.out.print("Ingrese el número de seguimiento del paquete: ");
            String trackingNumber = userInput.readLine();

            // Enviar el número de seguimiento del paquete al servidor
            writer.println(trackingNumber);

            // Leer la respuesta del servidor
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }

            // Cerrar la conexión con el servidor
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

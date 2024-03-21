package P2_Similars.P7_ReservesHabitacionsHotelMultifil;

import java.io.*;
import java.net.*;

public class HotelReservationClient {
    public static final String SERVER_ADDRESS = "localhost"; // Dirección del servidor
    public static final int SERVER_PORT = 5000; // Puerto del servidor

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Solicitar al usuario el número de habitación que desea reservar
            System.out.print("Ingrese el número de habitación que desea reservar: ");
            int roomNumber = Integer.parseInt(userInput.readLine());

            // Enviar la solicitud al servidor
            writer.println(roomNumber);

            // Leer la respuesta del servidor
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

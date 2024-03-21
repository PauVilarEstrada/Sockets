package P13_Similars.P7_ReservesHabitacionsHotelMultifil;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HotelReservationServer {
    private static final int PORT = 5000;
    private static final int MAX_ROOMS = 10;
    private static final AtomicInteger clientCounter = new AtomicInteger(1);
    private static final ConcurrentHashMap<Integer, Integer> roomAvailability = new ConcurrentHashMap<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de reservas de hotel iniciado en el puerto: " + PORT);

            // Inicializar la disponibilidad de las habitaciones
            for (int i = 1; i <= MAX_ROOMS; i++) {
                roomAvailability.put(i, 1);
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                // Iniciar un nuevo hilo para manejar la solicitud del cliente
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
        private int clientId;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.clientId = clientCounter.getAndIncrement();
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer la solicitud del cliente
                String request = reader.readLine();
                int roomNumber = Integer.parseInt(request);

                // Verificar la disponibilidad de la habitación
                if (roomAvailability.containsKey(roomNumber) && roomAvailability.get(roomNumber) > 0) {
                    // Reservar la habitación
                    roomAvailability.put(roomNumber, roomAvailability.get(roomNumber) - 1);
                    writer.println("¡Reserva exitosa! Detalles de la reserva:");
                    writer.println("Número de cliente: " + clientId);
                    writer.println("Número de habitación: " + roomNumber);
                } else {
                    // Habitación no disponible
                    writer.println("Lo sentimos, la habitación " + roomNumber + " no está disponible.");
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        HotelReservationServer server = new HotelReservationServer();
        server.startServer();
    }
}

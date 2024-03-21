package P2_Similars.P9_SeguimentPaquetsMultifil;// PackageTrackingServer.java
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class PackageTrackingServer {
    private static final int PORT = 6000;
    private static final String EXTERNAL_TRACKING_API = "https://example.com/api/track?package=";

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de seguimiento de paquetes iniciado en el puerto: " + PORT);

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

                // Leer el número de seguimiento del paquete del cliente
                String trackingNumber = reader.readLine();

                // Simular el seguimiento del paquete consultando una API externa
                String packageStatus = trackPackage(trackingNumber);

                // Enviar la respuesta al cliente
                writer.println("Número de cliente: " + Thread.currentThread().getId());
                writer.println("Estado del paquete (" + trackingNumber + "): " + packageStatus);

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String trackPackage(String trackingNumber) {
            // Aquí iría la lógica para consultar la API externa y obtener el estado del paquete
            // Por ahora, simplemente retornamos un estado aleatorio para fines de demostración
            String[] statuses = {"En tránsito", "Entregado", "En espera"};
            int randomIndex = (int) (Math.random() * statuses.length);
            return statuses[randomIndex];
        }
    }

    public static void main(String[] args) {
        PackageTrackingServer server = new PackageTrackingServer();
        server.startServer();
    }
}

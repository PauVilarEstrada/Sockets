package P2_Similars.P11_Cortos.P1_Acumulador;

import java.io.*;
import java.net.*;

public class AccumulationServer {
    private static final int PORT = 6000;
    private int totalSum = 0;

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de acumulación iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                // Obtener el número enviado por el cliente y sumarlo al total
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                int number = Integer.parseInt(reader.readLine());
                totalSum += number;

                // Enviar el número de cliente y la suma total al cliente
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println("Número de cliente: " + clientSocket.getLocalPort());
                writer.println("Suma total acumulada: " + totalSum);

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AccumulationServer server = new AccumulationServer();
        server.startServer();
    }
}

package P13_Similars.P1_FacturacioMultifil;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FacturacionServer {
    public static final int PORT = 5000;
    private AtomicInteger totalVentas = new AtomicInteger(0);

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de facturación iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Caja registradora conectada desde " + clientSocket.getInetAddress().getHostAddress());

                // Iniciar un nuevo hilo para manejar la conexión con la caja registradora
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private int ventasCaja;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                // Leer el monto total de ventas de la caja registradora
                ventasCaja = Integer.parseInt(reader.readLine());

                // Actualizar el total de ventas acumuladas en el servidor
                totalVentas.addAndGet(ventasCaja);

                // Enviar el número de caja y la suma total acumulada de ventas al cliente
                writer.println("Número de caja: " + Thread.currentThread().getId());
                writer.println("Ventas acumuladas hasta el momento: " + totalVentas);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FacturacionServer server = new FacturacionServer();
        server.startServer();
    }
}

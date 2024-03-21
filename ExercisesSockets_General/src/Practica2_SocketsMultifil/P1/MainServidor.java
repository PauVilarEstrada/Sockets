package Practica2_SocketsMultifil.P1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class MainServidor {
    private static AtomicInteger clienteCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(65000);
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clienteId = clienteCounter.incrementAndGet();
                System.out.println("Cliente " + clienteId + " conectado desde " + clientSocket.getRemoteSocketAddress());

                // Creamos un hilo para manejar la conexión con el cliente
                Thread clienteThread = new Thread(new ClienteHandler(clientSocket, clienteId));
                clienteThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error iniciando el servidor: " + e.getMessage());
        }
    }
}

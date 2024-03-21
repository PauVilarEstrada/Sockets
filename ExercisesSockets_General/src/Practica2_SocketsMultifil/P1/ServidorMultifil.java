package Practica2_SocketsMultifil.P1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServidorMultifil {
    private static AtomicInteger clienteCounter = new AtomicInteger(0);

    public void inicia() throws IOException {
        ServerSocket serverSocket = new ServerSocket(65000);
        System.out.println("Servidor iniciat. Esperant connexions...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            int clienteId = clienteCounter.incrementAndGet();
            System.out.println("Client " + clienteId + " connectat des de " + clientSocket.getRemoteSocketAddress());

            // Enviamos mensaje de bienvenida con el número de cliente
            new Thread(new ClienteHandler(clientSocket, clienteId)).start();
        }
    }
}

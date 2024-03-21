package Practica1_Sockets.P6;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberServer {
    private ServerSocket serverSocket;
    private AtomicInteger accumulator = new AtomicInteger(0);
    private int clientCount = 0;

    public NumberServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                clientCount++;
                handleClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try {
            int receivedNumber = readNumber(socket);
            accumulator.addAndGet(receivedNumber);
            sendResponse(socket, receivedNumber);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int readNumber(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return Integer.parseInt(reader.readLine());
    }

    private void sendResponse(Socket socket, int receivedNumber) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("Cliente #" + clientCount + ": Número recibido = " + receivedNumber + ", Suma acumulada = " + accumulator.get());
    }

    public static void main(String[] args) {
        int port = 6000; // Puedes cambiar el puerto si es necesario
        new NumberServer(port);
    }
}

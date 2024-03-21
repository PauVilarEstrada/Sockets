package Practica2_SocketsMultifil.P2.P6;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberServerMultithreaded {
    private ServerSocket serverSocket;
    private AtomicInteger accumulator = new AtomicInteger(0);
    private AtomicInteger clientCount = new AtomicInteger(0);

    public NumberServerMultithreaded(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                int receivedNumber = readNumber(socket);
                int currentCount = clientCount.incrementAndGet();
                accumulator.addAndGet(receivedNumber);
                sendResponse(socket, receivedNumber, currentCount);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int readNumber(Socket socket) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return Integer.parseInt(reader.readLine());
        }

        private void sendResponse(Socket socket, int receivedNumber, int currentCount) throws IOException {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("Cliente #" + currentCount + ": Número recibido = " + receivedNumber + ", Suma acumulada = " + accumulator.get());
        }
    }

    public static void main(String[] args) {
        int port = 12345; // Puedes cambiar el puerto si es necesario
        new NumberServerMultithreaded(port);
    }
}

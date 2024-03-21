package P1_Practica.P1_ServidorXatMultifil;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 8000;
    private static final Map<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                writer.println("Enter your username:");
                username = reader.readLine();
                clients.put(username, writer);
                broadcast(username + " has joined the chat.");

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("/msg")) {
                        sendPrivateMessage(line);
                    } else {
                        broadcast(username + ": " + line);
                    }
                }

                // If client disconnects
                clients.remove(username);
                clientSocket.close();
                broadcast(username + " has left the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String message) {
            for (PrintWriter client : clients.values()) {
                client.println(message);
            }
        }

        private void sendPrivateMessage(String line) {
            String[] parts = line.split(" ");
            if (parts.length >= 3) {
                String recipient = parts[1];
                String message = line.substring(parts[0].length() + parts[1].length() + 2);
                PrintWriter recipientWriter = clients.get(recipient);
                if (recipientWriter != null) {
                    recipientWriter.println("(Private from " + username + "): " + message);
                    writer.println("(Private to " + recipient + "): " + message);
                } else {
                    writer.println("User " + recipient + " is not online.");
                }
            }
        }
    }
}

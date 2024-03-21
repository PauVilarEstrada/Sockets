package Practica2_SocketsMultifil.P2.P1_P2;

import java.io.*;
import java.net.*;

public class WelcomeServerMultithreaded_P2 {
    private ServerSocket serverSocket;
    private final String WELCOME_MESSAGE = "Bienvenido al servidor";
    public static final int PORT = 12345;

    public void initService() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servicio iniciado en el puerto: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error iniciando el servicio o aceptando la conexión");
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
                OutputStream output = socket.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(output);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                // Obtener información sobre el servidor
                String serverInfo = "Servidor escuchando en: " + serverSocket.getInetAddress().getHostAddress() + ":" + PORT;
                System.out.println(serverInfo);

                // Obtener información sobre el cliente
                String clientInfo = "Cliente conectado desde: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                System.out.println(clientInfo);

                // Agregar información al mensaje de bienvenida
                String welcomeMessage = WELCOME_MESSAGE + "\n" + serverInfo + "\n" + clientInfo;

                bufferedWriter.write(welcomeMessage);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // Cerrar conexión
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        WelcomeServerMultithreaded_P2 server = new WelcomeServerMultithreaded_P2();
        server.initService();
    }
}

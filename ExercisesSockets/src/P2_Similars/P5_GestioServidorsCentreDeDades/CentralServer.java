package P13_Similars.P5_GestioServidorsCentreDeDades;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class CentralServer {
    private static final int PORT = 6000;
    private ConcurrentHashMap<String, ServerHandler> serverHandlers = new ConcurrentHashMap<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor central iniciado en el puerto: " + PORT);

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("Servidor conectado desde " + connectionSocket.getInetAddress().getHostAddress());

                // Crear un nuevo hilo para manejar la conexión con el servidor
                ServerHandler serverHandler = new ServerHandler(connectionSocket);
                serverHandlers.put(connectionSocket.getInetAddress().getHostAddress(), serverHandler);
                new Thread(serverHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerHandler implements Runnable {
        private Socket serverSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ServerHandler(Socket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                writer = new PrintWriter(serverSocket.getOutputStream(), true);

                // Escuchar continuamente los datos de monitoreo del servidor
                String data;
                while ((data = reader.readLine()) != null) {
                    System.out.println("Datos recibidos del servidor (" + serverSocket.getInetAddress().getHostAddress() + "): " + data);
                    // Aquí puedes procesar los datos recibidos y realizar acciones en función de ellos
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CentralServer server = new CentralServer();
        server.startServer();
    }
}

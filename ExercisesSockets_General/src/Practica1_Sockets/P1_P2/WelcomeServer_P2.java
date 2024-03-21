package Practica1_Sockets.P1_P2;
import java.io.*;
import java.net.*;

public class WelcomeServer_P2 {
    private ServerSocket serverSocket;
    private Socket socket;
    private final String WELCOME_MESSAGE = "Bienvenido al servidor";
    public static final int PORT = 6000;
    private OutputStream output;
    private InputStream input;

    public void initService() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servicio iniciado en el puerto: " + PORT);
            // Bucle infinito para espera de conexiones
            while (true) {
                socket = serverSocket.accept();
                manageNewConnection(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error iniciando el servicio o aceptando la conexión");
        }
    }

    private void manageNewConnection(Socket socket) {
        try {
            output = socket.getOutputStream();
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
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WelcomeServer_P2 server = new WelcomeServer_P2();
        server.initService();
    }
}

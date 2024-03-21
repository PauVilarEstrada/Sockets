package Practica1_Sockets.P8;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class FileServer {
    public static final int PORT = 6000;
    public static final String PUBLIC_DIRECTORY = "E:/DAM/2n DAM/M09/UF3/Practiques senceres_preExamen/Practiques_PreExamen/src/Practica2_SocketsMultifil/P2/P8/Public";

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                sendFileList(clientSocket);
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFileList(Socket socket) throws IOException {
        File directory = new File(PUBLIC_DIRECTORY);
        File[] files = directory.listFiles();

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        if (files != null) {
            Arrays.stream(files)
                    .filter(File::isFile)
                    .map(File::getName)
                    .forEach(writer::println);
        }
        writer.println("END");
    }

    public static void main(String[] args) {
        FileServer server = new FileServer();
        server.startServer();
    }
}

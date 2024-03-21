package Practica2_SocketsMultifil.P2.P8;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class FileServerMultithreaded {
    public static final int PORT = 12345;
    public static final String PUBLIC_DIRECTORY = "E:/DAM/2n DAM/M09/UF3/Practiques senceres_preExamen/Practiques_PreExamen/src/Practica2_SocketsMultifil/P2/P8/Public";

    public FileServerMultithreaded() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket)).start();
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
                sendFileList(socket); // Envía la lista de archivos al cliente
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String requestedFile = reader.readLine();
                sendFile(socket, requestedFile); // Envía el archivo solicitado al cliente
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendFileList(Socket socket) throws IOException {
            File directory = new File(PUBLIC_DIRECTORY);
            File[] files = directory.listFiles();

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        writer.println(file.getName());
                    }
                }
            }
            writer.println("END_FILE_LIST");
        }

        private void sendFile(Socket socket, String fileName) throws IOException {
            File file = new File(PUBLIC_DIRECTORY, fileName);
            if (!file.exists() || !file.isFile()) {
                System.out.println("El archivo solicitado no existe o no es válido: " + fileName);
                return;
            }

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileReader.readLine()) != null) {
                writer.println(line);
            }
            writer.println("END_FILE"); // Marca de finalización del archivo
            fileReader.close();
        }
    }

    public static void main(String[] args) {
        new FileServerMultithreaded();
    }
}

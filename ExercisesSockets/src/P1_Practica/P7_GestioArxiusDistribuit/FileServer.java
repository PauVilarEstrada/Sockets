package P7_GestioArxiusDistribuit;

import java.io.*;
import java.net.*;
import java.util.*;

public class FileServer {
    private static final int PORT = 6000;
    private static final String SERVER_DIRECTORY = "E:/DAM/2n DAM/M09/UF3/Exercicis_Practicar_PreExamen_UF3/src/P7_GestioArxiusDistribuit/ServerArxius";
    private static final String CLIENTS_FILES_DIRECTORY = "E:/DAM/2n DAM/M09/UF3/Exercicis_Practicar_PreExamen_UF3/src/P7_GestioArxiusDistribuit/ServerArxius/ArxiusClientsEnviats";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                writer.println("Ingrese su nombre de usuario:");
                username = reader.readLine();
                System.out.println("Nuevo cliente conectado: " + username);

                // Mostrar archivos disponibles en el servidor
                File serverDirectory = new File(SERVER_DIRECTORY);
                File[] serverFiles = serverDirectory.listFiles();
                if (serverFiles != null) {
                    writer.println("Archivos disponibles en el servidor:");
                    for (File file : serverFiles) {
                        if (file.isFile()) {
                            writer.println(file.getName());
                        }
                    }
                    writer.println("END");
                }

                // Leer archivos recibidos del cliente y guardarlos en el servidor
                String clientFilePath = CLIENTS_FILES_DIRECTORY + "/" + username;
                File clientFilesDirectory = new File(clientFilePath);
                if (!clientFilesDirectory.exists()) {
                    clientFilesDirectory.mkdirs();
                }
                String receivedFileName;
                while ((receivedFileName = reader.readLine()) != null) {
                    if (!receivedFileName.equals("END")) {
                        receiveFile(clientFilePath + "/" + receivedFileName);
                        System.out.println("Archivo recibido del cliente " + username + ": " + receivedFileName);
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void receiveFile(String filePath) throws IOException {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String line;
            while (!(line = reader.readLine()).equals("END")) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
    }
}

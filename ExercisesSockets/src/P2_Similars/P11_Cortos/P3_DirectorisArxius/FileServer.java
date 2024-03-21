package P2_Similars.P11_Cortos.P3_DirectorisArxius;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class FileServer {
    public static final int PORT = 6000;
    public static final String PUBLIC_DIRECTORY = "E:\\DAM\\2n DAM\\M09\\UF3\\Exercicis_Practicar_PreExamen_UF3\\src\\P2_Similars\\P11_Cortos\\P3_DirectorisArxius\\Arxius";
    public static final String SAVED_DIRECTORY = "E:\\DAM\\2n DAM\\M09\\UF3\\Exercicis_Practicar_PreExamen_UF3\\src\\P2_Similars\\P11_Cortos\\P3_DirectorisArxius\\Arxius\\Guardats";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                sendFileList(clientSocket);
                receiveSelectedFile(clientSocket);
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFileList(Socket socket) throws IOException {
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

    private static void receiveSelectedFile(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String selectedFile = reader.readLine();
        File fileToSend = new File(PUBLIC_DIRECTORY + "\\" + selectedFile);

        if (fileToSend.exists() && fileToSend.isFile()) {
            sendFile(socket, fileToSend);
        } else {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("NOT_FOUND");
        }
    }

    private static void sendFile(Socket socket, File file) throws IOException {
        byte[] buffer = new byte[8192];
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = socket.getOutputStream();
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        outputStream.close();
    }
}

package P7_GestioArxiusDistribuit;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 6000;
    private static final String CLIENT_DIRECTORY = "E:/DAM/2n DAM/M09/UF3/Exercicis_Practicar_PreExamen_UF3/src/P7_GestioArxiusDistribuit/ClientsArxius";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine()); // Leer mensaje de ingreso de usuario
            String username = scanner.nextLine();
            writer.println(username); // Enviar nombre de usuario al servidor

            // Mostrar archivos disponibles en el servidor
            String line;
            while (!(line = reader.readLine()).equals("END")) {
                System.out.println(line);
            }

            // Envío de archivos al servidor
            System.out.println("Seleccione archivos para enviar al servidor (separados por comas):");
            String filesToSendInput = scanner.nextLine();
            String[] filesToSend = filesToSendInput.split(",");
            for (String filename : filesToSend) {
                File file = new File(CLIENT_DIRECTORY + "/" + filename.trim());
                if (file.exists()) {
                    sendFile(writer, file);
                    System.out.println("Archivo enviado al servidor: " + file.getName());
                } else {
                    System.out.println("El archivo " + filename + " no existe en la carpeta del cliente.");
                }
            }

            // Descarga de archivos del servidor
            System.out.println("Seleccione archivos para descargar del servidor (separados por comas):");
            String filesToDownloadInput = scanner.nextLine();
            String[] filesToDownload = filesToDownloadInput.split(",");
            for (String filename : filesToDownload) {
                writer.println(filename.trim());
                receiveFile(reader, filename.trim());
                System.out.println("Archivo descargado del servidor: " + filename.trim());
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(PrintWriter writer, File file) throws IOException {
        writer.println(file.getName());
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = fileReader.readLine()) != null) {
            writer.println(line);
        }
        writer.println("END");
        fileReader.close();
    }

    private static void receiveFile(BufferedReader reader, String filename) throws IOException {
        File file = new File(CLIENT_DIRECTORY + "/" + filename);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        String line;
        while (!(line = reader.readLine()).equals("END")) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
}

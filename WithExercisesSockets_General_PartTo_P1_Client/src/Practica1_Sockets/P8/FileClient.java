package Practica1_Sockets.P8;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 6000;
    public static final String DOWNLOAD_DIRECTORY = "descargas";

    public void downloadFile(String fileName) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Envía el nombre del archivo al servidor
            writer.println(fileName);

            // Lee la respuesta del servidor
            String response = reader.readLine();
            if (response.equals("NOT_FOUND")) {
                System.out.println("El archivo no se encontró en el servidor.");
            } else {
                saveFile(reader, fileName);
                System.out.println("Archivo descargado correctamente: " + fileName);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(BufferedReader reader, String fileName) throws IOException {
        File directory = new File(DOWNLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_DIRECTORY + "/" + fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

        String line;
        while (!(line = reader.readLine()).equals("END")) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
    }

    public static void main(String[] args) {
        FileClient client = new FileClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Lista de archivos en el servidor:");
        client.listFiles();

        System.out.print("Introduce el nombre del archivo que deseas descargar: ");
        String fileName = scanner.nextLine();
        client.downloadFile(fileName);
    }

    public void listFiles() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (!(line = reader.readLine()).equals("END")) {
                System.out.println(line);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

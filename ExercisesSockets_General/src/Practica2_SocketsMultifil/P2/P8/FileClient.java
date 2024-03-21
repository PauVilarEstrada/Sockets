package Practica2_SocketsMultifil.P2.P8;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 12345;
    public static final String DOWNLOAD_FOLDER = "W:\\Descargas";

    public void getFile(String fileName) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (!(line = reader.readLine()).equals("END_FILE_LIST")) {
                System.out.println(line);
            }

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(fileName);

            File downloadFolder = new File(DOWNLOAD_FOLDER);
            if (!downloadFolder.exists()) {
                downloadFolder.mkdirs();
            }
            File outputFile = new File(downloadFolder, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            while (!(line = reader.readLine()).equals("END_FILE")) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            fileWriter.close();
            fileOutputStream.close();

            System.out.println("Archivo descargado correctamente en: " + outputFile.getAbsolutePath());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileClient client = new FileClient();
        client.getFileList();
    }

    private void getFileList() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String line;
            while (!(line = reader.readLine()).equals("END_FILE_LIST")) {
                System.out.println(line);
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("Seleccione el nombre del archivo que desea descargar: ");
            String fileName = scanner.nextLine();
            writer.println(fileName);

            File downloadFolder = new File(DOWNLOAD_FOLDER);
            if (!downloadFolder.exists()) {
                downloadFolder.mkdirs();
            }
            File outputFile = new File(downloadFolder, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            while (!(line = reader.readLine()).equals("END_FILE")) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            fileWriter.close();
            fileOutputStream.close();

            System.out.println("Archivo descargado correctamente en: " + outputFile.getAbsolutePath());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package P2_Similars.P11_Cortos.P3_DirectorisArxius;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 6000;
    public static final String SAVED_DIRECTORY = "E:\\DAM\\2n DAM\\M09\\UF3\\Exercicis_Practicar_PreExamen_UF3\\src\\P2_Similars\\P11_Cortos\\P3_DirectorisArxius\\Arxius\\Guardats";

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Lista de archivos en el servidor:");
            printFileList(reader);

            System.out.print("Introduce el nombre del archivo que deseas descargar: ");
            String fileName = scanner.nextLine();
            writer.println(fileName);

            receiveFile(socket, fileName);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printFileList(BufferedReader reader) throws IOException {
        String line;
        while (!(line = reader.readLine()).equals("END")) {
            System.out.println(line);
        }
    }

    private static void receiveFile(Socket socket, String fileName) throws IOException {
        File file = new File(SAVED_DIRECTORY + "\\" + fileName);
        byte[] buffer = new byte[8192];
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();
    }
}

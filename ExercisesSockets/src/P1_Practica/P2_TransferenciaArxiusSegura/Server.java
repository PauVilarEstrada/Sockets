package P2_TransferenciaArxiusSegura;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Server {
    private static final int PORT = 8000;
    private static final String SECRET_KEY = "SecretKey1234567"; // Clave secreta para el cifrado
    private static final String FILE_DIRECTORY = "E:\\DAM\\2n DAM\\M09\\UF3\\Exercicis_Practicar_PreExamen_UF3\\src\\P2_TransferenciaArxiusSegura\\Arxius";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Autenticación del cliente
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String username = reader.readLine();
                String password = reader.readLine();

                if (authenticate(username, password)) {
                    writer.println("Authenticated successfully. You can transfer files now.");

                    // Crear el archivo recibido
                    receiveAndSaveFile(clientSocket);
                } else {
                    writer.println("Authentication failed. Connection closed.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean authenticate(String username, String password) {
        // Lógica de autenticación (por ejemplo, verificar en una base de datos)
        return username.equals("admin") && password.equals("admin123");
    }

    private static void receiveAndSaveFile(Socket clientSocket) throws IOException {
        try {
            // Recibir el archivo cifrado
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            SealedObject sealedFile = (SealedObject) inputStream.readObject();

            // Clave secreta para el cifrado
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Descifrar el archivo
            byte[] decryptedData = (byte[]) sealedFile.getObject(cipher);

            // Crear el directorio si no existe
            File directory = new File(FILE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Guardar el archivo en el sistema
            String filePath = FILE_DIRECTORY + File.separator + "received_file.txt";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(decryptedData);
            fileOutputStream.close();

            System.out.println("File received and saved successfully: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

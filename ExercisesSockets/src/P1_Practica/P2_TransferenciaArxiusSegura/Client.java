package P2_TransferenciaArxiusSegura;

import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 8000;
    private static final String SECRET_KEY = "SecretKey1234567"; // Clave secreta para el cifrado

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Connected to server.");

            // Autenticación del cliente
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("admin"); // Nombre de usuario
            writer.println("admin123"); // Contraseña

            String response = reader.readLine();
            if (response.equals("Authenticated successfully. You can transfer files now.")) {
                System.out.println("Authentication successful. You can transfer files now.");

                // Transferencia segura de archivos
                sendEncryptedFile(socket);
            } else {
                System.out.println("Authentication failed. Connection closed.");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendEncryptedFile(Socket socket) throws IOException {
        try {
            // Leer el archivo a transferir
            File fileToSend = new File("file_to_send.txt");
            if (!fileToSend.exists()) {
                // Si el archivo no existe, crear uno nuevo
                fileToSend.createNewFile();
                FileWriter fileWriter = new FileWriter(fileToSend);
                fileWriter.write("Este es un archivo creado automáticamente por el cliente.");
                fileWriter.close();
                System.out.println("New file 'file_to_send.txt' created.");
            }

            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            byte[] fileData = new byte[(int) fileToSend.length()];
            fileInputStream.read(fileData);
            fileInputStream.close();

            // Cifrar el archivo con AES
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(fileData);

            // Enviar el archivo cifrado al servidor
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new SealedObject(encryptedData, cipher));

            outputStream.close();
            System.out.println("File sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

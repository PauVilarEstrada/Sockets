package P1_Practica.P5_MissatgesSegurs;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.util.*;

public class SecureMessagingClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private static PrivateKey privateKey;
    private static Map<String, PublicKey> userPublicKeys;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Autenticar cliente con el servidor
            authenticateClient(outputStream, inputStream);

            while (true) {
                sendAndEncryptMessage(outputStream);
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException |
                 NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    private static void authenticateClient(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Recibir clave pública del servidor
        PublicKey serverPublicKey = (PublicKey) inputStream.readObject();
        userPublicKeys = new HashMap<>();
        userPublicKeys.put(SERVER_ADDRESS, serverPublicKey);

        // Enviar clave pública del cliente al servidor
        KeyPair keyPair = generateKeyPair();
        outputStream.writeObject(keyPair.getPublic());

        // Generar clave privada del cliente
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
        privateKey = keyFactory.generatePrivate(privateKeySpec);

        System.out.println("Cliente autenticado con el servidor");

        // Enviar clave pública del servidor al cliente
        outputStream.writeObject(serverPublicKey);
    }

    private static void sendAndEncryptMessage(ObjectOutputStream outputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su mensaje: ");
        String message = scanner.nextLine();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, userPublicKeys.get(SERVER_ADDRESS)); // Usar la clave pública del servidor
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        outputStream.writeObject(encryptedMessage);
        System.out.println("Mensaje enviado de manera segura al servidor");
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}

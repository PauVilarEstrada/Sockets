package P1_Practica.P5_MissatgesSegurs;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.util.*;

public class SecureMessagingServer {
    private static final int PORT = 12345;
    private static Map<String, PublicKey> userPublicKeys = new HashMap<>();
    private static Map<String, PrivateKey> userPrivateKeys = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                authenticateClient();
                while (true) {
                    receiveAndDecryptMessage();
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void authenticateClient() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
            // Enviar clave pública del servidor al cliente
            KeyPair keyPair = generateKeyPair();
            outputStream.writeObject(keyPair.getPublic());

            // Recibir clave pública del cliente
            PublicKey clientPublicKey = (PublicKey) inputStream.readObject();
            userPublicKeys.put(clientSocket.getInetAddress().toString(), clientPublicKey);

            // Generar clave privada del servidor
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            userPrivateKeys.put(clientSocket.getInetAddress().toString(), privateKey);

            System.out.println("Cliente autenticado: " + clientSocket.getInetAddress().toString());
        }

        private void receiveAndDecryptMessage() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, userPrivateKeys.get(clientSocket.getInetAddress().toString()));

            byte[] encryptedMessage = (byte[]) inputStream.readObject();
            byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
            String message = new String(decryptedMessage);
            System.out.println("Mensaje recibido de " + clientSocket.getInetAddress().toString() + ": " + message);
        }

        private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        }
    }
}

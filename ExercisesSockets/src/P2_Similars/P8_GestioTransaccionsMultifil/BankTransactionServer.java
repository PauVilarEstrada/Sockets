// BankTransactionServer.java
package P13_Similars.P8_GestioTransaccionsMultifil;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class BankTransactionServer {
    private static final int PORT = 6000;
    private ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public BankTransactionServer() {
        // Crear algunas cuentas de ejemplo
        accounts.put("123456", new Account("123456", 1000));
        accounts.put("789012", new Account("789012", 500));
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de transacciones bancarias iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());

                // Iniciar un nuevo hilo para manejar la conexión con el cliente
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer la solicitud del cliente
                String accountNumber = reader.readLine();
                int transactionType = Integer.parseInt(reader.readLine());
                double amount = Double.parseDouble(reader.readLine());

                Account account = accounts.get(accountNumber);
                if (account == null) {
                    writer.println("No se encontró la cuenta");
                    return;
                }

                synchronized (account) {
                    switch (transactionType) {
                        case 1:
                            account.deposit(amount);
                            writer.println("Depósito de " + amount + " realizado con éxito. Saldo actual: " + account.getBalance());
                            break;
                        case 2:
                            if (account.withdraw(amount)) {
                                writer.println("Retiro de " + amount + " realizado con éxito. Saldo actual: " + account.getBalance());
                            } else {
                                writer.println("Fondos insuficientes para realizar el retiro");
                            }
                            break;
                        case 3:
                            writer.println("Saldo actual: " + account.getBalance());
                            break;
                        default:
                            writer.println("Acción no válida");
                            break;
                    }
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BankTransactionServer server = new BankTransactionServer();
        server.startServer();
    }
}

// BankTransactionClient.java
package P13_Similars.P8_GestioTransaccionsMultifil;

import java.io.*;
import java.net.*;

public class BankTransactionClient {
    public static final String SERVER_ADDRESS = "localhost"; // Dirección del servidor
    public static final int SERVER_PORT = 6000; // Puerto del servidor

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + SERVER_PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Solicitar al usuario el número de cuenta
            System.out.print("Ingrese el número de cuenta: ");
            String accountNumber = userInput.readLine();

            // Solicitar al usuario la transacción a realizar (Depósito, Retiro o Consulta de saldo)
            System.out.println("Seleccione la transacción que desea realizar:");
            System.out.println("1. Depósito");
            System.out.println("2. Retiro");
            System.out.println("3. Consulta de saldo");
            System.out.print("Ingrese el número de la transacción: ");
            int transactionType = Integer.parseInt(userInput.readLine());

            // Solicitar al usuario el monto de la transacción
            double amount = 0;
            if (transactionType == 1 || transactionType == 2) {
                System.out.print("Ingrese el monto de la transacción: ");
                amount = Double.parseDouble(userInput.readLine());
            }

            // Enviar la solicitud al servidor
            writer.println(accountNumber);
            writer.println(transactionType);
            writer.println(amount);

            // Leer la respuesta del servidor
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

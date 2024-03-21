package P13_Similars.P1_FacturacioMultifil;

import java.io.*;
import java.net.*;

public class CajaRegistradoraClient {
    public static final String SERVER_ADDRESS = "localhost"; // Cambia esto si el servidor está en otra dirección
    public static final int SERVER_PORT = 5000;

    public void enviarVentas(int montoVentas) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Enviar el monto total de ventas al servidor
            writer.println(montoVentas);

            // Leer la respuesta del servidor
            String numeroCaja = reader.readLine();
            String ventasAcumuladas = reader.readLine();
            System.out.println("Respuesta del servidor:");
            System.out.println(numeroCaja);
            System.out.println(ventasAcumuladas);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CajaRegistradoraClient client = new CajaRegistradoraClient();
        
        // Simulamos ventas de la caja registradora
        int montoVentas = 1000;
        
        // Enviamos las ventas al servidor
        client.enviarVentas(montoVentas);
    }
}

package Practica2_SocketsMultifil.P1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainCliente {
    public static void main(String[] args) {
        // Dirección y puerto del servidor
        String serverAddress = "127.0.0.1";
        int serverPort = 65000;

        try {
            // Conectarse al servidor
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);

            // Leer el mensaje de bienvenida del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensaje = reader.readLine();
            System.out.println("Mensaje del servidor: " + mensaje);

            // Cerrar la conexión
            socket.close();
        } catch (IOException e) {
            System.out.println("Error de conexión o lectura: " + e.getMessage());
        }
    }
}

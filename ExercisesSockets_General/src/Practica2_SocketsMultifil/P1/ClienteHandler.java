package Practica2_SocketsMultifil.P1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClienteHandler implements Runnable {
    private Socket socket;
    private int clienteId;

    public ClienteHandler(Socket socket, int clienteId) {
        this.socket = socket;
        this.clienteId = clienteId;
    }

    @Override
    public void run() {
        try {
            // Enviamos el mensaje de bienvenida con el número de cliente
            OutputStream out = socket.getOutputStream();
            String mensajeBienvenida = "Benvingut, ets el client " + clienteId + "\n";
            out.write(mensajeBienvenida.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

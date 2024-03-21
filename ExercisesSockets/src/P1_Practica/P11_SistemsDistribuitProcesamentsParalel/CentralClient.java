package P11_SistemsDistribuitProcesamentsParalel;

import java.io.*;
import java.net.Socket;

public class CentralClient {
    public static final String SERVER_ADDRESS = "localhost"; // Dirección del servidor central
    public static final int SERVER_PORT = 12345; // Puerto del servidor central

    public static void main(String[] args) {
        try {
            // Conexión con el servidor central
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Enviar tareas al servidor central
            Task[] tasks = {new Task(1), new Task(2), new Task(3)}; // Ejemplo de tareas a enviar
            outputStream.writeObject(tasks);
            outputStream.flush();

            // Recibir resultados de los nodos de procesamiento
            Result[] results = (Result[]) inputStream.readObject();
            for (Result result : results) {
                System.out.println("Resultado del nodo " + result.getNodeID() + ": " + result.getResult());
            }

            // Cierre de la conexión
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package P11_SistemsDistribuitProcesamentsParalel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CentralServer {
    private List<Socket> processingNodes = new ArrayList<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Puerto del servidor central
            System.out.println("Servidor central iniciado en el puerto: " + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                processingNodes.add(socket);

                // Recibe tareas del cliente central
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Task[] tasks = (Task[]) inputStream.readObject();

                // Asigna tareas a los nodos de procesamiento disponibles
                Result[] results = distributeTasks(tasks);

                // Envia resultados al cliente central
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(results);
                outputStream.flush();

                // Cierra la conexión con el nodo de procesamiento
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Result[] distributeTasks(Task[] tasks) {
        // Distribuye tareas a los nodos de procesamiento disponibles y recopila los resultados
        List<Result> results = new ArrayList<>();
        for (Socket node : processingNodes) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(node.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(node.getInputStream());

                // Enviar tareas al nodo de procesamiento
                outputStream.writeObject(tasks);
                outputStream.flush();

                // Recibir resultados del nodo de procesamiento
                Result[] nodeResults = (Result[]) inputStream.readObject();
                for (Result result : nodeResults) {
                    results.add(result);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return results.toArray(new Result[0]);
    }

    public static void main(String[] args) {
        CentralServer server = new CentralServer();
        server.startServer();
    }
}

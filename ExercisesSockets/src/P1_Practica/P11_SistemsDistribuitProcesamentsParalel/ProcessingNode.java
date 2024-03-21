package P11_SistemsDistribuitProcesamentsParalel;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessingNode extends Thread {
    private ServerSocket serverSocket;
    private int nodeID;

    public ProcessingNode(int nodeID) {
        this.nodeID = nodeID;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(0); // Puerto aleatorio
            System.out.println("Nodo de procesamiento " + nodeID + " iniciado en el puerto: " + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                Task[] tasks = (Task[]) inputStream.readObject(); // Recibe tareas del servidor central
                Result[] results = processTasks(tasks); // Procesa las tareas

                // Enviar resultados al servidor central
                outputStream.writeObject(results);
                outputStream.flush();

                // Cierre de la conexión
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Result[] processTasks(Task[] tasks) {
        // Procesa las tareas y devuelve los resultados
        Result[] results = new Result[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            // Ejemplo de procesamiento de tareas (aquí se puede implementar la lógica real)
            results[i] = new Result(nodeID, tasks[i].getValue() * 2); // Resultado = valor de tarea * 2
        }
        return results;
    }

    public int getNodeID() {
        return nodeID;
    }

    public static void main(String[] args) {
        // Se crea e inicia un nodo de procesamiento
        ProcessingNode node = new ProcessingNode(1); // Ejemplo de ID de nodo
        node.start();
    }
}

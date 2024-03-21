import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class WordTrainServer {
    public static final int PORT = 6000;
    private List<String> wordTrain = new ArrayList<>();

    public static void main(String[] args) {
        WordTrainServer server = new WordTrainServer();
        server.start();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String clientWord = reader.readLine();
        synchronized (wordTrain) {
            wordTrain.add(clientWord);
            StringBuilder train = new StringBuilder();
            for (String word : wordTrain) {
                train.append("|").append(word);
            }
            writer.println(train.toString());
        }

        socket.close();
    }
}

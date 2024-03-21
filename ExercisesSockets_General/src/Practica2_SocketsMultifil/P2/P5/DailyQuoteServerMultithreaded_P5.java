package Practica2_SocketsMultifil.P2.P5;

import java.io.*;
import java.net.*;
import java.util.*;

public class DailyQuoteServerMultithreaded_P5 {
    private ServerSocket serverSocket;
    private List<String> dailyQuotes;
    private Map<String, List<String>> clientHistory; // Registro de clientes y frases enviadas
    public static final int PORT = 12345;

    public DailyQuoteServerMultithreaded_P5() {
        dailyQuotes = new ArrayList<>();
        dailyQuotes.add("Eres muy guapo, tanto que me he desmayado. - Pau Vilar");
        dailyQuotes.add("Si la vida fuera una peonza, tu serias mi cuerda. - Pau Vilar");
        dailyQuotes.add("No solo te tienes que tomar las criticas con humor, sino alagarte ya que lo" +
                " estas haciendo bien. - Pau Vilar");
        dailyQuotes.add("La vida es lo que pasa mientras estás ocupado haciendo otros planes. - John Lennon");
        dailyQuotes.add("El único modo de hacer un gran trabajo es amar lo que haces. - Steve Jobs");
        dailyQuotes.add("La felicidad no es algo listo. Viene de tus propias acciones. - Dalai Lama");
        dailyQuotes.add("La imaginación es más importante que el conocimiento. - Albert Einstein");
        dailyQuotes.add("No hay viento favorable para el que no sabe a dónde va. - Séneca");

        clientHistory = new HashMap<>();
    }

    public void initService() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                String clientAddress = socket.getInetAddress().getHostAddress();
                List<String> clientQuotes = clientHistory.getOrDefault(clientAddress, new ArrayList<>());

                Random random = new Random();
                String quote = null;
                do {
                    quote = dailyQuotes.get(random.nextInt(dailyQuotes.size()));
                } while (clientQuotes.contains(quote));

                clientQuotes.add(quote);
                clientHistory.put(clientAddress, clientQuotes);

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(quote);
                socket.close();
                printClientHistory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printClientHistory() {
        System.out.println("Registro de clientes y frases enviadas:");
        for (Map.Entry<String, List<String>> entry : clientHistory.entrySet()) {
            String clientAddress = entry.getKey();
            List<String> quotesSent = entry.getValue();
            System.out.println("Cliente: " + clientAddress);
            System.out.println("Tren:");
            for (String quote : quotesSent) {
                System.out.println("- " + serverSocket);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        DailyQuoteServerMultithreaded_P5 server = new DailyQuoteServerMultithreaded_P5();
        server.initService();
    }
}

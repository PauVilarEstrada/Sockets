package Practica2_SocketsMultifil.P2.P3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyQuoteServerMultithreaded {
    private ServerSocket serverSocket;
    private List<String> dailyQuotes;
    public static final int PORT = 12345;

    public DailyQuoteServerMultithreaded() {
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
            sendRandomQuote(socket);
        }
    }

    private synchronized void sendRandomQuote(Socket socket) {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            Random random = new Random();
            int index;
            do {
                index = random.nextInt(dailyQuotes.size());
            } while (dailyQuotes.get(index) == null); // Skip already sent quotes
            String quote = dailyQuotes.get(index);
            dailyQuotes.set(index, null); // Mark quote as sent
            writer.println(quote);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DailyQuoteServerMultithreaded server = new DailyQuoteServerMultithreaded();
        server.initService();
    }
}

package Practica1_Sockets.P3;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyQuoteServer {
    private ServerSocket serverSocket;
    private List<String> dailyQuotes;
    public static final int PORT = 6000;

    public DailyQuoteServer() {
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
                sendRandomQuote(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRandomQuote(Socket socket) {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            Random random = new Random();
            int index = random.nextInt(dailyQuotes.size());
            String quote = dailyQuotes.get(index);
            writer.println(quote);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DailyQuoteServer server = new DailyQuoteServer();
        server.initService();
    }
}


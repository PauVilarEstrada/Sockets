package P10_SubastesEnLinea;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AuctionServer {
    private static final int PORT = 5555;
    private static final int AUCTION_DURATION_SECONDS = 60;
    private static final int INITIAL_PRICE = 100;

    private final Map<String, Integer> bids = new ConcurrentHashMap<>();
    private final Set<String> clients = new HashSet<>();

    public static void main(String[] args) {
        new AuctionServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de subastas iniciado en el puerto " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                clients.add(clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        private final String clientAddress;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientAddress = socket.getInetAddress().getHostAddress();
        }

        @Override
        public void run() {
            try {
                startAuction();
                TimeUnit.SECONDS.sleep(AUCTION_DURATION_SECONDS);
                endAuction();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(clientAddress);
            }
        }

        private void startAuction() throws IOException {
            out.println("Auction started! Place your bids.");
            int currentPrice = INITIAL_PRICE;
            while (true) {
                out.println("Current price: " + currentPrice); // Envía el precio actual al cliente
                String input = in.readLine();
                if (input.equalsIgnoreCase("end")) {
                    break;
                }
                int bid = Integer.parseInt(input);
                if (bid > currentPrice) {
                    currentPrice = bid;
                    bids.put(clientAddress, bid);
                    out.println("Your bid of " + bid + " has been accepted.");
                } else {
                    out.println("Your bid must be higher than the current price (" + currentPrice + ").");
                }
            }
        }

        private void endAuction() throws IOException {
            out.println("Auction ended!");
            if (!bids.isEmpty()) {
                String winner = Collections.max(bids.entrySet(), Map.Entry.comparingByValue()).getKey();
                out.println("The winner is: " + winner + " with a bid of " + bids.get(winner));
            } else {
                out.println("No bids were placed.");
            }
        }
    }
}

package P10_SubastesEnLinea;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuctionClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor de subastas en " + SERVER_ADDRESS + ":" + PORT);
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
                if (message.equals("Auction started! Place your bids.")) {
                    System.out.println("Enter your bid (type 'end' to finish bidding):");
                    while (true) {
                        String currentPrice = in.readLine(); // Recibe el precio actual de la subasta
                        Matcher matcher = Pattern.compile("\\d+").matcher(currentPrice);
                        if (matcher.find()) {
                            int price = Integer.parseInt(matcher.group());
                            System.out.println("Current price: " + price);
                            String userInput = consoleReader.readLine();
                            if (userInput == null || userInput.isEmpty()) {
                                System.out.println("Invalid input. Please enter a bid.");
                                continue;
                            }
                            if (userInput.equalsIgnoreCase("end")) {
                                out.println(userInput);
                                break;
                            } else {
                                int bid = Integer.parseInt(userInput);
                                if (bid > price) {
                                    out.println(userInput);
                                } else {
                                    System.out.println("Your bid must be higher than the current price (" + price + ").");
                                }
                            }
                        } else {
                            System.out.println("Error: Couldn't parse current price.");
                            break;
                        }
                    }
                } else if (message.equals("Auction ended!")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

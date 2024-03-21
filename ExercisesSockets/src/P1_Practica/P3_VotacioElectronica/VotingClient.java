package P3_VotacioElectronica;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class VotingClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to Voting System");
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            // Envía credenciales de inicio de sesión al servidor
            writer.println("LOGIN " + username + " " + password);
            String response = reader.readLine();
            if (response.equals("LOGIN_SUCCESS")) {
                System.out.println("Login successful!");
                System.out.println("Please vote for a candidate:");
                System.out.println("1. Candidate 1");
                System.out.println("2. Candidate 2");
                System.out.println("3. Candidate 3");
                System.out.print("Enter candidate number to vote (1/2/3): ");
                String candidate = scanner.nextLine();

                // Verifica la elección del candidato y envía el voto al servidor
                if (candidate.equals("1") || candidate.equals("2") || candidate.equals("3")) {
                    writer.println("VOTE " + candidate);
                    response = reader.readLine();
                    if (response.equals("VOTE_SUCCESS")) {
                        System.out.println("Thank you for voting!");
                    } else if (response.equals("ALREADY_VOTED")) {
                        System.out.println("You have already voted. You cannot vote again.");
                    } else {
                        System.out.println("Failed to cast vote. Please try again later.");
                    }
                } else {
                    System.out.println("Invalid candidate selection. Vote null.");
                    writer.println("VOTE NULL");
                }

                // Solicita los resultados de la votación al servidor
                System.out.print("Do you want to see the voting results? (yes/no): ");
                String showResults = scanner.nextLine();
                if (showResults.equalsIgnoreCase("yes")) {
                    writer.println("RESULTS");
                    String results = reader.readLine();
                    System.out.println("\nVoting Results:\n" + results);
                }
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

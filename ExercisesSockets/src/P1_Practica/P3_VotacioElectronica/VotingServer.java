package P3_VotacioElectronica;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class VotingServer {
    private static final int PORT = 12345;
    private static HashMap<String, String> voters = new HashMap<>();
    private static HashMap<String, Integer> votes = new HashMap<>();
    private static HashMap<String, Boolean> hasVoted = new HashMap<>(); // Registro de votantes que ya han votado
    private static boolean votingOpen = true;
    private static int totalVotes = 0;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String input;
                while ((input = reader.readLine()) != null) {
                    String[] tokens = input.split(" ");
                    if (tokens[0].equals("LOGIN")) {
                        handleLogin(tokens[1], tokens[2]);
                    } else if (tokens[0].equals("VOTE")) {
                        handleVote(tokens[1]);
                    } else if (tokens[0].equals("RESULTS")) {
                        sendResults();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleLogin(String username, String password) {
            // Verifica si el usuario ya ha votado
            if (hasVoted.containsKey(username)) {
                writer.println("ALREADY_VOTED");
                return;
            }

            // Implementa lógica de autenticación aquí
            // Por simplicidad, asumimos que todos los inicios de sesión son exitosos
            voters.put(username, password);
            this.username = username;
            writer.println("LOGIN_SUCCESS");
        }

        private void handleVote(String candidate) {
            if (!votingOpen) {
                writer.println("VOTING_CLOSED");
                return;
            }

            if (hasVoted.containsKey(username)) {
                writer.println("ALREADY_VOTED");
                return;
            }

            // Registra el voto
            hasVoted.put(username, true);
            votes.put(candidate, votes.getOrDefault(candidate, 0) + 1);
            totalVotes++;

            writer.println("VOTE_SUCCESS");
            System.out.println(username + " voted for " + candidate);
        }

        private void sendResults() {
            StringBuilder result = new StringBuilder();

            result.append("Total votes: ").append(totalVotes).append("\n");
            for (String candidate : votes.keySet()) {
                int votesForCandidate = votes.get(candidate);
                result.append("Total votes for Candidate ").append(candidate).append(": ").append(votesForCandidate).append("\n");
            }

            // Determinar el ganador
            String winner = "";
            int maxVotes = 0;
            for (String candidate : votes.keySet()) {
                int votesForCandidate = votes.get(candidate);
                if (votesForCandidate > maxVotes) {
                    maxVotes = votesForCandidate;
                    winner = candidate;
                }
            }
            result.append("Winner: Candidate ").append(winner).append(" with ").append(maxVotes).append(" votes.").append("\n");

            writer.println(result.toString());
        }
    }
}

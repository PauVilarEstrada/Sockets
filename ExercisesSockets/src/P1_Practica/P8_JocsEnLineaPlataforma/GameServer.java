package P8_JocsEnLineaPlataforma;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS_PER_ROOM = 8;
    private static final int MAX_PLAYERS_IN_WAITING_ROOM = 10;

    private List<ClientHandler> clients = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private Room waitingRoom;

    public void start() {
        waitingRoom = new Room("Waiting Room", MAX_PLAYERS_IN_WAITING_ROOM);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private Room currentRoom;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                out.println("Welcome to the game server! Please enter your username:");
                username = in.readLine();
                out.println("Hello, " + username + "!");

                while (true) {
                    out.println("Please select an option:");
                    out.println("1. Create a new room");
                    out.println("2. Join an existing room");
                    out.println("3. Start playing");
                    out.println("4. Exit");

                    String choice = in.readLine();
                    switch (choice) {
                        case "1":
                            createRoom();
                            break;
                        case "2":
                            joinRoom();
                            break;
                        case "3":
                            startPlaying();
                            break;
                        case "4":
                            exit();
                            return;
                        default:
                            out.println("Invalid choice. Please try again.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void createRoom() throws IOException {
            if (currentRoom != null) {
                out.println("You are already in a room.");
                return;
            }
            if (rooms.size() >= MAX_PLAYERS_PER_ROOM) {
                out.println("All rooms are full. Please try again later.");
                return;
            }
            Room room = new Room("Room " + (rooms.size() + 1), MAX_PLAYERS_PER_ROOM);
            rooms.add(room);
            currentRoom = room;
            room.addPlayer(this);
            out.println("Room created: " + room.getName());
        }

        private void joinRoom() throws IOException {
            if (currentRoom != null) {
                out.println("You are already in a room.");
                return;
            }
            out.println("Available rooms:");
            for (Room room : rooms) {
                out.println(room.getName());
            }
            out.println("Enter room name to join:");
            String roomName = in.readLine();
            for (Room room : rooms) {
                if (room.getName().equals(roomName)) {
                    currentRoom = room;
                    room.addPlayer(this);
                    out.println("Joined room: " + room.getName());
                    return;
                }
            }
            out.println("Room not found.");
        }

        private void startPlaying() throws IOException {
            if (currentRoom == null) {
                out.println("You are not in a room.");
                return;
            }
            currentRoom.startGame();
        }

        private void exit() throws IOException {
            out.println("Goodbye, " + username + "!");
            socket.close();
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }

    private class Room {
        private String name;
        private List<ClientHandler> players;
        private int capacity;

        public Room(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
            this.players = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void addPlayer(ClientHandler player) {
            players.add(player);
        }

        public void startGame() {
            // Implementar la lógica del juego dentro de la sala
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
    }
}

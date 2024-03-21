package P13_Similars.P4_ControlDeRobotsAutonoms;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class CentralServer {
    private static final int PORT = 6000;
    private ConcurrentHashMap<String, RobotHandler> robots = new ConcurrentHashMap<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor central iniciado en el puerto: " + PORT);

            while (true) {
                Socket robotSocket = serverSocket.accept();
                System.out.println("Robot autónomo conectado desde " + robotSocket.getInetAddress().getHostAddress());

                // Crear un nuevo hilo para manejar la conexión con el robot
                RobotHandler robotHandler = new RobotHandler(robotSocket);
                robots.put(robotSocket.getInetAddress().getHostAddress(), robotHandler);
                new Thread(robotHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RobotHandler implements Runnable {
        private Socket robotSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public RobotHandler(Socket robotSocket) {
            this.robotSocket = robotSocket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(robotSocket.getInputStream()));
                writer = new PrintWriter(robotSocket.getOutputStream(), true);

                // Escuchar continuamente las instrucciones del robot
                String instruction;
                while ((instruction = reader.readLine()) != null) {
                    System.out.println("Instrucción recibida del robot: " + instruction);
                    // Aquí puedes procesar las instrucciones recibidas y enviar respuestas según sea necesario
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CentralServer server = new CentralServer();
        server.startServer();
    }
}

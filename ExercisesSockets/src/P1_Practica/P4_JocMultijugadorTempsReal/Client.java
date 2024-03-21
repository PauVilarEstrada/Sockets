package P4_JocMultijugadorTempsReal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame implements KeyListener {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private PrintWriter writer;
    private GamePanel gamePanel;

    public Client() {
        setTitle("Ping Pong Game");
        setSize(GameLogic.WIDTH, GameLogic.HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel);
        addKeyListener(this);

        setVisible(true);

        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            sendAction("UP");
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            sendAction("DOWN");
        }
    }

    private void sendAction(String action) {
        writer.println(action);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar esto, pero se requiere por la interfaz KeyListener
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No necesitamos implementar esto, pero se requiere por la interfaz KeyListener
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}

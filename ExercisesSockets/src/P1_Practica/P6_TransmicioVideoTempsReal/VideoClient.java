package P6_TransmicioVideoTempsReal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class VideoClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 6000;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Video Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        JLabel label = new JLabel();
        frame.add(label);
        frame.setVisible(true);

        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Conectado al servidor en " + SERVER_ADDRESS + ":" + PORT);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            while (true) {
                byte[] imageData = (byte[]) inputStream.readObject();
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
                label.setIcon(new ImageIcon(image));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

/*
package P6_TransmicioVideoTempsReal;

//opencv

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoServer {
    public static final int PORT = 6000;
    public static final String VIDEO_FILE = "E:\\DAM\\2n DAM\\M09\\UF3\\Exercicis_Practicar_PreExamen_UF3\\src\\P6_TransmicioVideoTempsReal\\Video\\BOWEN.mov";

    public static void main(String[] args) {
        // Cargar la biblioteca OpenCV
        System.loadLibrary(Core .NATIVE_LIBRARY_NAME);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado en el puerto: " + PORT);

            // Inicializar la captura de video
            VideoCapture capture = new VideoCapture(VIDEO_FILE);
            if (!capture.isOpened()) {
                System.err.println("No se puede abrir el archivo de video");
                return;
            }

            // Aceptar conexiones de clientes
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                new Thread(() -> sendVideo(clientSocket, capture)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendVideo(Socket socket, VideoCapture capture) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            // Transmitir el video
            Mat frame = new Mat();
            while (capture.read(frame)) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(convertToBufferedImage(frame), "jpg", byteArrayOutputStream);
                outputStream.writeObject(byteArrayOutputStream.toByteArray());
                Thread.sleep(1000 / 24); // Frecuencia de fotogramas: 24 fps
            }

            // Cerrar la conexión
            capture.release();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage convertToBufferedImage(Mat frame) {
        Size frameSize = frame.size();
        byte[] data = new byte[(int) (frameSize.height * frameSize.width * frame.channels())];
        frame.get(0, 0, data);
        BufferedImage image = new BufferedImage((int) frameSize.width, (int) frameSize.height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, (int) frameSize.width, (int) frameSize.height, data);
        return image;
    }
}
*/

package Practica1_Sockets.P8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFiles {
    public static void main(String[] args) {
        try {
            File publicDirectory = new File("public");
            if (!publicDirectory.exists()) {
                publicDirectory.mkdirs();
            }

            createFile("public/file1.txt", "Contenido del archivo 1");
            createFile("public/file2.txt", "Contenido del archivo 2");
            createFile("public/file3.txt", "Contenido del archivo 3");

            System.out.println("Archivos creados correctamente en el directorio público.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFile(String filePath, String content) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
    }
}

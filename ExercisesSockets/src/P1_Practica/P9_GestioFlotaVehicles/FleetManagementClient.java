package P9_GestioFlotaVehicles;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FleetManagementClient {
    public static final String SERVER_ADDRESS = "localhost";
    public static final int PORT = 8000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + PORT);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Enter one of the following options:");
                System.out.println("1. Get fleet information (INFO)");
                System.out.println("2. Update vehicle position (POSITION)");
                System.out.println("3. Update vehicle status (STATUS)");
                System.out.print("Option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (option) {
                    case 1:
                        writer.println("INFO");
                        System.out.print("Enter vehicle ID to view specific vehicle information: ");
                        String vehicleID = scanner.nextLine();
                        writer.println("INFO," + vehicleID);
                        String vehicleInfo = reader.readLine();
                        System.out.println("Vehicle Information for ID " + vehicleID + ":\n" + vehicleInfo);
                        break;
                    case 2:
                        System.out.print("Enter vehicle ID: ");
                        vehicleID = scanner.nextLine();
                        System.out.print("Enter latitude: ");
                        double latitude = scanner.nextDouble();
                        System.out.print("Enter longitude: ");
                        double longitude = scanner.nextDouble();
                        writer.println("POSITION," + vehicleID + "," + latitude + "," + longitude);
                        break;
                    case 3:
                        System.out.print("Enter vehicle ID: ");
                        vehicleID = scanner.nextLine();
                        System.out.print("Enter new status: ");
                        String status = scanner.nextLine();
                        writer.println("STATUS," + vehicleID + "," + status);
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

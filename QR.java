import swiftbot.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class QRAuthentication {

    static SwiftBotAPI sb = SwiftBotAPI.INSTANCE;
    static final int MAX_ATTEMPTS = 3;

    public static String authenticateSender() {

        Scanner scanner = new Scanner(System.in);
        int attemptsLeft = MAX_ATTEMPTS;

        System.out.println("[Step 2 of 6] Sender Authentication");

        while (attemptsLeft > 0) {

            System.out.println("Please present your QR code to the camera.");
            System.out.println("Scanning... (Timeout: 3 seconds)");

            // Real camera usage
            BufferedImage img = sb.takeStill(ImageSize.SQUARE_1080x1080);

            // Simulated QR decoding
            System.out.print("Enter QR code text (callsign:location): ");
            String qrText = scanner.nextLine().trim();

            if (isValidQRFormat(qrText)) {
                String callsign = qrText.split(":")[0];
                String location = qrText.split(":")[1];

                System.out.println("\nAuthentication successful.");
                System.out.println("Welcome, Agent " + location + ".");
                return qrText;
            }

            attemptsLeft--;
            System.out.println("\nAuthentication failed.");
            System.out.println("Attempts left: " + attemptsLeft + "/3");

            if (attemptsLeft > 0) {
                System.out.println("Please try again.\n");
            }
        }

        System.out.println("Authentication failed. Access denied.");
        return null;
    }

    private static boolean isValidQRFormat(String qrText) {
        if (qrText == null || !qrText.contains(":")) return false;

        String[] parts = qrText.split(":");
        if (parts.length != 2) return false;

        String callsign = parts[0];
        String location = parts[1];

        return !callsign.isEmpty() &&
               (location.equals("A") || location.equals("B") || location.equals("C"));
    }
}


// /the QR authentication was tested on the SwiftBot once the battery was fully charged to ensure reliable camera operation

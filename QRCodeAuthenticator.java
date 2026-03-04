import swiftbot.SwiftBotAPI;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Pattern;

public class QRCodeAuthenticator {

    private static final Pattern CALLSIGN_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    private final SwiftBotAPI sb = SwiftBotAPI.INSTANCE;
    private final List<Agent> knownAgents;

    public QRCodeAuthenticator(List<Agent> knownAgents) {
        this.knownAgents = knownAgents;
    }

    // Step A: Camera scan for up to timeoutSeconds, returns decoded text or "" if none found
    public String scanQRTextWithTimeout(int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            try {
                BufferedImage img = sb.getQRImage();
                String decodedText = sb.decodeQRImage(img);

                if (decodedText != null && !decodedText.trim().isEmpty()) {
                    return decodedText.trim();
                }
            } catch (IllegalArgumentException e) {
                // SwiftBot API may throw this if image is invalid; ignore and keep scanning
            }
        }
        return "";
    }

    // Step B: Parse + validate format callsign:location
    public Agent parseAndValidateQR(String qrText) {
        if (qrText == null || qrText.trim().isEmpty()) {
            throw new IllegalArgumentException("QR code is empty.");
        }

        String[] parts = qrText.trim().split(":", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid QR format. Expected callsign:location.");
        }

        String callsign = parts[0].trim();
        String location = parts[1].trim().toUpperCase();

        if (!CALLSIGN_PATTERN.matcher(callsign).matches()) {
            throw new IllegalArgumentException("Invalid callsign. Must be alphanumeric only.");
        }

        if (location.length() != 1 || !(location.equals("A") || location.equals("B") || location.equals("C"))) {
            throw new IllegalArgumentException("Invalid location. Must be A, B, or C.");
        }

        return new Agent(callsign, location);
    }

    // Step C: Check against stored agents (brief requires agents stored in robot)
    public Agent authenticate(String qrText) {
        Agent scanned = parseAndValidateQR(qrText);

        for (Agent a : knownAgents) {
            if (a.getCallsign().equalsIgnoreCase(scanned.getCallsign())
                    && a.getLocation().equalsIgnoreCase(scanned.getLocation())) {
                return a;
            }
        }

        throw new IllegalArgumentException("Authentication failed. QR not recognised.");
    }

    // Step D: Full authentication flow with retries + timeout
    public Agent authenticateWithRetries(String roleLabel, int maxAttempts, int timeoutSeconds) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("[QR Authentication] " + roleLabel);
            System.out.println("Please present your QR code to the camera.");
            System.out.println("Scanning... (Timeout: " + timeoutSeconds + " seconds)");
            System.out.println("Attempts left: " + (maxAttempts - attempt + 1) + "/" + maxAttempts);

            String qrText = scanQRTextWithTimeout(timeoutSeconds);

            if (qrText.isEmpty()) {
                System.out.println("No QR detected within timeout.");
            } else {
                try {
                    Agent a = authenticate(qrText);
                    System.out.println("Authentication successful. Welcome, " + a.getCallsign() + " (" + a.getLocation() + ").\n");
                    return a;
                } catch (IllegalArgumentException ex) {
                    System.out.println("Authentication failed: " + ex.getMessage());
                }
            }

            if (attempt < maxAttempts) {
                System.out.println("Please try again.\n");
            }
        }

        throw new IllegalStateException("Authentication failed too many times.");
    }

    // Receiver must be at destination location
    public Agent authenticateReceiverAtLocation(String expectedLocation, int maxAttempts, int timeoutSeconds) {
        Agent receiver = authenticateWithRetries("Receiver Authentication", maxAttempts, timeoutSeconds);

        if (!receiver.getLocation().equalsIgnoreCase(expectedLocation)) {
            throw new IllegalArgumentException("Receiver location mismatch. Expected " + expectedLocation
                    + " but got " + receiver.getLocation() + ".");
        }

        return receiver;
    }
}

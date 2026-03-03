public class MessageParser {

    public static String extractDestination(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message is empty.");
        }

        String[] parts = message.trim().split("\\s+", 2); // destination + rest
        if (parts.length < 2) {
            throw new IllegalArgumentException("Message must include destination and content.");
        }

        String destination = parts[0].toUpperCase();
        if (!destination.equals("A") && !destination.equals("B") && !destination.equals("C")) {
            throw new IllegalArgumentException("Invalid destination. Must be A, B, or C.");
        }

        return destination;
    }

    public static String extractContent(String message) {
        return message.trim().split("\\s+", 2)[1].trim();
    }
}

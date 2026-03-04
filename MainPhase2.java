import java.util.*;

public class Main {
    public static void main(String[] args) {

        // Stored agents (your unique callsigns)
        List<Agent> agents = Arrays.asList(
                new Agent("AgentA1", "A"),
                new Agent("AgentB1", "B"),
                new Agent("AgentC1", "C")
        );

        QRCodeAuthenticator auth = new QRCodeAuthenticator(agents);

        try {
            // Sender auth
            Agent sender = auth.authenticateWithRetries("Sender Authentication", 3, 3);

            // Example destination (later comes from decoded Morse message)
            String destination = "A";

            // Receiver auth must match destination
            Agent receiver = auth.authenticateReceiverAtLocation(destination, 3, 3);

            System.out.println("Sender: " + sender);
            System.out.println("Receiver: " + receiver);
            System.out.println("Phase 2 complete.");

        } catch (Exception e) {
            System.out.println("Program stopped: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            MorseTranslator translator = new MorseTranslator("morse_dictionary.txt");

            String plain = "A WANT GEAR";
            String encoded = translator.encode(plain);
            String decoded = translator.decode(encoded);

            System.out.println("Plain:   " + plain);
            System.out.println("Encoded: " + encoded);
            System.out.println("Decoded: " + decoded);

            String destination = MessageParser.extractDestination(decoded);
            String content = MessageParser.extractContent(decoded);

            Message msg = new Message("AgentB", "B", destination, content);
            msg.markDeliveredNow();

            MessageLogger logger = new MessageLogger(MessageLogger.defaultLogFileName());
            logger.log(msg);

            System.out.println("Logged to: " + logger.getLogFilePath());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

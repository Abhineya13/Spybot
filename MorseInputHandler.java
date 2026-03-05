import swiftbot.Button;
import swiftbot.SwiftBotAPI;

public class MorseInputHandler {

    private final SwiftBotAPI sb = SwiftBotAPI.INSTANCE;

    private final StringBuilder currentSymbol = new StringBuilder(); // current letter (dots/dashes)
    private final StringBuilder message = new StringBuilder();       // full morse message

    private volatile boolean finished = false;

    // Call this to start collecting morse input. It blocks until end-of-message is entered.
    public String recordMorseMessage() {
        finished = false;
        currentSymbol.setLength(0);
        message.setLength(0);

        System.out.println("Enter message in Morse using buttons:");
        System.out.println("X=Dot (.)  Y=Dash (-)  A=End Char  B=End Word");
        System.out.println("End of Message: enter 0 in Morse (-----) then press A or B to commit it.");

        sb.enableButton(Button.X, this::onDot);
        sb.enableButton(Button.Y, this::onDash);
        sb.enableButton(Button.A, this::onEndChar);
        sb.enableButton(Button.B, this::onEndWord);

        // Block until finished
        while (!finished) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }

        // Clean up
        sb.disableButton(Button.X);
        sb.disableButton(Button.Y);
        sb.disableButton(Button.A);
        sb.disableButton(Button.B);

        return message.toString().trim();
    }

    private void onDot() {
        currentSymbol.append('.');
        System.out.println("Input: " + currentSymbol);
    }

    private void onDash() {
        currentSymbol.append('-');
        System.out.println("Input: " + currentSymbol);
    }

    private void onEndChar() {
        commitCurrentSymbol();
    }

    private void onEndWord() {
        commitCurrentSymbol();
        // Add word separator only if not finished
        if (!finished) {
            // avoid duplicate separators
            if (message.length() > 0 && !message.toString().endsWith(" / ")) {
                message.append(" / ");
            }
            System.out.println("Word ended. Message so far: " + message);
        }
    }

    private void commitCurrentSymbol() {
        if (currentSymbol.length() == 0) {
            return; // ignore empty end presses
        }

        String symbol = currentSymbol.toString();

        // End-of-message rule: 0 in Morse = "-----"
        if (symbol.equals("-----")) {
            // do not add "-----" into message; treat it as terminator
            finished = true;
            System.out.println("End of message detected.");
            currentSymbol.setLength(0);
            return;
        }

        // Normal char: add to message with a space
        message.append(symbol).append(" ");
        System.out.println("Char committed. Message so far: " + message);

        currentSymbol.setLength(0);
    }
}

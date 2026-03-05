import swiftbot.SwiftBotAPI;

public class LEDController {

    private final SwiftBotAPI sb = SwiftBotAPI.INSTANCE;

    // Simple blink timing (you can tweak)
    private final int blinkMs = 250;
    private final int gapMs = 150;

    public void blinkDot() {
        // White
        fill(255, 255, 255);
        sleep(blinkMs);
        off();
        sleep(gapMs);
    }

    public void blinkDash() {
        // Blue
        fill(0, 0, 255);
        sleep(blinkMs);
        off();
        sleep(gapMs);
    }

    public void blinkEndChar() {
        // Amber
        fill(255, 191, 0);
        sleep(blinkMs);
        off();
        sleep(gapMs);
    }

    public void blinkEndWord() {
        // Red
        fill(255, 0, 0);
        sleep(blinkMs);
        off();
        sleep(gapMs);
    }

    public void blinkEndMessage() {
        // Green
        fill(0, 255, 0);
        sleep(blinkMs);
        off();
        sleep(gapMs);
    }

    // Deliver a Morse string that uses:
    // - dot '.' and dash '-'
    // - letter separators as spaces
    // - word separators as " / "
    public void deliverMorse(String morse) {
        String trimmed = morse.trim();
        if (trimmed.isEmpty()) return;

        String[] words = trimmed.split("\\s*/\\s*"); // split on /
        for (int w = 0; w < words.length; w++) {
            String word = words[w].trim();
            if (!word.isEmpty()) {
                String[] letters = word.split("\\s+");
                for (String letter : letters) {
                    for (char c : letter.toCharArray()) {
                        if (c == '.') blinkDot();
                        else if (c == '-') blinkDash();
                    }
                    blinkEndChar();
                }
            }
            // End of word after each word (except we’ll still blink it; matches brief)
            blinkEndWord();
        }

        blinkEndMessage();
    }

    private void fill(int r, int g, int b) {
        sb.fillUnderlights(r, g, b);
    }

    private void off() {
        sb.disableUnderlights();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}

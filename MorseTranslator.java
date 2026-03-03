import java.io.*;
import java.util.*;

public class MorseTranslator {

    private final Map<Character, String> textToMorse = new HashMap<>();
    private final Map<String, Character> morseToText = new HashMap<>();

    public MorseTranslator(String filePath) throws IOException {
        loadDictionary(filePath);
    }

    private void loadDictionary(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+"); // handles extra spaces safely
                char letter = parts[0].charAt(0);
                String morse = parts[1];

                textToMorse.put(letter, morse);
                morseToText.put(morse, letter);
            }
        }
    }

    public String encode(String text) {
        StringBuilder encoded = new StringBuilder();

        for (char c : text.toUpperCase().toCharArray()) {
            if (c == ' ') {
                encoded.append(" / ");
            } else {
                String morse = textToMorse.get(c);
                if (morse != null) {
                    encoded.append(morse).append(" ");
                }
            }
        }
        return encoded.toString().trim();
    }

    public String decode(String morse) {
        StringBuilder decoded = new StringBuilder();
        String[] words = morse.trim().split("\\s*/\\s*"); // allows " / " or "/"

        for (String word : words) {
            String[] letters = word.trim().split("\\s+");
            for (String letter : letters) {
                Character c = morseToText.get(letter);
                if (c != null) {
                    decoded.append(c);
                }
            }
            decoded.append(" ");
        }
        return decoded.toString().trim();
    }
}

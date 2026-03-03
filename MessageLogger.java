import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageLogger {

    private final String logFilePath;

    public MessageLogger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void log(Message msg) throws IOException {
        try (FileWriter writer = new FileWriter(logFilePath, true)) { // append = true
            writer.write(msg.toLogString());
            writer.write("\n");
        }
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public static String defaultLogFileName() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return "spybot_log_" + LocalDateTime.now().format(fmt) + ".txt";
    }
}

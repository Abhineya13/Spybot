import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final String senderCallsign;
    private final String senderLocation;
    private final String receiverLocation;
    private final String contentPlainText;
    private final LocalDateTime recordedAt;
    private LocalDateTime deliveredAt;

    public Message(String senderCallsign, String senderLocation, String receiverLocation, String contentPlainText) {
        this.senderCallsign = senderCallsign;
        this.senderLocation = senderLocation;
        this.receiverLocation = receiverLocation;
        this.contentPlainText = contentPlainText;
        this.recordedAt = LocalDateTime.now();
    }

    public void markDeliveredNow() {
        this.deliveredAt = LocalDateTime.now();
    }

    public String toLogString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String delivered = (deliveredAt == null) ? "NOT_DELIVERED" : deliveredAt.format(fmt);

        return "----- MESSAGE -----\n"
                + "Sender Callsign: " + senderCallsign + "\n"
                + "Sender Location: " + senderLocation + "\n"
                + "Receiver Location: " + receiverLocation + "\n"
                + "Recorded At: " + recordedAt.format(fmt) + "\n"
                + "Delivered At: " + delivered + "\n"
                + "Content (Plain): " + contentPlainText + "\n";
    }
}

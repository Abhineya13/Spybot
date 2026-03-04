public class Agent {
    private final String callsign;
    private final String location; // "A", "B", or "C"

    public Agent(String callsign, String location) {
        this.callsign = callsign;
        this.location = location;
    }

    public String getCallsign() { return callsign; }
    public String getLocation() { return location; }

    @Override
    public String toString() {
        return callsign + "@" + location;
    }
}

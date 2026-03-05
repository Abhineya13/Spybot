import swiftbot.SwiftBotAPI;

public class NavigationController {

    public enum Location { A, B, C }

    private final SwiftBotAPI sb = SwiftBotAPI.INSTANCE;

    // ====== CALIBRATION CONSTANTS (you will adjust these) ======
    private int forwardSpeedLeft = 40;
    private int forwardSpeedRight = 40;

    // Time to travel ONE SIDE of the triangle (50cm)
    private int edgeTravelMs = 4500;

    // Time to turn ~60 degrees (clockwise / right turn)
    private int turn60Ms = 650;

    // Turning: right turn = left wheel forward, right wheel backward
    private int turnSpeedLeft = 35;
    private int turnSpeedRight = -35;
    // ===========================================================

    private Location currentLocation = Location.A; // start assumption
    private boolean clockwise = true;              // keep one direction for simplicity

    public NavigationController(Location startLocation) {
        this.currentLocation = startLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    // Optional: lets you update calibration after testing
    public void setCalibration(int edgeTravelMs, int turn60Ms) {
        this.edgeTravelMs = edgeTravelMs;
        this.turn60Ms = turn60Ms;
    }

    public void goTo(Location target) {
        if (target == currentLocation) return;

        int steps = clockwiseSteps(currentLocation, target);

        for (int i = 0; i < steps; i++) {
            moveOneEdgeForward();
            currentLocation = nextClockwise(currentLocation);

            // If we still need more edges, turn at the corner to follow the track
            if (i < steps - 1) {
                turnClockwise60();
            }
        }
    }

    public void returnTo(Location senderLocation) {
        goTo(senderLocation);
    }

    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) { }
    }

    // ====== low-level movement ======
    private void moveOneEdgeForward() {
        sb.move(forwardSpeedLeft, forwardSpeedRight, edgeTravelMs);
    }

    private void turnClockwise60() {
        sb.move(turnSpeedLeft, turnSpeedRight, turn60Ms);
    }

    // ====== triangle helpers ======
    private Location nextClockwise(Location loc) {
        return switch (loc) {
            case A -> Location.B;
            case B -> Location.C;
            case C -> Location.A;
        };
    }

    private int clockwiseSteps(Location from, Location to) {
        // A->B:1, A->C:2, B->C:1, B->A:2, C->A:1, C->B:2
        if (from == to) return 0;

        if (from == Location.A && to == Location.B) return 1;
        if (from == Location.A && to == Location.C) return 2;

        if (from == Location.B && to == Location.C) return 1;
        if (from == Location.B && to == Location.A) return 2;

        if (from == Location.C && to == Location.A) return 1;
        return 2; // C->B
    }
}

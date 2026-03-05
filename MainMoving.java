public class Main {
    public static void main(String[] args) {
        NavigationController nav = new NavigationController(NavigationController.Location.B);

        // Example: go from B -> A (clockwise will do 2 edges)
        nav.goTo(NavigationController.Location.A);

        // wait 10 seconds at destination (brief requirement)
        nav.waitSeconds(10);

        // return to sender (B)
        nav.returnTo(NavigationController.Location.B);
    }
}

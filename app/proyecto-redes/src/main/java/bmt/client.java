// package app.proyecto-redes.src.main.java.bmt;

public class client {
    public static void signIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.next(); // Read a single word
        System.out.println("Signing in with username: " + username);
    }
}

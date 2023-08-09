package bmt;

import java.util.Scanner;
import org.jivesoftware.smack.XMPPTCPConnection;
import org.jivesoftware.smack.XMPPTCPConnectionConfiguration;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost("jabber.org")
                .setPort(5222)

                .setUsernameAndPassword("mtucker", "password")
                .setServiceName("jabber.org")
                .setHost("jabber.org")
                .setPort(5222)
                .build();

        do {
            System.out.println("Welcome to chat undifined name we are not creeative enough to think of a name");
            System.out.println("1. Log in");
            System.out.println("2. Sing in");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Log in.");
                    break;
                case 2:
                    System.out.println("Sing in.");
                    break;
                case 3:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 3);

        scanner.close();
    }

    public static void signIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine(); // Read a single word
        username = username + "@gmail.xyz";
        System.out.println("Signing in with username: " + username);
    }

    public static void logIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.next(); // Read a single word
        System.out.println("Logging in with username: " + username);
    }

}

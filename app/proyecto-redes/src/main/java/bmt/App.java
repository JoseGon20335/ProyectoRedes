package bmt;

import java.io.IOException;
import java.util.Scanner;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

public class App {
    public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost("alumchat.xyz")
                .setXmppDomain("alumchat.xyz")
                .setPort(5222)
                .setUsernameAndPassword("jose@alumchat.xyz", "123")
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        connection.connect(); // Establishes a connection to the server
        connection.login(); // Logs in

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

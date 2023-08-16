package bmt;

import java.io.IOException;
import java.util.Scanner;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.minidns.record.A;
import org.jxmpp.jid.parts.Localpart;

public class App {
    static String nameToRefer = "";

    public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Welcome to chat undifined name we are not creeative enough to think of a name");
            System.out.println("1. Log in");
            System.out.println("2. Sing in");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Log in...");
                    boolean config = logIn();
                    if (config == false) {
                        System.out.println("Error: Invalid username or password");
                        break;
                    } else {
                        System.out.println("Connected");
                        break;
                    }
                    // break;
                case 2:
                    System.out.println("Sing in...");
                    // XMPPTCPConnectionConfiguration config = signIn();

                    // AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                    // connection.connect(); // Establishes a connection to the server
                    // connection.login(); // Logs in
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

    public static XMPPTCPConnectionConfiguration signIn()
            throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        username = username + "@alumchat.xyz";

        Localpart localpart = Localpart.from(username);

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        nameToRefer = username;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost("alumchat.xyz")
                .setXmppDomain("alumchat.xyz")
                .setPort(5222)
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        connection.connect();

        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.createAccount(localpart, password, null);

        System.out.println("Signing in with username: " + username);

        return config;
    }

    public static boolean logIn()
            throws SmackException, IOException, XMPPException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        boolean flag = false;
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        // username = username + "@alumchat.xyz";

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost("alumchat.xyz")
                    .setXmppDomain("alumchat.xyz")
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setUsernameAndPassword(username, password)
                    .build();

            System.out.println("Log in with username: " + username);
            AbstractXMPPConnection connection = new XMPPTCPConnection(config);

            connection.connect(); // Establishes a connection to the server
            connection.login(); // Logs in

            insideChat(connection);
            flag = true;
            // if (connection.getConnectionCounter() <= 0) {
            // System.out.println("Error: Invalid username or password");
            // flag = false;
            // } else {
            // System.out.println("Connected");

            // connection.connect(); // Establishes a connection to the server
            // connection.login(); // Logs in

            // insideChat(connection);
            // flag = true;
            // }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            flag = true;
        }
        // XMPPTCPConnectionConfiguration config =
        // XMPPTCPConnectionConfiguration.builder()
        // .setHost("alumchat.xyz")
        // .setXmppDomain("alumchat.xyz")
        // .setPort(5222)
        // .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
        // .setUsernameAndPassword(username, password)
        // .build();

        // System.out.println("Log in with username: " + username);
        return flag;
    }

    public static void insideChat(AbstractXMPPConnection connection)
            throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("Connected " + nameToRefer + " to the chat");

        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                System.out.println("New message from " + from + ": " + message.getBody());
            }
        });

        do {
            System.out.println("Welcome to chat undifined name we are not creeative enough to think of a name");
            System.out.println("1. Chat with someone");
            System.out.println("2. Chat in a group chat");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:

                    messageSender(connection);
                    break;
                case 2:
                    System.out.println("Sing in...");
                    // XMPPTCPConnectionConfiguration config = signIn();

                    // AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                    // connection.connect(); // Establishes a connection to the server
                    // connection.login(); // Logs in
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

    public static void messageSender(AbstractXMPPConnection connection)
            throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the username of the destinatary: ");
        String sendTo = scanner.nextLine();
        sendTo = sendTo + "@alumchat.xyz";

        System.out.print("Enter your message: ");
        String message = scanner.nextLine();

        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        EntityBareJid jid = JidCreate.entityBareFrom(sendTo);
        Chat chat = chatManager.chatWith(jid);

        chat.send(message);
    }

}

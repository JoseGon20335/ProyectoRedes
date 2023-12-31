package bmt;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.minidns.record.A;
import org.jxmpp.jid.parts.Localpart;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.PresenceBuilder;
import org.jivesoftware.smack.packet.StanzaFactory;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.MultiUserChatException.MucNotJoinedException;
import org.jivesoftware.smackx.muc.MultiUserChatException.NotAMucServiceException;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Resourcepart;
import java.io.File;
import java.util.*;

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
                    signIn();
                    break;
                case 3:
                    System.out.println("Exiting the program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 3);
    }

    public static XMPPTCPConnectionConfiguration signIn()
            throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        // username = username + "@alumchat.xyz";

        Localpart localpart = Localpart.from(username);

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        nameToRefer = username;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost("alumchat.xyz")
                .setXmppDomain("alumchat.xyz")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setPort(5222)
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        connection.connect();

        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.sensitiveOperationOverInsecureConnection(true);
        accountManager.createAccount(Localpart.fromOrThrowUnchecked(username), password);

        System.out.println("Signing in with username: " + username);

        insideChat(connection);

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

        } catch (Exception e) {
            System.out.println("Error: " + e);
            flag = true;
        }
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
            System.out.println("3. Mostrar todos los usuarios");
            System.out.println("4. Agregar un usuario a los contactos");
            System.out.println("5. Cambiar presencia");
            System.out.println("6. Cambiar estado");
            System.out.println("7. Cerrar sesion");
            System.out.println("8. Delete Account");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Chatear con alguien");
                    messageSender(connection);
                    break;
                case 2:
                    System.out.println("Chatear en un grupo");
                    messageSenderGroup(connection);
                    break;
                case 3:
                    System.out.println("Mostar contactos");
                    seeContactInfo(connection);
                    break;
                case 4:
                    System.out.println("Agregar un usuario a los contactos");
                    addContact(connection);
                    break;
                case 5:
                    System.out.println("Cambiar presencia");
                    changePresence(connection);
                    break;
                case 6:
                    System.out.println("Cambiar estado");
                    changeStatus(connection);
                    break;
                case 7:
                    System.out.println("Closing session.");
                    closeSession(connection);
                    main(null);
                    break;
                case 8:
                    System.out.println("Delete account.");
                    deleteAccount(connection);
                    main(null);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 7);
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

    public static void messageSenderGroup(AbstractXMPPConnection connection)
            throws SmackException, IOException, XMPPException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre del chat grupal: ");
        String sendTo = scanner.nextLine();

        System.out.print("Ingrese su nombre: ");
        String message = scanner.nextLine();

        EntityBareJid entity = JidCreate.entityBareFrom(sendTo + "@conference.alumchat.xyz");
        Resourcepart chatName = Resourcepart.from(message);

        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc = manager.getMultiUserChat(entity);

        MucEnterConfiguration.Builder builder = muc.getEnterConfigurationBuilder(chatName).requestNoHistory();
        MucEnterConfiguration configuration = builder.build();

        muc.join(configuration);

        muc.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {
                if (message.getBody() != null) {
                    System.out.println(message.getFrom() + ": " + message.getBody());
                }
            }
        });
        int choice;
        do {
            System.out.println("Opciones de chat grupal");
            System.out.println("1. Chatear en el grupo");
            System.out.println("2. Salir del grupo");
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                System.out.println("Escriba su mensaje");
                message = scanner.nextLine();

                muc.sendMessage(message);
            }
        } while (choice != 2);
    }

    public static void addContact(AbstractXMPPConnection connection)
            throws NotLoggedInException, NoResponseException, XMPPErrorException,
            NotConnectedException, InterruptedException, XmppStringprepException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre del contacto para agregar");
        String username = scanner.nextLine();
        Roster roster = Roster.getInstanceFor(connection);
        BareJid jid = JidCreate.bareFrom(username);

        System.out.println("Ingrese su user");
        final String userNameForContact = scanner.nextLine();

        final Roster finalRoster = roster; // Declare a final reference to the roster

        finalRoster.addRosterListener(new RosterListener() {
            @Override
            public void entriesAdded(Collection<Jid> addresses) {
                // Handle new entries
                System.out.println("Contacto agregado");
            }

            @Override
            public void entriesUpdated(Collection<Jid> addresses) {
                // Handle updated entries
                System.out.println("Contacto actualizado");
            }

            @Override
            public void entriesDeleted(Collection<Jid> addresses) {
                // Handle deleted entries
                System.out.println("Contacto eliminado");
            }

            @Override
            public void presenceChanged(Presence presence) {
                if (presence.getType() == Presence.Type.subscribe) {
                    BareJid fromJID = presence.getFrom().asBareJid();

                    try {
                        // Aceptar el request using finalRoster
                        finalRoster.createItemAndRequestSubscription(fromJID, userNameForContact, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Send subscription request
        finalRoster.createItemAndRequestSubscription(jid, username, null);
    }

    // ver a los contactos
    public static void seeContactInfo(AbstractXMPPConnection connection)
            throws SmackException, IOException, XMPPException, InterruptedException {

        try {

            Roster roster = Roster.getInstanceFor(connection);
            roster.addRosterListener(new RosterListener() {
                @Override
                public void entriesAdded(java.util.Collection<Jid> addresses) {
                    // Handle new entries
                    System.out.println("Contacto agregado");
                }

                @Override
                public void entriesUpdated(java.util.Collection<Jid> addresses) {
                    // Handle updated entries
                    System.out.println("Contacto actualizado");
                }

                @Override
                public void entriesDeleted(java.util.Collection<Jid> addresses) {
                    // Handle deleted entries
                    System.out.println("Contacto eliminado");
                }

                @Override
                public void presenceChanged(Presence presence) {
                    // Handle presence changes
                    System.out.println("Cambio de presencia");
                }
            });

            roster.reload();
            for (RosterEntry entry : roster.getEntries()) {
                System.out.println("______________________________________________________");
                System.out.println("User name: " + entry.getJid());
                Presence presence = roster.getPresence(entry.getJid());
                System.out.println("Is present: " + presence.getStatus());
                System.out.println("Is avaidable: " + presence.getMode());

            }
        } catch (SmackException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // define mensaajes de presencia y status
    public static void changePresence(AbstractXMPPConnection connection)
            throws NotConnectedException, InterruptedException {
        StanzaFactory stanzaFactory = connection.getStanzaFactory();
        PresenceBuilder presenceBuilder = stanzaFactory.buildPresenceStanza();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Elija un estado");
        System.out.println("1.Available");
        System.out.println("2.Not Available");
        System.out.println("3.Away");
        int optionInt = scanner.nextInt();
        switch (optionInt) {
            case 1:
                PresenceBuilder presenceBuilder2 = PresenceBuilder.buildPresence()
                        .setMode(Presence.Mode.available)
                        .setPriority(10);
                connection.sendStanza(presenceBuilder2.build());
                break;
            case 2:
                presenceBuilder2 = PresenceBuilder.buildPresence()
                        .setMode(Presence.Mode.dnd)
                        .setPriority(10);
                connection.sendStanza(presenceBuilder2.build());
                break;
            case 3:
                presenceBuilder2 = PresenceBuilder.buildPresence()
                        .setMode(Presence.Mode.away)
                        .setPriority(10);
                connection.sendStanza(presenceBuilder2.build());
                break;
            default:
                System.out.println("Option not valid");
        }
    }

    // define mensaajes de presencia y status
    public static void changeStatus(AbstractXMPPConnection connection)
            throws NotConnectedException, InterruptedException {
        StanzaFactory stanzaFactory = connection.getStanzaFactory();
        PresenceBuilder presenceBuilder = stanzaFactory.buildPresenceStanza();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el nuevo estado para su usuario");
        String newStatus = scanner.nextLine();
        Presence statusPresence = presenceBuilder.setStatus(newStatus).build();
        connection.sendStanza(statusPresence);
        scanner.nextLine();
    }

    public static void closeSession(AbstractXMPPConnection connection)
            throws NotConnectedException, InterruptedException {
        connection.disconnect();
    }

    public static void deleteAccount(AbstractXMPPConnection connection)
            throws NotConnectedException, InterruptedException, NoResponseException, XMPPErrorException {
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.deleteAccount();
    }
}

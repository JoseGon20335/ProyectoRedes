// package app.proyecto-redes.src.main.java.bmt;
import java.util.Scanner;
import 

public class client {
    public static void logIn() {
        AbstractXMPPConnection connection = new XMPPTCPConnection("mtucker", "password", "jabber.org");
        connection.connect().login();
    }
}

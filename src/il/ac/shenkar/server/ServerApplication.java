package il.ac.shenkar.server;

import il.ac.shenkar.common.ConnectionProxy;

import java.net.*;
import java.io.*;

public class ServerApplication {
    public static String serverName = "127.0.0.1";
    public static int serverPortNumber = 1300;

    public static void main(String args[]) {
        Socket socket = null;
        ServerSocket server = null;
        MessageBoard mb = new MessageBoard();
        ConnectionProxy connection = null;
        ClientDescriptor client = null;
        try {
            server = new ServerSocket(serverPortNumber, 5);
            System.out.println("server socket was created successfully");
        } catch (IOException e) {
            System.out.println("prob with creating the server socket object");
        }
        while (true) {
            try {
                socket = server.accept();
                connection = new ConnectionProxy(socket);
                client = new ClientDescriptor();
                connection.addConsumer(client);
                client.addConsumer(mb);
                mb.addConsumer(connection);
                connection.start();
                System.out.println("Received connection from client");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


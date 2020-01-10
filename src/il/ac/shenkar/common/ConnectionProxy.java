package il.ac.shenkar.common;

import il.ac.shenkar.client.ClientGUI;
import il.ac.shenkar.server.ClientDescriptor;

import java.io.*;
import java.net.Socket;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer {
    private Socket _socket;
    private StringConsumer _consumer;

    public ConnectionProxy(Socket socket) {
        this._socket = socket;

    }

    @Override
    public void run() {
        DataInputStream dataInputStream = null;
        while (true) {
            try {
                dataInputStream = new DataInputStream(_socket.getInputStream());
                String received = dataInputStream.readUTF();
                System.out.println("received: " + received);
                _consumer.consume(received);
            } catch (IOException e) {
                if (!isClosed()) {
                    close();
                }
                e.printStackTrace();
                return;
            }
        }

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        if (sc instanceof ClientDescriptor || sc instanceof ClientGUI) {
            _consumer = sc;
        }
        System.err.println("Incompatible type of consumer: " + sc.getClass());
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }

    @Override
    public void consume(String str) {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(_socket.getOutputStream());
            dataOutputStream.writeUTF(str);
            dataOutputStream.flush();

        } catch (IOException e) {
            System.err.println("Failed to send message using connection proxy");
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return _socket.isClosed();
    }

    public void close() {
        if (!_socket.isClosed()) {
            try {
                _socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket connection");
                e.printStackTrace();
            }
        }
    }
}
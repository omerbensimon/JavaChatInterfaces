package il.ac.shenkar.common;

import il.ac.shenkar.client.ClientGUI;

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
                e.printStackTrace();
            } finally {
                if (_socket != null) {
                    try {
                        _socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        // Make sure it's ClientDescriptor or ClientGUI
        if (sc instanceof ConnectionProxy || sc instanceof ClientGUI) {
            _consumer = sc;
        }
        // TODO err
    }

    @Override
    public void removeConsumer(StringConsumer sc) {

    }

    @Override
    public void consume(String str) {
        // Pass it through the socket connection
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(_socket.getOutputStream());
            dataOutputStream.writeUTF(str);
            dataOutputStream.flush();

        } catch (IOException e) {
            System.err.println("Failed to send message using connection proxy");
            e.printStackTrace();
        } finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
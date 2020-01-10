package il.ac.shenkar.client;

//import il.ac.shenkar.common.ConnectionProxy;
import il.ac.shenkar.common.ConnectionProxy;
import il.ac.shenkar.common.StringConsumer;
import il.ac.shenkar.common.StringProducer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI implements StringConsumer, StringProducer {
    private JFrame _frame;
    private JButton _sendButton;
    final private JTextArea _messageLog;
    final private JTextField _messageInput;
    private JScrollPane pane;
    private JPanel panel1;
    private JPanel panel2;
    private JLabel _chatLogLabel;
    private JLabel _messageLogLabel;
    GridBagConstraints gc;
    GridBagConstraints gc2;
    private ActionListener _sendMessageListener;
    private ConnectionProxy _connection = null;

    class BtListenr implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            _connection.consume(_messageInput.getText());
        }
    }
    public ClientGUI(){
        _frame = new JFrame("CHAT");
        _sendButton = new JButton("Send");
        _messageLog = new JTextArea(10, 10);
        _messageInput = new JTextField(10);
        panel1 = new JPanel(new GridBagLayout());
        panel2 = new JPanel(new GridBagLayout());
        _chatLogLabel = new JLabel("Chat Log");
        _messageLogLabel = new JLabel("Message");
        pane = new JScrollPane(_messageLog);
        gc = new GridBagConstraints();
        gc2 = new GridBagConstraints();
        _sendMessageListener = new BtListenr();
    }
    public void start(){
        _frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //super.windowClosing(e);
                System.exit(0);
            }
        });
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setResizable(false);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        panel1.add(_chatLogLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        panel1.add(pane, gc);

        gc2.fill = GridBagConstraints.HORIZONTAL;
        gc2.weightx = 1;
        gc2.gridx = 0;
        gc2.gridy = 0;
        gc2.ipadx = 10;
        panel2.add(_messageLogLabel, gc2);

        gc2.gridx = 1;
        gc2.gridy = 0;
        panel2.add(_messageInput, gc2);

        gc2.gridx = 1;
        gc2.gridy = 1;
        panel2.add(_sendButton, gc2);
        _sendButton.addActionListener(_sendMessageListener);
        _frame.add(panel1, BorderLayout.CENTER);
        _frame.add(panel2, BorderLayout.SOUTH);
        _frame.pack();
        _frame.setVisible(true);
    }

    public static void main(String args[]) throws IOException {
        ClientGUI gui = new ClientGUI();
        gui.start();
        Socket socket = new Socket("127.0.0.1", 1300);
        ConnectionProxy connection = new ConnectionProxy(socket);
        connection.addConsumer(gui);
        gui.addConsumer(connection);// Add the connection as a consumer of the ClientGui
    }
    @Override
    public void consume(String message) {
        // Displays the "str" as a message on the screen
        this._messageLog.append(message+"\n");
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        // Save the ConnectionProxy to a private property
        if (sc instanceof ConnectionProxy){
            _connection = (ConnectionProxy) sc;
        }
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        if (_connection != null){
            _connection.close();
        }
    }
}

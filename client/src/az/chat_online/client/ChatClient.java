package az.chat_online.client;

import az.chat_online.network.TCPConnection;
import az.chat_online.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatClient extends JFrame implements ActionListener, TCPConnectionListener {
    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready!");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception "+e );
    }

    private static final String host = "localhost";
    private static final int port = 8081;
    private static final int width = 600;
    private static final    int height = 400;
    private TCPConnection connection;
    private static int connectionCount;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatClient();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNick = new JTextField("alex");
    private final JTextField input = new JTextField();

    private ChatClient(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width,height);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        input.addActionListener(this);
        add(input, BorderLayout.SOUTH);
        add(fieldNick, BorderLayout.NORTH);
        try {
            connection= new TCPConnection(this, host, port);
            connectionCount++;
        } catch (IOException e) {
            e.printStackTrace();
            printMsg("Connection Exception "+e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        connection.sendString(getNickOrDefault()+": "+msg);
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    private String getNickOrDefault(){
        String nick = fieldNick.getText();
        if (nick.equals(""))
            return "DefaultUser"+connectionCount;

        return nick;
    }


}

package az.chat_online.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import az.chat_online.network.TCPConnection;
import az.chat_online.network.TCPConnectionListener;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) {
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connectionArrayList = new ArrayList<>();

    private ChatServer(){
        System.out.println("Server running...");
        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            while (true){
                try{
                    TCPConnection tcp = new TCPConnection(this, serverSocket.accept());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connectionArrayList.add(tcpConnection);
        sendToAllConnections("ClientConnected: "+tcpConnection.toString(), tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value, tcpConnection);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connectionArrayList.remove(tcpConnection);
        sendToAllConnections("ClientDisconnected" + tcpConnection.toString(),tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPException: "+ e);
    }

    private void sendToAllConnections(String value, TCPConnection tcpConnection){
        System.out.println(value);
        connectionArrayList./*stream().filter(x-> !x.equals(tcpConnection)).*/forEach(x -> x.sendString(value));
    }
}

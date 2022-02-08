package az.chat_online.network;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);
    void OnReceiveString(TCPConnection tcpConnection, String value);
    void onDisconnect(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception e);
}

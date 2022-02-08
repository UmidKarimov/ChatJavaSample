package az.chat_online.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TCPConnection {

    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(Socket socket) throws IOException {
        this.socket=socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        rxThread.start();
    }

}

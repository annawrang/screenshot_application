package screenshot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Creates the server when a client is connecting */

public class ServerListener extends Thread {

    protected int port = 33355;
    private ServerSocket serverSocket1;

    public ServerListener() throws IOException {
        serverSocket1 = new ServerSocket(port);
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSock1 = serverSocket1.accept();
                Server server = new Server(clientSock1);
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerListener sl = new ServerListener();
    }
}

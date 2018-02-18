package screenshot;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private Socket clientSocket1;
    private ObjectOutputStream userOut;
    private ObjectInputStream userIn;
    private Session session;

    public Server(Socket clientSocket1) {
        try {
            userOut = new ObjectOutputStream(clientSocket1.getOutputStream());
            userIn = new ObjectInputStream(clientSocket1.getInputStream());
            session = (Session)userIn.readObject();
            System.out.println(session.getTest());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

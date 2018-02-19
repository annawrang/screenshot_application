package screenshot;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*  The server which communicates with the database-connection class when 
    recieving requests from the client */

public class Server implements Runnable {

    private Socket clientSocket1;
    private ObjectOutputStream userOut;
    private ObjectInputStream userIn;
    private DatabaseConnection dc;
    private Session session = new Session();

    public Server(Socket clientSocket1) {
        try {
            dc = new DatabaseConnection();
            userOut = new ObjectOutputStream(clientSocket1.getOutputStream());
            userIn = new ObjectInputStream(clientSocket1.getInputStream());

            session.imageCount = dc.getImageNumber();
            session.setState(State.SUCCESS);
            userOut.writeObject(session);
            this.run();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                session = (Session) userIn.readObject();
                System.out.println(session.getState());
                if (session.getState().equals(State.GETSCREENSHOT)) {
                    System.out.println("getscreenshot server");
                    dc.saveImage(session.screenIm, session.getFileName(), session.getImageCount());
                    session.setState(State.SUCCESS);
                    userOut.writeObject(session);
                    userOut.flush();
                } else if (session.getState().equals(State.RETURNIMAGE)) {
                    System.out.println("returnimage server");
                    session.im = dc.retrieveImage(session.getImageName());
                    session.setState(State.SUCCESS);
                    userOut.writeObject(session);
                    userOut.flush();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

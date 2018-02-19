package screenshot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/* The client that creates a gui and communicates with the server with a
    session-object. Has methods to get a screenshot or to display a
    screenshot from the database */

public class ScreenShot {

    DatabaseConnection dc = new DatabaseConnection();
    private ObjectOutputStream outServer;
    private ObjectInputStream inServer;
    private Socket serverSocket;
    private int port = 33333;
    private Gui gui;
    private Session session;
    private String imageName = "screenshot";
    private long imageCount;

    public ScreenShot() {
        try {
            gui = new Gui(this);
            serverSocket = new Socket("127.0.0.1", 33355);
            outServer = new ObjectOutputStream(serverSocket.getOutputStream());
            inServer = new ObjectInputStream(serverSocket.getInputStream());
            session = (Session) inServer.readObject();
            imageCount = session.getImageCount();
            System.out.println("image count is " + imageCount);

        } catch (IOException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Can't connect to the server."
                    + " (Start ServerListener first)");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getScreenShot(String[] urls) throws URISyntaxException {
        String imageNames = "New screenshots have been saved as: ";
        try {
            String[] urlList = urls;
            Robot robot = new Robot();
            String format = "png";

            for (String u : urlList) {
                String fileName = imageName + imageCount + "." + format;
                imageNames += fileName + "  ";
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                URI uri = new java.net.URI(u);
                Desktop.getDesktop().browse(uri);
                TimeUnit.SECONDS.sleep(3);
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                ImageIcon ii = new ImageIcon(screenFullImage);

                session.screenIm = ii;
                session.setState(State.GETSCREENSHOT);
                session.setFileName(fileName);
                session.setImageCount(imageCount);
                outServer.writeObject(session);

                session = (Session) inServer.readObject();
                if (session.getState().equals(State.SUCCESS)) {
                }
                imageCount++;
            }
            JOptionPane.showMessageDialog(null, imageNames);
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void returnImage(String imageName) {
        ImageIcon im = null;
        try {
            session.setImageName(imageName);
            session.setState(State.RETURNIMAGE);
            outServer.writeObject(session);
            session = (Session) inServer.readObject();
            if (session.getState().equals(State.SUCCESS)) {
                im = session.getIm();
            }
            gui.showImage(im);

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "The image doesn't exist.");
        } catch (IOException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ScreenShot sc = new ScreenShot();
    }

}

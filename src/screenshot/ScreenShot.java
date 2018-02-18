package screenshot;

// testa att skriva in fler adresser + stäng ner sidorna efter screen shottet!

// https://www.facebook.com/;https://github.com/annawrang/Screenshot

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ScreenShot {

    DatabaseConnection dc = new DatabaseConnection();
    private ObjectOutputStream outServer;
    private ObjectInputStream inServer;
    private Socket serverSocket;
    private int port = 33333;
    private Gui gui;
    private Session session = new Session();
    private String imageName = "screenshot";
    private long imageCount;

    public ScreenShot() {
        try {
            imageCount = dc.getImageNumber();
            gui = new Gui(this);
            serverSocket = new Socket("127.0.0.1", 33355);
            outServer = new ObjectOutputStream(serverSocket.getOutputStream());
            inServer = new ObjectInputStream(serverSocket.getInputStream());
            outServer.writeObject(session);
        } catch (IOException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Can't connect to the server."
                    + " (Start ServerListener first)");
        }
    }

    public void getScreenShot(String[] urls) throws URISyntaxException {
        String imageNames = "The screenshots have been saved as: ";
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
                ImageIO.write(screenFullImage, format, new File(fileName));
                dc.saveImage(fileName, imageCount);
                imageCount++;
            }

        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScreenShot.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(null, imageNames);
    }

    public void returnImage(String imageName) {
        BufferedImage im = null;
        try {
            im = dc.retrieveImage(imageName);
            if(im!=null){
                System.out.println("Det finns en bild!!!");
                gui.showImage(im);
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "The image doesn't exist.");
        }
    }

    public static void main(String[] args) {
        ScreenShot sc = new ScreenShot();
    }

}

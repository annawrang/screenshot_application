package screenshot;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class Gui extends JFrame {

    protected ScreenShot ss;
    private ActionHandler ah;

    // gui for main menu
    private JPanel menuPanel = new JPanel();
    private JLabel menu = new JLabel("What would you like to do?", SwingConstants.CENTER);
    protected JButton choiceOne = new JButton("Get a screenshot from a url");
    protected JButton choiceTwo = new JButton("Show screenshot");

    // gui for return Image
    private JPanel returnImagePanel = new JPanel();
    private JLabel enterImageLabel = new JLabel("Please enter the image name");
    protected JTextField enterImageName = new JTextField();
    protected JButton returnImage = new JButton("View image");

    // gui for screenshot
    private JPanel makeScreenShotPanel = new JPanel();
    private JLabel writeUrl = new JLabel("Please write the url of the website");
    protected JTextField websiteEntry = new JTextField();
    protected JButton getScreenShot = new JButton("Get screenshot");

    // gui for viewing image
    private JPanel viewImagePanel = new JPanel();
    protected JButton back = new JButton("Back");
    private JLabel theImage = new JLabel();

    public Gui(ScreenShot ss) {
        this.ss = ss;
        this.ah = new ActionHandler(this);

        menuPanel.setLayout(new GridLayout(3, 1));
        menuPanel.add(menu);
        menuPanel.add(choiceOne);
        menuPanel.add(choiceTwo);
        choiceOne.addActionListener(ah);
        choiceTwo.addActionListener(ah);
        this.add(menuPanel);

        this.setTitle("Screen shot application");
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSmallFrame();
    }

    public void setScreenShotGui() {
        this.remove(menuPanel);
        makeScreenShotPanel.setLayout(new GridLayout(4, 1));
        makeScreenShotPanel.add(writeUrl);
        makeScreenShotPanel.add(websiteEntry);
        makeScreenShotPanel.add(getScreenShot);

        getScreenShot.addActionListener(ah);
        this.add(makeScreenShotPanel);
        this.setSmallFrame();
    }

    public void setReturnImageGui() {
        this.remove(menuPanel);

        returnImagePanel.setLayout(new GridLayout(3, 1));
        returnImagePanel.add(enterImageLabel);
        returnImagePanel.add(enterImageName);
        returnImagePanel.add(returnImage);
        returnImage.addActionListener(ah);

        this.add(returnImagePanel);
        this.setSmallFrame();
    }

    public void showImage(BufferedImage im) {
        this.remove(returnImagePanel);

        JLabel theImage = new JLabel(new ImageIcon(im));
        viewImagePanel.setLayout(new BorderLayout());
        viewImagePanel.add(back, BorderLayout.NORTH);
        viewImagePanel.add(theImage, BorderLayout.CENTER);

        back.addActionListener(ah);

        this.add(viewImagePanel);
        this.setBigFrame();
    }

    public void setSmallFrame() {
        this.setSize(400, 200);
        this.setLocation(800, 600);
        this.revalidate();
        this.repaint();
    }

    public void setBigFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
        this.setLocation(100, 100);
        this.revalidate();
        this.repaint();
    }

}

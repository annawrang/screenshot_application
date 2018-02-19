package screenshot;

import java.awt.event.*;
import java.net.URISyntaxException;
import java.util.logging.*;
import javax.swing.JOptionPane;

public class ActionHandler implements ActionListener {

    private Gui gui;

    public ActionHandler(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gui.choiceOne) {
            gui.setScreenShotGui();
        } else if (e.getSource() == gui.choiceTwo) {
            gui.setReturnImageGui();
        } else if (e.getSource() == gui.getScreenShot) {
            if (gui.websiteEntry == null) {
                JOptionPane.showMessageDialog(null, "You have to enter a url.");
            } else {
                String userIn = gui.websiteEntry.getText();
                String[] urls = userIn.split(";");
                try {
                    gui.ss.getScreenShot(urls);
                    gui.dispose();
                } catch (URISyntaxException ex) {
                    Logger.getLogger(ActionHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (e.getSource() == gui.returnImage) {
            if (gui.enterImageName == null) {
                JOptionPane.showMessageDialog(null, 
                    "You have to enter an image name. (For example \"image5.png\")");
            } else {
                String in = gui.enterImageName.getText();
                gui.ss.returnImage(in);
            }
        }
    }

}

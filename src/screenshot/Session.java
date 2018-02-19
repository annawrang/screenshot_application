package screenshot;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Session implements Serializable{
    protected State state;    
    protected String fileName;
    protected long imageCount;
    protected String imageName;
    protected ImageIcon screenIm;
    protected ImageIcon im;

    public ImageIcon getIm() {
        return im;
    }

    public void setIm(ImageIcon im) {
        this.im = im;
    }

    public State getState() {
        return state;
    }

    public void setState(State IDLE) {
        this.state = IDLE;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getImageCount() {
        return imageCount;
    }

    public void setImageCount(long imageCount) {
        this.imageCount = imageCount;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
}


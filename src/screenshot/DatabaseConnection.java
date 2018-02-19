package screenshot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*  Where all the communication with the database happens */

public class DatabaseConnection {

    Properties p = new Properties();

    public DatabaseConnection() {
        try {
            p.load(new FileInputStream("src/ScreenShot/settings.properties"));
            java.lang.Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getImageNumber() {
        int nr = -1;

        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select max(images.number) from images;");
            while (rs.next()) {
                nr = rs.getInt("max(Images.Number)");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        nr += 1;
        return nr;
    }

    void saveImageNumber(long imageCount) {
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Insert into image(number) values(" + imageCount + ");");

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveImage(ImageIcon ima, String imageName, long number) {
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            BufferedImage buff = new BufferedImage(
                    ima.getIconWidth(),
                    ima.getIconHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = buff.createGraphics();
            ima.paintIcon(null, g, 0, 0);

            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            ImageIO.write(buff, "png", byteArrayOut);
            byte[] imageInByte = byteArrayOut.toByteArray();
            Blob blob = con.createBlob();
            blob.setBytes(1, imageInByte);
            PreparedStatement ps = con.prepareStatement(
                    "Insert into images(image, name, number) values(?,?,?)");
            ps.setBlob(1, blob);
            ps.setString(2, imageName);
            ps.setLong(3, number);

            int result = ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ImageIcon retrieveImage(String imageName) {
        BufferedImage im = null;
        ImageIcon ii = null;
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            Statement prs = con.createStatement();
            ResultSet res = prs.executeQuery(
                    "Select image from images where images.name='" + imageName + "';");
            while (res.next()) {
                im = ImageIO.read(res.getBinaryStream(1));
            }
            ii = new ImageIcon(im);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ii;
    }

}

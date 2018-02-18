package screenshot;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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

    public void saveImage(String imageName, long number) {
        try (Connection con = DriverManager.getConnection(
                p.getProperty("ConnectionString"),
                p.getProperty("username"),
                p.getProperty("password"))) {

            File image = new File(imageName);
            PreparedStatement ps = con.prepareStatement(
                    "Insert into images(image, name, number) values(?,?,?)");
            ps.setString(2, imageName);
            ps.setLong(3, number);

            FileInputStream fi = new FileInputStream(image);
            ps.setBinaryStream(1, (InputStream) fi, (int) (image.length()));

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Uploaded successfully!");
            } else {
                System.out.println("Unsucessful to upload image.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BufferedImage retrieveImage(String imageName) {
        BufferedImage im = null;
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

        } catch (SQLException | IOException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return im;
    }

}

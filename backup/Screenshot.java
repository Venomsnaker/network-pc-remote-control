package features.screenshot;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Screenshot {
    private final String path = "image.png";

    public File takeScreenShot(){
        File img = new File(path);
        img.delete();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = null;
        try {
            capture = new Robot().createScreenCapture(screenRect);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        File imageFile = new File(this.path);
        try {
            ImageIO.write(capture, "png", imageFile );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageFile;
    }

}

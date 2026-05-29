import java.io.*;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;

public class MeowEncoder {
    public static void main(String[] args) throws IOException{
        BufferedImage img = ImageIO.read(new File("test_image.jpg"));
        int height = img.getHeight();
        int width = img.getWidth();
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("test_image.meow")));

        //writing magic bytes
        out.writeBytes("MEOW");

        //writing height and width
        out.writeInt(height);
        out.writeInt(width);

        //writing rgb values
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Color color = new Color(img.getRGB(j, i));

                out.writeByte(color.getRed());
                out.writeByte(color.getGreen());
                out.writeByte(color.getBlue());
            }
        }

        out.close();
    }
}

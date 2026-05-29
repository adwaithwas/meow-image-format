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
        PrintWriter out = new PrintWriter("test_image.meow");

        //writing height and width
        out.println(height + " " + width);

        //writing rgb values
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Color color = new Color(img.getRGB(j, i));

                out.print(color.getRed() + " ");
                out.print(color.getGreen() + " ");
                out.print(color.getBlue() + " ");
            }
        }

        out.close();
    }
}

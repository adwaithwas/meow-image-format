import java.io.*;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;

public class MeowEncoder {
    public static void main(String[] args) throws IOException{
        File inputFile = new File("test_images/test_image.jpg");
        if (!inputFile.exists()) {
            inputFile = new File("test_image.jpg");
        }
        
        File outputFile = new File("test_images/test_image.meow");
        if (!outputFile.getParentFile().exists()) {
            outputFile = new File("test_image.meow");
        }
        
        encode(inputFile, outputFile);
    }

    public static void encode(File inputFile, File outputFile) throws IOException {
        BufferedImage img = ImageIO.read(inputFile);
        if (img == null) {
            throw new IOException("Failed to read image file: " + inputFile.getName());
        }
        encode(img, outputFile);
    }

    public static void encode(BufferedImage img, File outputFile) throws IOException {
        int height = img.getHeight();
        int width = img.getWidth();
        
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
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
        }
    }
}

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;


public class MeowViewer {
    public static int pixelSize = 1;
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("test_image.meow");
        Scanner sc = new Scanner(file);

        final int height = sc.nextInt();
        final int width = sc.nextInt();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[][][] image_data = new int[height][width][3];

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                image_data[i][j][0] = sc.nextInt(); int r = image_data[i][j][0]; //red
                image_data[i][j][1] = sc.nextInt(); int g = image_data[i][j][1]; //green
                image_data[i][j][2] = sc.nextInt(); int b = image_data[i][j][2]; //blue
                
                Color color = new Color(r, g, b);
                image.setRGB(j, i, color.getRGB());
            }
        }

        //displaying image
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                // TODO Auto-generated method stub
                super.paintComponent(g);
                g.drawImage(image, 0, 0, width*pixelSize, height*pixelSize, null);
            }
        };
        
        frame.add(panel);
        frame.setSize(width*pixelSize, height*pixelSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        sc.close();
    }
    
}
import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;


public class MeowViewer {
    public static int pixelSize = 1;
    public static void main(String[] args) throws IOException {
        File file = new File("test_images/test_image.meow");
        if (!file.exists()) {
            file = new File("test_image.meow");
        }
        DataInputStream sc = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        // read magic bytes
        byte[] magic = new byte[4];
        sc.readFully(magic);
        
        final int height = sc.readInt();
        final int width = sc.readInt();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[][][] image_data = new int[height][width][3];

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                image_data[i][j][0] = sc.readUnsignedByte(); int r = image_data[i][j][0]; //red
                image_data[i][j][1] = sc.readUnsignedByte(); int g = image_data[i][j][1]; //green
                image_data[i][j][2] = sc.readUnsignedByte(); int b = image_data[i][j][2]; //blue
                
                Color color = new Color(r, g, b);
                image.setRGB(j, i, color.getRGB());
            }
        }

        //displaying image
        JFrame frame = new JFrame("Meow Viewer");
        ImagePanel panel = new ImagePanel(image);
        
        frame.add(panel);
        frame.setSize(width*pixelSize, height*pixelSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        sc.close();
    }

    static class ImagePanel extends JPanel {
        private final BufferedImage image;
        private double zoomFactor = 1.0;
        private double offsetX = 0;
        private double offsetY = 0;
        private Point lastMousePosition;

        public ImagePanel(BufferedImage image) {
            this.image = image;
            
            // Mouse wheel listener for zooming
            addMouseWheelListener(e -> {
                double oldZoom = zoomFactor;
                if (e.getWheelRotation() < 0) {
                    zoomFactor *= 1.1; // Zoom in
                } else {
                    zoomFactor /= 1.1; // Zoom out
                }
                zoomFactor = Math.max(0.1, Math.min(zoomFactor, 50.0));
                
                double mouseX = e.getX();
                double mouseY = e.getY();
                
                double scaleX = (double) getWidth() / image.getWidth();
                double scaleY = (double) getHeight() / image.getHeight();
                double baseScale = Math.min(scaleX, scaleY);
                
                double oldW = image.getWidth() * baseScale * oldZoom;
                double oldH = image.getHeight() * baseScale * oldZoom;
                
                double oldCenterX = (getWidth() - oldW) / 2 + offsetX;
                double oldCenterY = (getHeight() - oldH) / 2 + offsetY;
                
                double newW = image.getWidth() * baseScale * zoomFactor;
                double newH = image.getHeight() * baseScale * zoomFactor;
                
                offsetX = mouseX - (getWidth() - newW) / 2.0 - (mouseX - oldCenterX) * (zoomFactor / oldZoom);
                offsetY = mouseY - (getHeight() - newH) / 2.0 - (mouseY - oldCenterY) * (zoomFactor / oldZoom);
                
                repaint();
            });

            // Mouse listeners for panning
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastMousePosition = e.getPoint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (lastMousePosition != null) {
                        Point current = e.getPoint();
                        offsetX += current.x - lastMousePosition.x;
                        offsetY += current.y - lastMousePosition.y;
                        lastMousePosition = current;
                        repaint();
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double scaleX = (double) getWidth() / image.getWidth();
            double scaleY = (double) getHeight() / image.getHeight();
            double baseScale = Math.min(scaleX, scaleY);

            double w = image.getWidth() * baseScale * zoomFactor;
            double h = image.getHeight() * baseScale * zoomFactor;

            double x = (getWidth() - w) / 2 + offsetX;
            double y = (getHeight() - h) / 2 + offsetY;

            g2.drawImage(image, (int) x, (int) y, (int) w, (int) h, null);
        }
    }
}
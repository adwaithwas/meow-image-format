import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class MeowImagePanel extends JPanel {
    private BufferedImage image;
    private double zoomFactor = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private Point lastMousePosition;

    public MeowImagePanel(BufferedImage image) {
        this.image = image;
        
        // Mouse wheel listener for zooming
        addMouseWheelListener(e -> {
            if (this.image == null) return;

            double oldZoom = zoomFactor;
            if (e.getWheelRotation() < 0) {
                zoomFactor *= 1.1; // Zoom in
            } else {
                zoomFactor /= 1.1; // Zoom out
            }
            zoomFactor = Math.max(0.1, Math.min(zoomFactor, 50.0));
            
            double mouseX = e.getX();
            double mouseY = e.getY();
            
            double scaleX = (double) getWidth() / this.image.getWidth();
            double scaleY = (double) getHeight() / this.image.getHeight();
            double baseScale = Math.min(scaleX, scaleY);
            
            double oldW = this.image.getWidth() * baseScale * oldZoom;
            double oldH = this.image.getHeight() * baseScale * oldZoom;
            
            double oldCenterX = (getWidth() - oldW) / 2 + offsetX;
            double oldCenterY = (getHeight() - oldH) / 2 + offsetY;
            
            double newW = this.image.getWidth() * baseScale * zoomFactor;
            double newH = this.image.getHeight() * baseScale * zoomFactor;
            
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

    public void setImage(BufferedImage newImage) {
        this.image = newImage;
        // Reset zoom and pan for the new image
        this.zoomFactor = 1.0;
        this.offsetX = 0;
        this.offsetY = 0;
        repaint();
    }

    public BufferedImage getImage() {
        return this.image;
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

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;

public class MeowGallery extends JFrame {
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private MeowImagePanel imagePanel;
    private File currentDirectory;

    public MeowGallery() {
        setTitle("Meow Gallery");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Setup Sidebar for File List
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFile = fileList.getSelectedValue();
                    if (selectedFile != null) {
                        loadImage(new File(currentDirectory, selectedFile));
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(fileList);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.WEST);

        // Setup Main Image Panel
        imagePanel = new MeowImagePanel(null);
        imagePanel.setBackground(Color.DARK_GRAY);
        add(imagePanel, BorderLayout.CENTER);

        // Setup Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openDirItem = new JMenuItem("Open Directory...");
        openDirItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(currentDirectory);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                loadDirectory(chooser.getSelectedFile());
            }
        });
        fileMenu.add(openDirItem);

        JMenuItem exportMeowItem = new JMenuItem("Export as .meow...");
        exportMeowItem.addActionListener(e -> {
            if (imagePanel.getImage() == null) {
                JOptionPane.showMessageDialog(this, "No image is currently loaded to export.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser(currentDirectory);
            chooser.setFileFilter(new FileNameExtensionFilter("Meow Format", "meow"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File fileToSave = chooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".meow")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".meow");
                }
                try {
                    MeowEncoder.encode(imagePanel.getImage(), fileToSave);
                    JOptionPane.showMessageDialog(this, "Image exported successfully to .meow!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh directory if saved in the current directory
                    if (fileToSave.getParentFile() != null && fileToSave.getParentFile().equals(currentDirectory)) {
                        loadDirectory(currentDirectory);
                        fileList.setSelectedValue(fileToSave.getName(), true);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(exportMeowItem);
        
        JMenuItem exportItem = new JMenuItem("Export Current Image...");
        exportItem.addActionListener(e -> {
            if (imagePanel.getImage() == null) {
                JOptionPane.showMessageDialog(this, "No image is currently loaded to export.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser(currentDirectory);
            chooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File fileToSave = chooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
                }
                try {
                    ImageIO.write(imagePanel.getImage(), "PNG", fileToSave);
                    JOptionPane.showMessageDialog(this, "Image exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        fileMenu.add(exportItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Load test_images directory by default if it exists, otherwise current directory
        File defaultDir = new File("test_images");
        if (defaultDir.exists() && defaultDir.isDirectory()) {
            loadDirectory(defaultDir);
        } else {
            loadDirectory(new File("."));
        }
    }

    private void loadDirectory(File dir) {
        if (dir == null || !dir.isDirectory()) return;
        currentDirectory = dir;
        listModel.clear();
        setTitle("Meow Gallery - " + dir.getAbsolutePath());
        
        File[] files = dir.listFiles((d, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".meow") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
        });
        if (files != null) {
            for (File f : files) {
                listModel.addElement(f.getName());
            }
        }
        
        if (!listModel.isEmpty()) {
            fileList.setSelectedIndex(0);
        } else {
            imagePanel.setImage(null);
        }
    }

    private void loadImage(File file) {
        try {
            BufferedImage img;
            if (file.getName().toLowerCase().endsWith(".meow")) {
                img = loadMeow(file);
            } else {
                img = ImageIO.read(file);
            }
            imagePanel.setImage(img);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static BufferedImage loadMeow(File file) throws IOException {
        try (DataInputStream sc = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            // read magic bytes
            byte[] magic = new byte[4];
            sc.readFully(magic);
            String magicStr = new String(magic);
            if (!magicStr.equals("MEOW")) {
                throw new IOException("Not a valid .meow file!");
            }
            
            int height = sc.readInt();
            int width = sc.readInt();

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int r = sc.readUnsignedByte();
                    int g = sc.readUnsignedByte();
                    int b = sc.readUnsignedByte();
                    
                    Color color = new Color(r, g, b);
                    image.setRGB(j, i, color.getRGB());
                }
            }
            return image;
        }
    }

    public static void main(String[] args) {
        try {
            // Set System L&F for a nicer native look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }
        SwingUtilities.invokeLater(() -> {
            new MeowGallery().setVisible(true);
        });
    }
}

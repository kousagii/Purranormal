
package puzzle;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

class BackgroundPanel extends JPanel {
    public Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        setLayout(null); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
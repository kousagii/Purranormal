package puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Portrait extends JPanel {

    private int number;
    private String date;
    private JLabel imageLabel;
    private JLabel nameplateLabel;
    private Point initialClick;
    private PortraitSortFrame parentFrame;
    private Point currentSlotPosition;

    public void setCurrentSlotPosition(Point p) {
        this.currentSlotPosition = new Point(p);
    }

    public Point getCurrentSlotPosition() {
        return currentSlotPosition;
    }
    
    public Portrait(int number, String date, String imagePath) {
        this.number = number;
        this.date = date;

        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(176, 175));

        ImageIcon portraitImage = new ImageIcon(getClass().getResource(imagePath));
        int portraitHeight = 138;
        int portraitWidth = portraitHeight * portraitImage.getIconWidth() / portraitImage.getIconHeight();
        Image scaledPortrait = portraitImage.getImage().getScaledInstance(portraitWidth, portraitHeight, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(scaledPortrait));
        imageLabel.setBounds((176 - portraitWidth) / 2, 0, portraitWidth, portraitHeight);
        add(imageLabel);

        nameplateLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.DARK_GRAY);
                g.setFont(object.SuperObject.pixelFont.deriveFont(32f));
                FontMetrics fm = g.getFontMetrics();
                int dateX = (getWidth() - fm.stringWidth(date)) / 2;
                int dateY = (getHeight() + fm.getAscent()) / 2 - 2;
                g.drawString(date, dateX, dateY);
            }
        };

        ImageIcon nameplateImage = new ImageIcon(getClass().getResource("/portraitgame/Nameplate.png"));
        nameplateLabel.setIcon(nameplateImage);
        nameplateLabel.setBounds((176 - 112) / 2, portraitHeight + 5, 112, 32);
        add(nameplateLabel);

        
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                if (parentFrame != null) {
                    parentFrame.handleDrop(Portrait.this);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = getX();
                int thisY = getY();

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
                getParent().repaint();
            }
        });
    }

    public void setParentFrame(PortraitSortFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }
}

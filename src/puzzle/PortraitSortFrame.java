package puzzle;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PortraitSortFrame extends JDialog {

    private GamePanel gp;
    private ArrayList<Portrait> portraits;
    private JPanel portraitPanel;
    private ArrayList<Point> slots = new ArrayList<>();
    private PuzzleMessageBox messageBox;
    
    private int getSlotIndex(Point point) {
    for (int i = 0; i < slots.size(); i++) {
        if (slots.get(i).equals(point)) {
            return i;
        }
    }
    return -1;
}
    public PortraitSortFrame(GamePanel gp) {
        super(SwingUtilities.getWindowAncestor(gp), Dialog.ModalityType.APPLICATION_MODAL);
        
        portraits = new ArrayList<>();

        portraits.add(new Portrait(1, "1860", "/portraitgame/Cat1.png"));
        portraits.add(new Portrait(2, "1920", "/portraitgame/Cat2.png"));
        portraits.add(new Portrait(3, "2000", "/portraitgame/Cat3.png"));
        portraits.add(new Portrait(4, "1840", "/portraitgame/Cat4.png"));
        portraits.add(new Portrait(5, "1930", "/portraitgame/Cat5.png"));
        portraits.add(new Portrait(6, "1900", "/portraitgame/Cat6.png"));
        
        this.gp = gp;
        setTitle("Portrait Sorting Game");
        setSize(650, 470); 
        setLocationRelativeTo(gp);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
        setLayout(new BorderLayout());

        portraitPanel = new BackgroundPanel("/portraitgame/PortraitWall.png") {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(object.SuperObject.pixelFont);
            g.setColor(main.UiStyle.ACCENT);

            for (int i = 0; i < slots.size(); i++) {
                Point p = slots.get(i);
                g.drawString((i + 1) + "", p.x + 5, p.y + 30); 
            }
        }
    }; 
        
        portraitPanel.setPreferredSize(new Dimension(650, 420));
        portraitPanel.setLayout(null); 
        
        add(portraitPanel, BorderLayout.CENTER);

        for (int i = 0; i < portraits.size(); i++) {
            Portrait p = portraits.get(i);
            p.setParentFrame(this);

            int x = (i % 3) * 200 + 40;
            int y = (i < 3) ? 20 : 195;

            Point slotPosition = new Point(x, y);
            slots.add(slotPosition);

            p.setBounds(x, y, 176, 175);
            p.setCurrentSlotPosition(new Point(x, y)); 
            portraitPanel.add(p);
        }
        
           messageBox = new PuzzleMessageBox(new String[]{
            "Drag and drop the portraits to swap their \npositions.",
            "Sort the portraits by date!"
        });

        messageBox.setVisible(true);
        messageBox.setOnButtonClick(() -> gp.playSound(sound.SoundEffects.Cue.CLICK));
        portraitPanel.add(messageBox);
        portraitPanel.setComponentZOrder(messageBox, 0);
        
        //Back button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(650, 50));
        buttonPanel.setBackground(main.UiStyle.PANEL);
        
        JButton backButton = new JButton("Back");
        backButton.setFont(object.SuperObject.pixelFont);
        backButton.setPreferredSize(new Dimension(140, 40));
        
        backButton.setBackground(main.UiStyle.PANEL); 
        backButton.setForeground(main.UiStyle.TEXT); 
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(main.UiStyle.TEXT, 3));
        backButton.addActionListener(e -> {
            gp.playSound(sound.SoundEffects.Cue.CLICK);
            gp.player.isPuzzleGameActive = false;
            dispose();
        });
        backButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            backButton.setBackground(main.UiStyle.ACCENT);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            backButton.setBackground(main.UiStyle.PANEL); 
        }
        });

        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gp.player.isPuzzleGameActive = false;
            }
        });
    }
    
    public void handleDrop(Portrait draggedPortrait) {
        Rectangle draggedBounds = draggedPortrait.getBounds();
        Point draggedCenter = new Point(
            draggedBounds.x + draggedBounds.width / 2,
            draggedBounds.y + draggedBounds.height / 2
        );

        int snapTolerance = 60;
        int closestIndex = -1;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < slots.size(); i++) {
            Point slot = slots.get(i);
            double distance = draggedCenter.distance(slot.x + 88, slot.y + 88); // center of slot
            if (distance < closestDistance && distance < snapTolerance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }

        if (closestIndex != -1) {
            Point targetSlot = slots.get(closestIndex);
            if (!targetSlot.equals(draggedPortrait.getCurrentSlotPosition())) {
                gp.playSound(sound.SoundEffects.Cue.PORTRAIT);
            }

            Portrait otherPortrait = null;
            for (Portrait p : portraits) {
                if (p != draggedPortrait && p.getCurrentSlotPosition().equals(targetSlot)) {
                    otherPortrait = p;
                    break;
                }
            }

            if (otherPortrait != null) {
                Point originalLocation = draggedPortrait.getCurrentSlotPosition();
                draggedPortrait.setLocation(targetSlot);
                otherPortrait.setLocation(originalLocation);

                draggedPortrait.setCurrentSlotPosition(targetSlot);
                otherPortrait.setCurrentSlotPosition(originalLocation);
            } else {
                draggedPortrait.setLocation(targetSlot);
                draggedPortrait.setCurrentSlotPosition(targetSlot);
            }
        } else {
            draggedPortrait.setLocation(draggedPortrait.getCurrentSlotPosition());
        }

        portraitPanel.repaint();
        checkIfSortedUsingBubbleSort();
    }
    
    public void checkIfSortedUsingBubbleSort() {
        if (gp.player.isPortraitPuzzleSolved) return;
        Portrait[] arranged = new Portrait[6];

        for (Portrait p : portraits) {
            int slotIndex = getSlotIndex(p.getCurrentSlotPosition());
            if (slotIndex != -1) {
                arranged[slotIndex] = p;
            }
        }

        int[] dates = new int[6];
        for (int i = 0; i < arranged.length; i++) {
            if (arranged[i] == null) return; 
            dates[i] = Integer.parseInt(arranged[i].getDate());
        }

        int[] sorted = dates.clone();
        for (int i = 0; i < sorted.length - 1; i++) {
            for (int j = 0; j < sorted.length - i - 1; j++) {
                if (sorted[j] > sorted[j + 1]) {
                    int temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }

        boolean isSorted = true;
        for (int i = 0; i < dates.length; i++) {
            if (dates[i] != sorted[i]) {
                isSorted = false;
                break;
            }
        }

            if (isSorted) {
                gp.playSound(sound.SoundEffects.Cue.PUZZLE);
                gp.player.isPortraitPuzzleSolved = true;
                gp.player.sortedPuzzles++;
                System.out.println(gp.player.sortedPuzzles);
                
                PuzzleDialogs.showCongratulations(this);
                
                for (Portrait p : portraits) {
                    for (MouseListener ml : p.getMouseListeners()) {
                        p.removeMouseListener(ml);
                    }
                    for (MouseMotionListener mml : p.getMouseMotionListeners()) {
                        p.removeMouseMotionListener(mml);
                    }
                }
            }
    }
}


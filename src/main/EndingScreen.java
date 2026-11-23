package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/** The reunion after the owner's final conversation. */
public final class EndingScreen extends JPanel {
    private final BufferedImage cat = UiStyle.image("/player/ghost_cat.png");
    private final BufferedImage owner = UiStyle.image("/npc/ownerdown.png");
    private final Rectangle replay = new Rectangle();
    private final Rectangle exit = new Rectangle();
    private Point pointer = new Point(-1, -1);
    private Runnable onButtonClick = () -> { };

    public void setOnButtonClick(Runnable action) { onButtonClick = action; }

    public EndingScreen(Runnable onReplay, Runnable onExit) {
        setPreferredSize(new Dimension(864, 576));
        setBackground(UiStyle.BACKGROUND);
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (replay.contains(e.getPoint())) { onButtonClick.run(); onReplay.run(); }
                else if (exit.contains(e.getPoint())) { onButtonClick.run(); onExit.run(); }
            }
            @Override public void mouseExited(MouseEvent e) { pointer = new Point(-1, -1); repaint(); }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { pointer = e.getPoint(); repaint(); }
        });
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        int center = getWidth() / 2;
        g.setColor(UiStyle.ACCENT);
        g.setFont(UiStyle.font(66));
        UiStyle.centered(g, "Together, again", center, 102);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int ownerHeight = 124;
        int ownerWidth = owner.getWidth() * ownerHeight / owner.getHeight();
        int catHeight = 78;
        int catWidth = cat.getWidth() * catHeight / cat.getHeight();
        g.drawImage(owner, center - ownerWidth - 10, 140, ownerWidth, ownerHeight, null);
        g.drawImage(cat, center + 10, 264 - catHeight, catWidth, catHeight, null);
        g.setColor(UiStyle.BORDER);
        g.fillRect(center - 104, 282, 208, 2);
        g.setFont(UiStyle.font(30));
        g.setColor(UiStyle.TEXT);
        UiStyle.centered(g, "The house held the life you shared.", center, 328);
        UiStyle.centered(g, "Your owner has come to take you home.", center, 366);
        g.setColor(UiStyle.MUTED);
        UiStyle.centered(g, "Together, you leave the past behind.", center, 404);
        replay.setBounds(center - 220, 458, 208, 52);
        exit.setBounds(center + 12, 458, 208, 52);
        g.setFont(UiStyle.font(36));
        UiStyle.button(g, replay, "Play Again", replay.contains(pointer));
        UiStyle.button(g, exit, "Exit", exit.contains(pointer));
        g.dispose();
    }
}


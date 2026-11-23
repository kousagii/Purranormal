package main;

import data.DataStorage;
import data.SaveGameService;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import javax.swing.*;

public class TitleScreen extends JPanel {
    private final Runnable onStartGame;
    private final Consumer<DataStorage> onLoadGame;
    private Runnable onSettingsClick = () -> { };
    private Runnable onButtonClick = () -> { };
    private final SaveGameService saves = new SaveGameService(Path.of("savegame.dat"));
    private final BufferedImage ghostCat = UiStyle.image("/player/ghost_cat.png");
    private final Rectangle[] buttons = { new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle() };
    private final Rectangle yesButton = new Rectangle();
    private final Rectangle noButton = new Rectangle();
    private final Rectangle backButton = new Rectangle();
    private Point pointer = new Point(-1, -1);
    private boolean showNewGameConfirmation;
    private String errorMessage;

    public TitleScreen(Runnable onStartGame, Consumer<DataStorage> onLoadGame) {
        this.onStartGame = onStartGame;
        this.onLoadGame = onLoadGame;
        setPreferredSize(new Dimension(864, 576));
        setBackground(UiStyle.BACKGROUND);
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { click(e.getPoint()); }
            @Override public void mouseExited(MouseEvent e) { pointer = new Point(-1, -1); repaint(); }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { pointer = e.getPoint(); repaint(); }
        });
    }

    public void setOnSettingsClick(Runnable action) { onSettingsClick = action; }
    public void setOnButtonClick(Runnable action) { onButtonClick = action; }

    private boolean hit(Rectangle bounds, Point point) {
        if (!bounds.contains(point)) return false;
        onButtonClick.run();
        return true;
    }

    private void click(Point p) {
        if (errorMessage != null) {
            if (hit(backButton, p)) errorMessage = null;
        } else if (showNewGameConfirmation) {
            if (hit(yesButton, p)) {
                showNewGameConfirmation = false;
                onStartGame.run();
            } else if (hit(noButton, p)) showNewGameConfirmation = false;
        } else if (hit(buttons[0], p)) {
            showNewGameConfirmation = true;
        } else if (hit(buttons[1], p)) {
            loadGame();
        } else if (hit(buttons[2], p)) {
            onSettingsClick.run();
        } else if (hit(buttons[3], p)) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
        repaint();
    }

    private void loadGame() {
        if (!saves.exists()) { errorMessage = "There is no saved game yet."; return; }
        try {
            onLoadGame.accept(saves.load());
        } catch (IOException | IllegalArgumentException e) {
            errorMessage = "This save could not be loaded. Keep a backup and check the README for help.";
            System.err.println("Load failed: " + e.getMessage());
        }
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        int center = getWidth() / 2;
        g.setColor(UiStyle.BORDER);
        g.fillRect(center - 30, 44, 60, 2);
        g.setColor(UiStyle.TEXT);
        g.setFont(UiStyle.font(80));
        UiStyle.centered(g, "Purranormal", center, 126);
        g.setColor(UiStyle.ACCENT);
        g.setFont(UiStyle.font(34));
        UiStyle.centered(g, "A Cat's Tale", center, 166);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int height = 104;
        int width = ghostCat.getWidth() * height / ghostCat.getHeight();
        g.drawImage(ghostCat, center - width / 2, 188, width, height, null);
        String[] labels = { "New Game", "Load Game", "Settings", "Quit" };
        g.setFont(UiStyle.font(36));
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(center - 150, 316 + i * 57, 300, 46);
            UiStyle.button(g, buttons[i], labels[i], buttons[i].contains(pointer));
        }
        if (showNewGameConfirmation || errorMessage != null) {
            g.setColor(new Color(8, 5, 14, 190));
            g.fillRect(0, 0, getWidth(), getHeight());
            Rectangle panel = new Rectangle(center - 280, 174, 560, 228);
            UiStyle.panel(g, panel);
            g.setFont(UiStyle.font(32));
            g.setColor(UiStyle.TEXT);
            if (showNewGameConfirmation) {
                UiStyle.centered(g, "Start a new journey?", center, panel.y + 46);
                g.setFont(UiStyle.font(28));
                UiStyle.wrapped(g, "Your saved game stays until you save again.",
                        panel.x + 30, panel.y + 88, panel.width - 60);
                yesButton.setBounds(center - 156, panel.y + 154, 140, 46);
                noButton.setBounds(center + 16, panel.y + 154, 140, 46);
                UiStyle.button(g, yesButton, "Yes", yesButton.contains(pointer));
                UiStyle.button(g, noButton, "No", noButton.contains(pointer));
            } else {
                UiStyle.wrapped(g, errorMessage, panel.x + 30, panel.y + 48, panel.width - 60);
                backButton.setBounds(center - 70, panel.y + 154, 140, 46);
                UiStyle.button(g, backButton, "Back", backButton.contains(pointer));
            }
        }
        g.dispose();
    }
}


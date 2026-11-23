package puzzle;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.UiStyle;

/** Shared, bounded tutorial panel for both sorting games. */
public class PuzzleMessageBox extends JPanel {
    private final String[] lines;
    private int currentLine;
    private final Rectangle buttonBounds = new Rectangle(450, 66, 112, 34);
    private boolean hover;
    private OnFinishListener onFinishListener;
    private Runnable onButtonClick = () -> { };
    public void setOnButtonClick(Runnable action) { onButtonClick = action; }

    public interface OnFinishListener { void onFinish(); }
    public void setOnFinishListener(OnFinishListener listener) { onFinishListener = listener; }

    public PuzzleMessageBox(String[] lines) {
        this.lines = lines;
        setOpaque(false);
        setBounds(30, 282, 588, 116);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                hover = buttonBounds.contains(e.getPoint());
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (buttonBounds.contains(e.getPoint())) { onButtonClick.run(); nextLine(); }
            }
            @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        });
    }

    public void nextLine() {
        if (currentLine < lines.length - 1) { currentLine++; repaint(); }
        else {
            setVisible(false);
            if (onFinishListener != null) onFinishListener.onFinish();
        }
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        UiStyle.panel(g, new Rectangle(0, 0, getWidth(), getHeight()));
        g.setFont(UiStyle.font(28));
        g.setColor(UiStyle.TEXT);
        UiStyle.wrapped(g, lines[currentLine], 18, 28, getWidth() - 36);
        UiStyle.button(g, buttonBounds, currentLine == lines.length - 1 ? "Finish" : "Next", hover);
        g.dispose();
    }
}


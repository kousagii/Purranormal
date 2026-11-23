package puzzle;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.UiStyle;

/** Shared completion dialog for both sorting games. */
final class PuzzleDialogs {
    private PuzzleDialogs() { }

    static void showCongratulations(Window owner) {
        JDialog dialog = new JDialog(owner, "Congratulations", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(owner);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel content = new JPanel() {
            @Override protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(new Color(0, 0, 0, 200));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g.setColor(UiStyle.TEXT);
                g.setStroke(new BasicStroke(4));
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g.dispose();
            }
        };
        content.setOpaque(false);
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("You did it!", SwingConstants.CENTER);
        label.setFont(UiStyle.font(60));
        label.setForeground(UiStyle.TEXT);
        content.add(label, BorderLayout.CENTER);

        JButton ok = new JButton("OK");
        ok.setFont(UiStyle.font(40));
        ok.setFocusPainted(false);
        ok.setBorderPainted(false);
        ok.setContentAreaFilled(true);
        ok.setOpaque(true);
        ok.setBackground(new Color(0, 0, 0, 200));
        ok.setForeground(UiStyle.TEXT);
        ok.setBorder(BorderFactory.createLineBorder(UiStyle.TEXT, 2));
        ok.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                ok.setBackground(new Color(50, 50, 50, 220));
            }
            @Override public void mouseExited(MouseEvent e) {
                ok.setBackground(new Color(0, 0, 0, 200));
            }
        });
        ok.addActionListener(e -> dialog.dispose());
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(ok);
        content.add(buttons, BorderLayout.SOUTH);
        dialog.setContentPane(content);
        dialog.setVisible(true);
    }
}


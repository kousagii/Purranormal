package main;

import java.awt.*;
import java.util.Hashtable;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Settings extends JPanel {
    private final JSlider volumeSlider = new JSlider(0, 100, 45);
    private Runnable onButtonClick = () -> { };

    public Settings(Runnable onBack, Consumer<Float> onVolumeChange) {
        setPreferredSize(new Dimension(864, 576));
        setBackground(UiStyle.BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel card = new JPanel();
        card.setBackground(UiStyle.PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiStyle.BORDER, 2), new EmptyBorder(32, 44, 32, 44)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Settings");
        title.setFont(UiStyle.font(58));
        title.setForeground(UiStyle.ACCENT);
        title.setAlignmentX(CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(32));
        JLabel label = new JLabel("Music Volume");
        label.setFont(UiStyle.font(36));
        label.setForeground(UiStyle.TEXT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        card.add(label);
        card.add(Box.createVerticalStrut(16));
        volumeSlider.setPreferredSize(new Dimension(480, 86));
        volumeSlider.setMaximumSize(new Dimension(480, 86));
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setOpaque(false);
        volumeSlider.setForeground(UiStyle.TEXT);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for (int i = 0; i <= 100; i += 25) {
            JLabel tick = new JLabel(String.valueOf(i));
            tick.setFont(UiStyle.font(24));
            tick.setForeground(UiStyle.MUTED);
            labels.put(i, tick);
        }
        volumeSlider.setLabelTable(labels);
        volumeSlider.addChangeListener(e -> onVolumeChange.accept(volumeSlider.getValue() / 100f));
        card.add(volumeSlider);
        card.add(Box.createVerticalStrut(36));
        JButton back = new JButton("Back");
        back.setFont(UiStyle.font(36));
        back.setForeground(UiStyle.TEXT);
        back.setBackground(new Color(41, 32, 52));
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiStyle.BORDER, 2), new EmptyBorder(8, 60, 8, 60)));
        back.setAlignmentX(CENTER_ALIGNMENT);
        back.addActionListener(e -> { onButtonClick.run(); onBack.run(); });
        card.add(back);
        add(card);
    }

    public void setOnButtonClick(Runnable action) { onButtonClick = action; }
    public void setVolume(int value) { volumeSlider.setValue(value); }
}


package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/** Shared pixel font, spacing and contrast for the existing artwork. */
public final class UiStyle {
    public static final Color BACKGROUND = new Color(17, 14, 24);
    public static final Color PANEL = new Color(30, 24, 40, 248);
    public static final Color BORDER = new Color(153, 128, 166);
    public static final Color TEXT = new Color(247, 240, 226);
    public static final Color MUTED = new Color(194, 181, 203);
    public static final Color ACCENT = new Color(236, 192, 123);
    private static final Font FONT = loadFont();

    private UiStyle() { }

    private static Font loadFont() {
        try (InputStream stream = UiStyle.class.getResourceAsStream("/font/ByteBounce.ttf")) {
            if (stream == null) throw new IOException("Missing pixel font");
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (IOException | FontFormatException e) {
            return new Font(Font.MONOSPACED, Font.PLAIN, 24);
        }
    }

    public static Font font(float size) { return FONT.deriveFont(size); }

    public static BufferedImage image(String path) {
        try (InputStream stream = UiStyle.class.getResourceAsStream(path)) {
            if (stream == null) throw new IOException("Missing resource: " + path);
            BufferedImage image = ImageIO.read(stream);
            if (image == null) throw new IOException("Invalid image: " + path);
            return image;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static void panel(Graphics2D g, Rectangle bounds) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(bounds.x + 5, bounds.y + 5, bounds.width, bounds.height);
        g.setColor(PANEL);
        g.fill(bounds);
        g.setStroke(new BasicStroke(2));
        g.setColor(BORDER);
        g.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
    }

    public static void centered(Graphics2D g, String text, int centerX, int baseline) {
        g.drawString(text, centerX - g.getFontMetrics().stringWidth(text) / 2, baseline);
    }

    public static void button(Graphics2D g, Rectangle bounds, String label, boolean hovered) {
        g.setColor(hovered ? new Color(74, 55, 83) : new Color(41, 32, 52));
        g.fill(bounds);
        g.setColor(hovered ? ACCENT : BORDER);
        g.setStroke(new BasicStroke(2));
        g.drawRect(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        g.setColor(TEXT);
        FontMetrics fm = g.getFontMetrics();
        centered(g, label, bounds.x + bounds.width / 2,
                bounds.y + (bounds.height - fm.getHeight()) / 2 + fm.getAscent());
    }

    /** Wrap at words and honor authored line breaks. */
    public static int wrapped(Graphics2D g, String text, int x, int y, int width) {
        FontMetrics fm = g.getFontMetrics();
        for (String paragraph : text.split("\n", -1)) {
            String line = "";
            for (String word : paragraph.trim().split("\\s+")) {
                String next = line.isEmpty() ? word : line + " " + word;
                if (!line.isEmpty() && fm.stringWidth(next) > width) {
                    g.drawString(line, x, y);
                    y += fm.getHeight();
                    line = word;
                } else line = next;
            }
            g.drawString(line, x, y);
            y += fm.getHeight();
        }
        return y;
    }
}


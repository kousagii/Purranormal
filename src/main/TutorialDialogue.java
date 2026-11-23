package main;

import java.awt.*;

public class TutorialDialogue {
    private final GamePanel gp;
    private int currentLine;
    private final String[] lines = {
        "Use WASD to move.",
        "Press Space to interact with objects.",
        "Solve puzzles by sorting them and have fun!"
    };
    private boolean active = true;

    public TutorialDialogue(GamePanel gp) { this.gp = gp; }

    public void draw(Graphics2D g) {
        if (!active) return;
        Rectangle panel = new Rectangle(40, gp.screenHeight - 150, gp.screenWidth - 80, 120);
        UiStyle.panel(g, panel);
        g.setFont(UiStyle.font(32));
        g.setColor(UiStyle.TEXT);
        UiStyle.wrapped(g, lines[currentLine], panel.x + 24, panel.y + 36, panel.width - 48);
        Rectangle button = getButtonBounds();
        g.setFont(UiStyle.font(28));
        UiStyle.button(g, button, currentLine == lines.length - 1 ? "Finish" : "Next",
                button.contains(gp.mouseHandler.mouseX, gp.mouseHandler.mouseY));
    }

    public Rectangle getButtonBounds() {
        return new Rectangle(gp.screenWidth - 184, gp.screenHeight - 90, 120, 40);
    }

    public void nextLine() {
        if (currentLine < lines.length - 1) currentLine++;
        else finish();
    }

    public boolean isActive() { return active; }
    public void start() { active = true; currentLine = 0; }
    public void finish() { active = false; }
}


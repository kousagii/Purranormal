package object;

import java.awt.*;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.UiStyle;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision;
    public int worldX, worldY;
    public int width = 48, height = 48;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public boolean interactable;
    public String currentDialogue = "Default Text";
    public String[] dialogues = new String[20];
    public static final Font pixelFont = UiStyle.font(48);
    public boolean showPickupDialogue;
    public final Rectangle yesButton = new Rectangle();
    public final Rectangle noButton = new Rectangle();
    public Runnable onYesAction;
    public Runnable onNoAction;

    public void interact(GamePanel gp) { }
    public void onDialogueClosed(GamePanel gp) { }
    public void update() { }

    public void showDialogueScreen(GamePanel gp, Graphics2D g) {
        Rectangle panel = new Rectangle(40, gp.screenHeight - 202, gp.screenWidth - 80, 172);
        UiStyle.panel(g, panel);
        g.setColor(UiStyle.TEXT);
        g.setFont(UiStyle.font(32));
        UiStyle.wrapped(g, currentDialogue, panel.x + 24, panel.y + 36, panel.width - 48);
        if (showPickupDialogue) {
            yesButton.setBounds(panel.x + panel.width - 272, panel.y + 112, 112, 40);
            noButton.setBounds(panel.x + panel.width - 136, panel.y + 112, 112, 40);
            g.setFont(UiStyle.font(28));
            UiStyle.button(g, yesButton, "Yes", yesButton.contains(gp.mouseHandler.mouseX, gp.mouseHandler.mouseY));
            UiStyle.button(g, noButton, "No", noButton.contains(gp.mouseHandler.mouseX, gp.mouseHandler.mouseY));
        }
    }


    public void draw(Graphics2D g, GamePanel gp) {
        int x = worldX - (int) gp.player.worldX + gp.player.screenX;
        int y = worldY - (int) gp.player.worldY + gp.player.screenY;
        if (image != null && x + width > 0 && x < gp.screenWidth && y + height > 0 && y < gp.screenHeight) {
            g.drawImage(image, x, y, width, height, null);
        }
    }
}


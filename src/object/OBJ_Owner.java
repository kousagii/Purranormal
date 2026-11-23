package object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.UiStyle;

/** The owner returns for Coby after the cat has revisited their shared memories. */
public class OBJ_Owner extends SuperObject {
    private final BufferedImage[] images = {
        UiStyle.image("/npc/ownerdown.png"), UiStyle.image("/npc/ownerup.png")
    };
    private final String[] reunion = {
        "You finally made it, Coby. I've been waiting for you.",
        "I passed on before you. Then your time came, too.",
        "You wandered through our home, remembering the life we shared.",
        "But you don't have to stay here alone anymore. I came back for you.",
        "Come with me, little one. It's time for us to go home."
    };
    private int line;
    private int animationTick;

    public OBJ_Owner(int width, int height) {
        name = "Owner";
        interactable = true;
        collision = true;
        this.width = width;
        this.height = height;
        solidArea = new Rectangle(0, 0, 96, 96);
        image = images[0];
    }

    @Override public void interact(GamePanel gp) {
        if (gp.player.isDialogueVisible) return;
        currentDialogue = reunion[line];
        gp.player.currentDialogueObject = this;
        gp.player.setDialogueVisible(true);
    }

    @Override public void onDialogueClosed(GamePanel gp) {
        if (++line == reunion.length) gp.completeGame();
        else interact(gp);
    }

    @Override public void update() {
        image = images[(++animationTick / 16) % images.length];
    }
}


package object;

import entity.Player;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_GhostDog extends SuperObject {
    private int spriteCounter = 0;
    private int spriteNum = 1; 
    private BufferedImage[] ghostDogImages = new BufferedImage[2];
    private int dialogueIndex = 0; 
    

    public OBJ_GhostDog(int width, int height) {
        name = "GhostDog"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        
        try {
            ghostDogImages[0] = ImageIO.read(getClass().getResourceAsStream("/npc/GhostDogLeft.png"));
            ghostDogImages[1] = ImageIO.read(getClass().getResourceAsStream("/npc/GhostDogRight.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        image = ghostDogImages[0]; 
        
        solidArea = new Rectangle(0, 0, 48, 48);    
        collision = true;
        
        dialogues[0] = "Don't forget to pick up the portraits \nyou pass through!";
        dialogues[1] = "Arf!";
        dialogues[2] = "You'll be needing that in the \nportrait hall.";

    } 
    
    @Override
    public void interact(GamePanel gp) {
            Player player = gp.player;

            if (player.isDialogueVisible) {
                player.setDialogueVisible(false);
            } else {

                player.setDialogueVisible(true);
                currentDialogue = dialogues[dialogueIndex];
                gp.player.currentDialogueObject = this;
                
                if (dialogueIndex < dialogues.length - 1 && dialogues[dialogueIndex + 1] != null) {
                    dialogueIndex++;
                }
            }
        }

    public void update() {
        spriteCounter++;
        
        if (spriteCounter > 15) {
            if (spriteNum == 0) {
                spriteNum = 1;
            } else {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
        
        image = ghostDogImages[spriteNum];
    }
    
    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - (int)gp.player.worldX + gp.player.screenX;
        int screenY = worldY - (int)gp.player.worldY + gp.player.screenY;
        
        if (screenX + width > 0 && screenX - width < gp.screenWidth &&
            screenY + height > 0 && screenY - height < gp.screenHeight) {
            g2.drawImage(image, screenX, screenY, width, height, null);
        }
             
        
    }
    
}



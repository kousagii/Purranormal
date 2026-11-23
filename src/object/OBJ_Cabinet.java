package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Cabinet extends SuperObject {
    
 public OBJ_Cabinet(int width, int height) {
        name = "Cabinet"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48*3, 48*3);    
        collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "Cabinet";
   }
}

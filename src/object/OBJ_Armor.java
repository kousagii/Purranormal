package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_Armor extends SuperObject {
    
 public OBJ_Armor(int width, int height) {
        name = "Armor"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48, 48*3);    
        collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "Armor";
   }
}
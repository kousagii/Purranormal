package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_LongTable extends SuperObject {
    
 public OBJ_LongTable(int width, int height) {
        name = "LongTable"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48, 48*5);    
        collision = true;

    } 

   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "A table with an assortment of \nthings.";
   }
}

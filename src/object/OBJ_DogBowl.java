package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_DogBowl extends SuperObject {
    
 public OBJ_DogBowl(int width, int height) {
        name = "DogBowl"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48, 48);    
        collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "Bowl";
   }
}
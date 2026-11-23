package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_DogHouse extends SuperObject {
    
 public OBJ_DogHouse(int width, int height) {
        name = "DogHouse"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48*3, 48*2);    
        collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "Doghouse";
   }
}
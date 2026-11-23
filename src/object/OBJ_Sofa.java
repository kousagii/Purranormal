package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_Sofa extends SuperObject {
    
 public OBJ_Sofa(int width, int height) {
        name = "Sofa"; 
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
       currentDialogue = "I remember we used to hangout \nwith my person here.";
   }
}
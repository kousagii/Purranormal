package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_SideSofa extends SuperObject {
    
 public OBJ_SideSofa(int width, int height) {
        name = "SideSofa"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48*2, 48*4);    
        collision = true;
    } 
 
    @Override
    public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "A sofa.";
   }
}

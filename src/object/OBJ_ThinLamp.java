package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_ThinLamp extends SuperObject {
    
 public OBJ_ThinLamp(int width, int height) {
        name = "ThinLamp"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/Thinlamp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            solidArea = new Rectangle(0, 0, 48, 48*4);    
            collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "A shiny lamp.";
   }
}

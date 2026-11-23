package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TVBack extends SuperObject {
    
 public OBJ_TVBack(int width, int height) {
        name = "TVBack"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/tvBack.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            solidArea = new Rectangle(0, 0, 48*2, 48*2);    
            collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "A TV.";
   }
}

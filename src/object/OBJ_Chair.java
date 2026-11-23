package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Chair extends SuperObject {
    
 public OBJ_Chair(int width, int height) {
        name = "Chair"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chair.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            solidArea = new Rectangle(0, 0, 48, 48*2);    
            collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "A chair.";
   }
}

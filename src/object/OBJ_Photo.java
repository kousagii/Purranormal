package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Photo extends SuperObject {
    
 public OBJ_Photo(int width, int height) {
        name = "Photo"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/Photo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            solidArea = new Rectangle(0, 0, 48, 48);    
            collision = true;
    } 
    @Override
   public void interact(GamePanel gp) {
       gp.player.setDialogueVisible(true);
       gp.player.currentDialogueObject = this;
       currentDialogue = "There's a photo on the ground. \nPick up photo?";
       showPickupDialogue = true;
       
       onYesAction = () -> {
        gp.playSound(sound.SoundEffects.Cue.PICKUP);
        gp.player.hasPhoto++;
        gp.player.removeObject(this);
        System.out.println("Photo count: " + gp.player.hasPhoto);
        };
   }
}

package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_ShadowDoor extends SuperObject {
    
 public OBJ_ShadowDoor(int width, int height) {
        name = "ShadowDoor"; 
        interactable = false;
        this.width = width; 
        this.height = height; 
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door_shadow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            solidArea = new Rectangle(0, 0, 48*2, 48);    
            collision = true;
    } 
}
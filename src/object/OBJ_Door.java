package object;

import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Door extends SuperObject {
    
 public OBJ_Door(int width, int height) {
        name = "Door"; 
        interactable = false;
        this.width = width; 
        this.height = height; 
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
            solidArea = new Rectangle(0, 0, 48*2, 48*3);    
            collision = true;
    } 
}

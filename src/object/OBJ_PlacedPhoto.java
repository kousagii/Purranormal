    package object;

    import javax.imageio.ImageIO;
    import java.io.IOException;
    import java.awt.image.BufferedImage;

    public class OBJ_PlacedPhoto extends SuperObject {

        public int index;
        
        public OBJ_PlacedPhoto(int index, int width, int height) {
            name = "PlacedPhoto" + index;
            this.index = index; 
            this.width = width;
            this.height = height;
            this.collision = true;

            try {
                image = ImageIO.read(getClass().getResourceAsStream("/objects/portrait_cat_" + index + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
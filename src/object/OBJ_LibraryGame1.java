package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_LibraryGame1 extends SuperObject {
    
 public OBJ_LibraryGame1(int width, int height) {
        name = "LibraryGame1"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48*5, 48);    
        collision = true;
        }
 @Override 
 public void interact(GamePanel gp) {
            
        if (gp.player.isDialogueVisible) return;
       
        if (gp.player.isLibraryPuzzleSolved1) {
        currentDialogue = "The books are already sorted.";
        gp.player.currentDialogueObject = this;
        gp.player.setDialogueVisible(true);
        return;
        }

        gp.player.isPuzzleGameActive = true;
        gp.keyH.resetKeys();
        try {
            new puzzle.BookSortFrame(gp, false).setVisible(true);
        } finally {
            gp.player.isPuzzleGameActive = false;
            gp.keyH.resetKeys();
            gp.requestFocusInWindow();
        }

 }
    } 


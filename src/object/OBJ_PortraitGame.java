package object;

import java.awt.Rectangle;
import main.GamePanel;

public class OBJ_PortraitGame extends SuperObject {
    
 public OBJ_PortraitGame(int width, int height) {
        name = "PortraitGame"; 
        interactable = true;
        this.width = width; 
        this.height = height; 
        image = null;
        solidArea = new Rectangle(0, 0, 48*6, 48);    
        collision = true;

    } 

    @Override
    public void interact(GamePanel gp) {
        if (gp.player.isDialogueVisible) return;

    if (gp.player.isPortraitPuzzleSolved) {
               currentDialogue = "The portraits are successfully \nsorted.";
               gp.player.currentDialogueObject = this;
               gp.player.setDialogueVisible(true);

               int[][] sortedPositions = {
                   {12, 2}, {20, 2}, {24, 2}, {10, 2}, {22, 2}, {14, 2}
               };

               for (int i = 0; i < sortedPositions.length; i++) {
                   int tileX = sortedPositions[i][0] * gp.tileSize;
                   int tileY = sortedPositions[i][1] * gp.tileSize;
                   gp.aSetter.placePlacedPhoto(10 + i, 3, tileX, tileY, i + 1);
               }

               return;
    }

        if (gp.player.placedPhotos >= 6 && gp.player.hasPhoto == 0) {
            gp.player.isPuzzleGameActive = true;
            gp.keyH.resetKeys();
            try {
                new puzzle.PortraitSortFrame(gp).setVisible(true);
            } finally {
                gp.player.isPuzzleGameActive = false;
                gp.keyH.resetKeys();
                gp.requestFocusInWindow();
            }
        } else if (gp.player.hasPhoto > 0) {
            currentDialogue = "Put Photo?";
            gp.player.currentDialogueObject = this;
            gp.player.setDialogueVisible(true);
            showPickupDialogue = true;

            onYesAction = () -> {
                gp.playSound(sound.SoundEffects.Cue.PLACE);
                gp.player.hasPhoto--;
                gp.player.placedPhotos++;
                System.out.println("Photo placed: " + gp.player.placedPhotos);

                int[][] positions = {
                    {10, 2}, {12, 2}, {14, 2}, {20, 2}, {22, 2}, {24, 2}
                };

                int index = gp.player.placedPhotos - 1;
                if (index >= 0 && index < positions.length) {
                    int tileX = positions[index][0] * gp.tileSize;
                    int tileY = positions[index][1] * gp.tileSize;
                    gp.aSetter.placePlacedPhoto(10 + index, 3, tileX, tileY, index + 1);
                }
            };
        } else {
            currentDialogue = "Some photos are missing.";
            gp.player.currentDialogueObject = this;
            gp.player.setDialogueVisible(true);
            showPickupDialogue = false;
        }
    }
}


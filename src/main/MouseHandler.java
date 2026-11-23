package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import object.SuperObject;

public class MouseHandler extends MouseAdapter {
    GamePanel gp;
    public int mouseX, mouseY;
    
    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gp.player.isDialogueVisible && gp.player.currentDialogueObject != null) {
            SuperObject obj = gp.player.currentDialogueObject;
            if (obj.showPickupDialogue) {
                int mx = e.getX();
                int my = e.getY();

                if (obj.yesButton.contains(mx, my)) {
                    gp.playSound(sound.SoundEffects.Cue.CLICK);
                    if (obj.onYesAction != null) {
                         obj.onYesAction.run(); 
                     }
                    obj.showPickupDialogue = false;
                    gp.player.setDialogueVisible(false);
                    gp.player.currentDialogueObject = null;
                } else if (obj.noButton.contains(mx, my)) {
                    gp.playSound(sound.SoundEffects.Cue.CLICK);
                       if (obj.onNoAction != null) {
                            obj.onNoAction.run(); 
                    }
                    obj.showPickupDialogue = false;
                    gp.player.setDialogueVisible(false);
                    gp.player.currentDialogueObject = null;
                }
            }
        }
        
    }
    
    public void mouseMoved(MouseEvent e) {     
        mouseX = e.getX();
        mouseY = e.getY();
    }


}

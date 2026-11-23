
package main;

import java.awt.event.*;


public class KeyHandler implements KeyListener{
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, pausePressed;
    private boolean escapeHeld;
    private boolean spaceHeld;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }
        
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }        
        
        if(code == KeyEvent.VK_SPACE) {
            if (!spaceHeld) spacePressed = true;
            spaceHeld = true;
        }
        
        if(code == KeyEvent.VK_ESCAPE) {
            if (!escapeHeld) pausePressed = !pausePressed;
            escapeHeld = true;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) escapeHeld = false;
        
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }        
        
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = false;
            spaceHeld = false;
        }
    }
    
        public void resetKeys() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        spacePressed = false;
        spaceHeld = false;
        escapeHeld = false;
        pausePressed = false;
    }
}

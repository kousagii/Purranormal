
package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import object.SuperObject;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public boolean isDialogueVisible = false;
    public SuperObject currentDialogueObject = null;

    public boolean isPuzzleGameActive = false;
    public boolean isPortraitPuzzleSolved = false;
    public boolean isLibraryPuzzleSolved1 = false;
    public boolean isLibraryPuzzleSolved2 = false;
    public int sortedPuzzles = 0;

    public int hasPhoto = 0;
    public int placedPhotos = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = gp.tileSize - 16;
        solidArea.height = gp.tileSize - 16;

        setDefaultValues();
        getPlayerImage();
    }

    public void getPlayerImage() {
        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/player/cat_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/cat_up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/cat_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/cat_down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/cat_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/cat_left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/cat_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/cat_right2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 33;
        worldY = gp.tileSize * 47;
        speed = 5;
        direction = "up";
    }

    public void setDialogueVisible(boolean visible) {
        isDialogueVisible = visible;
    }

    public void update() {

        if (isDialogueVisible) {
            if (keyH.spacePressed) {
                SuperObject closedDialogue = currentDialogueObject;
                isDialogueVisible = false;
                currentDialogueObject = null;
                keyH.spacePressed = false;
                if (closedDialogue != null) {
                    closedDialogue.showPickupDialogue = false;
                    closedDialogue.onDialogueClosed(gp);
                }
            }
            return;
        }

        if (isPuzzleGameActive) {
            keyH.upPressed = false;
            return;
        }

        if (keyH.spacePressed) {
            int objIndex = gp.cChecker.checkInteractionObject(this);
            if (objIndex != 999 && gp.obj[gp.currentMap][objIndex] != null) {
                gp.playSound(sound.SoundEffects.Cue.INTERACT);
                gp.obj[gp.currentMap][objIndex].interact(gp);
            }
            keyH.spacePressed = false;
            if (isDialogueVisible || isPuzzleGameActive)
                return;
        }

        if (keyH.upPressed == true || keyH.downPressed == true
                || keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
                direction = "up";
            } else if (keyH.downPressed == true) {
                direction = "down";
            } else if (keyH.leftPressed == true) {
                direction = "left";
            } else if (keyH.rightPressed == true) {

                direction = "right";
            }

            // check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // check object collision
            int previousMap = gp.currentMap;
            int objIndex = gp.cChecker.checkObject(this, true);
            touchObject(objIndex);
            if (gp.currentMap != previousMap || isDialogueVisible)
                return;

            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            spriteCounter++;
            if (spriteCounter > 15) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void removeObject(SuperObject obj) {
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] == obj) {
                gp.obj[gp.currentMap][i] = null;
                break;
            }
        }
    }

    public void touchObject(int i) {
        if (i != 999 && gp.obj[gp.currentMap][i] != null) {
            String objectName = gp.obj[gp.currentMap][i].name;

            if ("Door".equals(objectName)) {
                switch (gp.currentMap) {
                    case 0: // From hallway to library
                        if (i == 0) {
                            gp.switchMap(2);
                            gp.player.worldX = 8.5 * gp.tileSize;
                            gp.player.worldY = 10 * gp.tileSize;
                            direction = "up";
                        }

                        if (i == 6) {
                            SuperObject obj = gp.obj[gp.currentMap][i];
                            if (sortedPuzzles == 3) {
                                gp.switchMap(5);
                                gp.player.worldX = 7.5 * gp.tileSize;
                                gp.player.worldY = 13 * gp.tileSize;
                                direction = "up";
                            } else {
                                if (!isDialogueVisible && obj != null) {
                                    currentDialogueObject = obj;
                                    obj.currentDialogue = "Locked. Complete the quests \nto know what's behind this door.";
                                    isDialogueVisible = true;
                                }
                            }
                        }
                        break;

                    case 1: // From npcroom to hallway
                        if (i == 0) {
                            gp.switchMap(0);
                            gp.player.worldX = 58.5 * gp.tileSize;
                            gp.player.worldY = 43 * gp.tileSize;
                            direction = "up";
                        }
                        break;

                    case 2: // From library back to hallway
                        if (i == 0) {
                            gp.switchMap(0);
                            gp.player.worldX = 45.5 * gp.tileSize;
                            gp.player.worldY = 22 * gp.tileSize;
                            direction = "up";
                        }
                        break;

                    case 3: // From portrait hall back to hallway
                        if (i == 0) {
                            gp.switchMap(0);
                            gp.player.worldX = 26.5 * gp.tileSize;
                            gp.player.worldY = 14 * gp.tileSize;
                            direction = "up";
                        }
                        break;

                    case 4: // From kitchen to hallway
                        if (i == 0) {
                            gp.switchMap(0);
                            gp.player.worldX = 10.5 * gp.tileSize;
                            gp.player.worldY = 23 * gp.tileSize;
                            direction = "up";
                        }
                        break;
                }
            }
            if ("ShadowDoor".equals(objectName)) {
                switch (gp.currentMap) {
                    case 0:
                        if (i == 1) { // from hallway to npc
                            gp.switchMap(1);
                            gp.player.worldX = 8.5 * gp.tileSize;
                            gp.player.worldY = 6 * gp.tileSize;
                            direction = "down";
                        }
                        if (i == 2) { // from hallway to library
                            gp.switchMap(2);
                            gp.player.worldX = 8.5 * gp.tileSize;
                            gp.player.worldY = 4 * gp.tileSize;
                            direction = "down";
                        }

                        if (i == 3) { // from hallway to portrait room
                            gp.switchMap(3);
                            gp.player.worldX = 17.5 * gp.tileSize;
                            gp.player.worldY = 6 * gp.tileSize;
                            direction = "down";
                        }

                        if (i == 4) { // from hallway to kitchen
                            gp.switchMap(4);
                            gp.player.worldX = 15.5 * gp.tileSize;
                            gp.player.worldY = 8 * gp.tileSize;
                            direction = "down";
                        }

                        if (i == 5) {
                            SuperObject obj = gp.obj[gp.currentMap][i];
                            if (!isDialogueVisible && obj != null) {
                                currentDialogueObject = obj;
                                obj.currentDialogue = "You can't go out.";
                                isDialogueVisible = true;
                            }
                        }
                        break;

                    case 2:
                        if (i == 1) { // from library to hallway
                            gp.switchMap(0);
                            gp.player.worldX = 47.5 * gp.tileSize;
                            gp.player.worldY = 37 * gp.tileSize;
                            direction = "down";
                        }
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

        if (isDialogueVisible && currentDialogueObject != null) {
            currentDialogueObject.showDialogueScreen(gp, g2);
        }
    }
}

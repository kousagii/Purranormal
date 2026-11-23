package main;

import entity.Entity;
import java.awt.Rectangle;
import object.SuperObject;

public class CollisionChecker {
    private final GamePanel gp;

    public CollisionChecker(GamePanel gp) { this.gp = gp; }

    private Rectangle worldArea(Entity entity, int distance) {
        Rectangle area = new Rectangle(entity.solidArea);
        area.translate((int) entity.worldX, (int) entity.worldY);
        switch (entity.direction) {
            case "up": area.y -= distance; break;
            case "down": area.y += distance; break;
            case "left": area.x -= distance; break;
            case "right": area.x += distance; break;
            default: break;
        }
        return area;
    }

    public void checkTile(Entity entity) {
        Rectangle area = worldArea(entity, entity.speed);
        int width = gp.maxWorldCol[gp.currentMap] * gp.tileSize;
        int height = gp.maxWorldRow[gp.currentMap] * gp.tileSize;
        if (area.x < 0 || area.y < 0 || area.x + area.width > width || area.y + area.height > height) {
            entity.collisionOn = true;
            return;
        }
        int left = area.x / gp.tileSize;
        int right = (area.x + area.width - 1) / gp.tileSize;
        int top = area.y / gp.tileSize;
        int bottom = (area.y + area.height - 1) / gp.tileSize;
        for (int col = left; col <= right; col++) {
            for (int row = top; row <= bottom; row++) {
                int number = gp.tileM.mapTileNum[gp.currentMap][col][row];
                if (number < 0 || number >= gp.tileM.tile.length || gp.tileM.tile[number] == null
                        || gp.tileM.tile[number].collision) entity.collisionOn = true;
            }
        }
    }

    public int checkObject(Entity entity, boolean player) {
        Rectangle area = worldArea(entity, entity.speed);
        int index = 999;
        SuperObject[] objects = gp.obj[gp.currentMap];
        for (int i = 0; i < objects.length; i++) {
            SuperObject object = objects[i];
            if (object == null) continue;
            Rectangle target = new Rectangle(object.solidArea);
            target.translate(object.worldX, object.worldY);
            if (area.intersects(target)) {
                if (object.collision) entity.collisionOn = true;
                if (player) index = i;
            }
        }
        return index;
    }

    /** The prompt and Space action deliberately use exactly the same target. */
    public int checkInteractionObject(Entity entity) {
        Rectangle area = worldArea(entity, gp.tileSize);
        SuperObject[] objects = gp.obj[gp.currentMap];
        for (int i = 0; i < objects.length; i++) {
            SuperObject object = objects[i];
            if (object == null || !object.interactable) continue;
            Rectangle target = new Rectangle(object.solidArea);
            target.translate(object.worldX, object.worldY);
            if (area.intersects(target)) return i;
        }
        return 999;
    }
}


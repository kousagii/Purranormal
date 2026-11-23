package tile;

import java.awt.Graphics2D;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    private final GamePanel gp;
    public Tile[] tile;
    public final int[][][] mapTileNum;
    private final Map<Integer, Tile[]> tilesByMap = new HashMap<>();

    public TileManager(GamePanel gp) {
        this.gp = gp;
        mapTileNum = new int[gp.maxMap][][];
        String[] folders = {"hallwaytiles", "npcroomtiles", "librarytiles",
            "portraithalltiles", "kitchentiles", "lockedroomtiles"};
        String[] maps = {"hallway.png.txt", "npcroom.png.txt", "library.png.txt",
            "portrait_hall.png.txt", "kitchen.png.txt", "whiteTile.txt"};
        for (int i = 0; i < folders.length; i++) loadNewMap(folders[i], "/maps/" + maps[i], i);
    }

    public void loadNewMap(String folder, String path, int map) {
        int[][] dimensions = {{64, 50}, {19, 17}, {18, 12}, {31, 15}, {29, 14}, {15, 15}};
        if (map < 0 || map >= dimensions.length) throw new IllegalArgumentException("Unknown map: " + map);
        gp.maxWorldCol[map] = dimensions[map][0];
        gp.maxWorldRow[map] = dimensions[map][1];
        tile = tilesByMap.computeIfAbsent(map, ignored -> loadTiles(folder));
        if (mapTileNum[map] == null) loadMap(path, map);
    }

    private Tile[] loadTiles(String folder) {
        Tile[] result = new Tile[150];
        String prefix = folder.replace("tiles", "");
        Set<Integer> walkable = getNonCollisionTiles(folder);
        for (int i = 0; i < result.length; i++) {
            String path = String.format("/%s/%s_%03d.png", folder, prefix, i);
            try (InputStream stream = getClass().getResourceAsStream(path)) {
                if (stream == null) break;
                Tile value = new Tile();
                value.image = ImageIO.read(stream);
                if (value.image == null) throw new IOException("Invalid tile image: " + path);
                value.collision = !walkable.contains(i);
                result[i] = value;
            } catch (IOException e) {
                throw new IllegalStateException("Cannot load tiles: " + path, e);
            }
        }
        return result;
    }

    public Set<Integer> getNonCollisionTiles(String folder) {
        return switch (folder) {
            case "hallwaytiles" -> Set.of(33, 34, 56, 57, 58, 59, 60, 61, 62, 66, 67,
                    68, 69, 74, 75, 76, 77, 81, 82, 84, 85, 88, 108, 111, 123,
                    124, 125, 126, 127, 128, 129, 130, 131, 132, 133);
            case "npcroomtiles" -> Set.of(26, 27, 28, 29, 30, 38, 39, 40, 44, 47, 50, 55, 56);
            case "librarytiles" -> Set.of(26, 27, 28, 29, 30, 47, 53, 32, 23, 24, 61, 62);
            case "portraithalltiles" -> Set.of(67, 68, 69, 70, 77, 78, 79, 80, 84, 85, 86, 89, 90, 91, 113, 87, 88, 54, 55);
            case "kitchentiles" -> Set.of(47, 48, 61, 62, 65, 70, 71);
            case "lockedroomtiles" -> Set.of(0);
            default -> Set.of();
        };
    }

    private void loadMap(String path, int map) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) throw new IOException("Missing map: " + path);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                int[][] grid = new int[gp.maxWorldCol[map]][gp.maxWorldRow[map]];
                for (int row = 0; row < gp.maxWorldRow[map]; row++) {
                    String line = reader.readLine();
                    if (line == null) throw new IOException("Missing row " + row);
                    String[] values = line.split(",");
                    if (values.length != gp.maxWorldCol[map]) throw new IOException("Wrong column count on row " + row);
                    for (int col = 0; col < values.length; col++) {
                        int value = Integer.parseInt(values[col].trim());
                        if (value < 0 || value >= tile.length || tile[value] == null) {
                            throw new IOException("Unknown tile " + value + " on row " + row);
                        }
                        grid[col][row] = value;
                    }
                }
                mapTileNum[map] = grid;
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Cannot load map " + path + ": " + e.getMessage(), e);
        }
    }

    public void draw(Graphics2D g) {
        for (int row = 0; row < gp.maxWorldRow[gp.currentMap]; row++) {
            for (int col = 0; col < gp.maxWorldCol[gp.currentMap]; col++) {
                int x = col * gp.tileSize - (int) gp.player.worldX + gp.player.screenX;
                int y = row * gp.tileSize - (int) gp.player.worldY + gp.player.screenY;
                if (x + gp.tileSize > 0 && x < gp.screenWidth && y + gp.tileSize > 0 && y < gp.screenHeight) {
                    g.drawImage(tile[mapTileNum[gp.currentMap][col][row]].image,
                            x, y, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}


package main;

import data.DataStorage;
import data.SaveGameService;
import entity.Player;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import object.SuperObject;
import sound.SoundPlayer;
import sound.SoundEffects;
import tile.TileManager;

/** World/session coordinator. Input, simulation and painting share Swing's EDT. */
public class GamePanel extends JPanel {
    public final int tileSize = 48;
    public final int maxScreenCol = 18;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;
    public final int maxMap = 10;
    public final int[] maxWorldCol = new int[maxMap];
    public final int[] maxWorldRow = new int[maxMap];
    public int currentMap;
    public final SuperObject[][] obj = new SuperObject[maxMap][50];
    public final TileManager tileM = new TileManager(this);
    public final KeyHandler keyH = new KeyHandler();
    public final MouseHandler mouseHandler = new MouseHandler(this);
    public final CollisionChecker cChecker = new CollisionChecker(this);
    public final AssetSetter aSetter = new AssetSetter(this);
    public final Player player = new Player(this, keyH);
    public final TutorialDialogue tutorialDialogue = new TutorialDialogue(this);

    public boolean paused;
    public boolean showQuestDialog;
    public boolean showGameSavedDialog;
    public final Rectangle questButton = new Rectangle(20, 20, 140, 44);
    public final Rectangle continueButton = new Rectangle();
    public final Rectangle saveButton = new Rectangle();
    public final Rectangle backToTitleButton = new Rectangle();
    public final Rectangle backButton = new Rectangle();
    public final Rectangle gameSavedBackButton = new Rectangle();
    public Rectangle hoveredPauseButton;
    public Rectangle hoveredQuestButton;

    private static final String[] TILE_FOLDERS = {
        "hallwaytiles", "npcroomtiles", "librarytiles",
        "portraithalltiles", "kitchentiles", "lockedroomtiles"
    };
    private static final String[] MAP_PATHS = {
        "/maps/hallway.png.txt", "/maps/npcroom.png.txt", "/maps/library.png.txt",
        "/maps/portrait_hall.png.txt", "/maps/kitchen.png.txt", "/maps/whiteTile.txt"
    };
    private final Runnable backToTitleCallback;
    private final Runnable endingCallback;
    private final Timer gameLoop = new Timer(1000 / 60, e -> { update(); repaint(); });
    private final SoundPlayer bgm;
    private final SoundEffects sfx = new SoundEffects();
    private final BufferedImage photoIcon = UiStyle.image("/objects/Photo.png");
    private final SaveGameService saves = new SaveGameService(Path.of("savegame.dat"));
    private String saveMessage = "Game Saved";
    private boolean ending;

    public GamePanel(Runnable backToTitleCallback) { this(backToTitleCallback, () -> { }); }

    public GamePanel(Runnable backToTitleCallback, Runnable endingCallback) {
        this.backToTitleCallback = backToTitleCallback;
        this.endingCallback = endingCallback;
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        addKeyListener(keyH);
        addMouseMotionListener(mouseHandler);
        addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                boolean wasPaused = paused;
                keyH.resetKeys();
                keyH.pausePressed = wasPaused;
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { handleClick(e); }
            @Override public void mouseExited(MouseEvent e) {
                hoveredPauseButton = null;
                hoveredQuestButton = null;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                hoveredQuestButton = questButton.contains(p) ? questButton : null;
                hoveredPauseButton = continueButton.contains(p) ? continueButton
                        : saveButton.contains(p) ? saveButton
                        : backToTitleButton.contains(p) ? backToTitleButton : null;
                repaint();
            }
        });
        setupGame();
        bgm = new SoundPlayer("/sound/first_light_particles.wav");
        bgm.setVolume(0.45f);
        bgm.playLoop();
    }

    private boolean hit(Rectangle bounds, Point point) {
        if (!bounds.contains(point)) return false;
        playSound(SoundEffects.Cue.CLICK);
        return true;
    }

    private void handleClick(MouseEvent e) {
        Point p = e.getPoint();
        if (showGameSavedDialog) {
            if (hit(gameSavedBackButton, p)) showGameSavedDialog = false;
        } else if (showQuestDialog) {
            if (hit(backButton, p)) showQuestDialog = false;
        } else if (paused) {
            if (hit(continueButton, p)) {
                paused = false;
                keyH.resetKeys();
            } else if (hit(saveButton, p)) {
                saveGame();
                showGameSavedDialog = true;
            } else if (hit(backToTitleButton, p)) {
                stopGameLoop();
                keyH.resetKeys();
                backToTitleCallback.run();
            }
        } else if (tutorialDialogue.isActive()) {
            if (hit(tutorialDialogue.getButtonBounds(), p)) tutorialDialogue.nextLine();
        } else if (player.isDialogueVisible) {
            mouseHandler.mousePressed(e);
        } else if (!player.isPuzzleGameActive && hit(questButton, p)) {
            showQuestDialog = true;
            keyH.resetKeys();
        }
        requestFocusInWindow();
        repaint();
    }

    /** Reset the entire in-memory world; never remove a user's save file. */
    public void setupGame() {
        stopGameLoop();
        resetTransientState();
        rebuildObjects();
        switchMap(0);
        player.setDefaultValues();
        player.hasPhoto = 0;
        player.placedPhotos = 0;
        player.sortedPuzzles = 0;
        player.isPortraitPuzzleSolved = false;
        player.isLibraryPuzzleSolved1 = false;
        player.isLibraryPuzzleSolved2 = false;
        tutorialDialogue.start();
    }

    private void rebuildObjects() {
        for (SuperObject[] objects : obj) Arrays.fill(objects, null);
        aSetter.setObject();
    }

    private void resetTransientState() {
        paused = false;
        ending = false;
        showQuestDialog = false;
        showGameSavedDialog = false;
        hoveredPauseButton = null;
        hoveredQuestButton = null;
        keyH.resetKeys();
        player.isDialogueVisible = false;
        player.currentDialogueObject = null;
        player.isPuzzleGameActive = false;
        player.collisionOn = false;
        player.spriteCounter = 0;
        player.spriteNum = 1;
    }

    public void startGameLoop() { if (!ending) gameLoop.start(); }
    public void stopGameLoop() { gameLoop.stop(); }
    public boolean isGameLoopRunning() { return gameLoop.isRunning(); }

    public void update() {
        paused = keyH.pausePressed;
        if (ending || paused || showQuestDialog || showGameSavedDialog || tutorialDialogue.isActive()) return;
        for (SuperObject object : obj[currentMap]) if (object != null) object.update();
        player.update();
    }

    public void completeGame() {
        if (ending) return;
        ending = true;
        playSound(SoundEffects.Cue.ENDING);
        stopGameLoop();
        keyH.resetKeys();
        endingCallback.run();
    }

    public void switchMap(int mapIndex) {
        if (mapIndex < 0 || mapIndex >= TILE_FOLDERS.length) {
            throw new IllegalArgumentException("Invalid map: " + mapIndex);
        }
        if (isGameLoopRunning() && currentMap != mapIndex) playSound(SoundEffects.Cue.DOOR);
        tileM.loadNewMap(TILE_FOLDERS[mapIndex], MAP_PATHS[mapIndex], mapIndex);
        currentMap = mapIndex;
    }

    public boolean isInteractionPromptVisible() {
        return !ending && !paused && !showQuestDialog && !showGameSavedDialog
                && !tutorialDialogue.isActive() && !player.isDialogueVisible
                && !player.isPuzzleGameActive && cChecker.checkInteractionObject(player) != 999;
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        tileM.draw(g);
        for (SuperObject object : obj[currentMap]) if (object != null) object.draw(g, this);
        player.draw(g);
        if (!player.isPuzzleGameActive) drawHud(g);
        if (isInteractionPromptVisible()) {
            g.setFont(UiStyle.font(28));
            String label = "Space: interact";
            int width = g.getFontMetrics().stringWidth(label) + 28;
            Rectangle bounds = new Rectangle(player.screenX + tileSize / 2 - width / 2,
                    player.screenY - 48, width, 36);
            UiStyle.panel(g, bounds);
            g.setColor(UiStyle.ACCENT);
            UiStyle.centered(g, label, bounds.x + width / 2, bounds.y + 25);
        }
        if (tutorialDialogue.isActive()) tutorialDialogue.draw(g);
        if (paused) drawPause(g);
        if (showQuestDialog) drawQuestDialog(g);
        if (showGameSavedDialog) drawSaveDialog(g);
        g.dispose();
    }

    private void drawHud(Graphics2D g) {
        if (!paused && !tutorialDialogue.isActive() && !player.isDialogueVisible) {
            g.setFont(UiStyle.font(34));
            UiStyle.button(g, questButton, "Quests", hoveredQuestButton == questButton);
        }
        Rectangle inventory = new Rectangle(screenWidth - 130, 20, 110, 56);
        UiStyle.panel(g, inventory);
        g.drawImage(photoIcon, inventory.x + 8, inventory.y + 8, 40, 40, null);
        g.setFont(UiStyle.font(32));
        g.setColor(UiStyle.TEXT);
        g.drawString("x" + player.hasPhoto, inventory.x + 59, inventory.y + 36);
    }

    private void shade(Graphics2D g) {
        g.setColor(new Color(8, 5, 14, 170));
        g.fillRect(0, 0, screenWidth, screenHeight);
    }

    private void drawPause(Graphics2D g) {
        shade(g);
        Rectangle panel = new Rectangle((screenWidth - 400) / 2, 106, 400, 364);
        UiStyle.panel(g, panel);
        g.setColor(UiStyle.ACCENT);
        g.setFont(UiStyle.font(54));
        UiStyle.centered(g, "Paused", screenWidth / 2, panel.y + 62);
        Rectangle[] buttons = { continueButton, saveButton, backToTitleButton };
        String[] labels = { "Continue", "Save", "Back to Title" };
        g.setFont(UiStyle.font(38));
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBounds(panel.x + 50, panel.y + 100 + i * 72, 300, 52);
            UiStyle.button(g, buttons[i], labels[i], hoveredPauseButton == buttons[i]);
        }
    }

    public void drawQuestDialog(Graphics2D g) {
        shade(g);
        Rectangle panel = new Rectangle((screenWidth - 660) / 2, 130, 660, 316);
        UiStyle.panel(g, panel);
        g.setColor(UiStyle.ACCENT);
        g.setFont(UiStyle.font(46));
        UiStyle.centered(g, "Quests", screenWidth / 2, panel.y + 54);
        String[] labels = { "Portrait Puzzle", "Library Left Shelf Puzzle", "Library Right Shelf Puzzle" };
        boolean[] done = { player.isPortraitPuzzleSolved, player.isLibraryPuzzleSolved1, player.isLibraryPuzzleSolved2 };
        g.setFont(UiStyle.font(28));
        for (int i = 0; i < labels.length; i++) {
            int y = panel.y + 108 + i * 48;
            g.setColor(UiStyle.TEXT);
            g.drawString(labels[i], panel.x + 28, y);
            String status = done[i] ? "Completed" : "Not completed";
            g.setColor(done[i] ? UiStyle.ACCENT : UiStyle.MUTED);
            g.drawString(status, panel.x + panel.width - 28 - g.getFontMetrics().stringWidth(status), y);
        }
        backButton.setBounds(screenWidth / 2 - 70, panel.y + 248, 140, 44);
        UiStyle.button(g, backButton, "Back", backButton.contains(mouseHandler.mouseX, mouseHandler.mouseY));
    }

    private void drawSaveDialog(Graphics2D g) {
        shade(g);
        Rectangle panel = new Rectangle((screenWidth - 520) / 2, 186, 520, 204);
        UiStyle.panel(g, panel);
        g.setColor(UiStyle.TEXT);
        g.setFont(UiStyle.font(32));
        UiStyle.wrapped(g, saveMessage, panel.x + 28, panel.y + 48, panel.width - 56);
        gameSavedBackButton.setBounds(screenWidth / 2 - 70, panel.y + 138, 140, 44);
        UiStyle.button(g, gameSavedBackButton, "Back",
                gameSavedBackButton.contains(mouseHandler.mouseX, mouseHandler.mouseY));
    }

    public DataStorage captureGame() {
        DataStorage data = new DataStorage();
        data.worldX = (int) player.worldX;
        data.worldY = (int) player.worldY;
        data.hasPhoto = player.hasPhoto;
        data.placedPhotos = player.placedPhotos;
        data.sortedPuzzles = player.sortedPuzzles;
        data.isPortraitPuzzleSolved = player.isPortraitPuzzleSolved;
        data.isLibraryPuzzleSolved1 = player.isLibraryPuzzleSolved1;
        data.isLibraryPuzzleSolved2 = player.isLibraryPuzzleSolved2;
        data.currentMap = currentMap;
        for (int map = 0; map < obj.length; map++) {
            data.removedObjects[map] = new ArrayList<>();
            for (int i = 0; i < obj[map].length; i++) {
                if (obj[map][i] == null) data.removedObjects[map].add(i);
                if (obj[map][i] instanceof object.OBJ_PlacedPhoto photo) {
                    data.placedPhotoList.add(new DataStorage.PlacedPhotoData(map, i,
                            photo.worldX, photo.worldY, photo.index));
                }
            }
        }
        return data;
    }

    public boolean saveGame() {
        try {
            saves.save(captureGame());
            saveMessage = "Game Saved";
            return true;
        } catch (IOException e) {
            saveMessage = "Could not save. Check that the game folder is writable.";
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }

    public void loadGame(DataStorage data) {
        validateSave(data);
        stopGameLoop();
        resetTransientState();
        rebuildObjects();
        switchMap(data.currentMap);
        player.setDefaultValues();
        player.worldX = data.worldX;
        player.worldY = data.worldY;
        player.hasPhoto = data.hasPhoto;
        player.placedPhotos = data.placedPhotos;
        player.sortedPuzzles = data.sortedPuzzles;
        player.isPortraitPuzzleSolved = data.isPortraitPuzzleSolved;
        player.isLibraryPuzzleSolved1 = data.isLibraryPuzzleSolved1;
        player.isLibraryPuzzleSolved2 = data.isLibraryPuzzleSolved2;
        for (int map = 0; map < data.removedObjects.length; map++) {
            if (data.removedObjects[map] != null) {
                for (int index : data.removedObjects[map]) obj[map][index] = null;
            }
        }
        for (DataStorage.PlacedPhotoData photo : data.placedPhotoList) {
            aSetter.placePlacedPhoto(photo.objIndex, photo.map, photo.worldX, photo.worldY, photo.imageIndex);
        }
        tutorialDialogue.finish();
    }

    private void validateSave(DataStorage data) {
        if (data == null || data.currentMap < 0 || data.currentMap >= TILE_FOLDERS.length
                || data.hasPhoto < 0 || data.hasPhoto > 6 || data.placedPhotos < 0 || data.placedPhotos > 6
                || data.sortedPuzzles < 0 || data.sortedPuzzles > 3 || data.removedObjects == null
                || data.removedObjects.length > maxMap || data.placedPhotoList == null) {
            throw new IllegalArgumentException("The saved game contains invalid progress.");
        }
        int width = maxWorldCol[data.currentMap] * tileSize;
        int height = maxWorldRow[data.currentMap] * tileSize;
        if (data.worldX < -player.solidArea.x || data.worldY < -player.solidArea.y
                || data.worldX + player.solidArea.x + player.solidArea.width > width
                || data.worldY + player.solidArea.y + player.solidArea.height > height) {
            throw new IllegalArgumentException("The saved position is outside the house.");
        }
        for (var removed : data.removedObjects) {
            if (removed != null) for (Integer index : removed) {
                if (index == null || index < 0 || index >= 50) throw new IllegalArgumentException("Invalid saved object.");
            }
        }
        for (DataStorage.PlacedPhotoData photo : data.placedPhotoList) {
            if (photo == null || photo.map < 0 || photo.map >= TILE_FOLDERS.length
                    || photo.objIndex < 0 || photo.objIndex >= 50 || photo.imageIndex < 1 || photo.imageIndex > 6
                    || photo.worldX < 0 || photo.worldY < 0
                    || photo.worldX >= maxWorldCol[photo.map] * tileSize
                    || photo.worldY >= maxWorldRow[photo.map] * tileSize) {
                throw new IllegalArgumentException("Invalid saved portrait.");
            }
        }
    }

    public void setVolume(float volume) { bgm.setVolume(volume); }
    public void playSound(SoundEffects.Cue cue) { sfx.play(cue); }
    public void close() { stopGameLoop(); bgm.close(); sfx.close(); }
}


package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    // Preserve compatibility with saves written before explicit versioning.
    private static final long serialVersionUID = -7504396210604482240L;
    public int worldX, worldY;
    public int hasPhoto;
    public int placedPhotos;
    public int sortedPuzzles;
    public boolean isPortraitPuzzleSolved;
    public boolean isLibraryPuzzleSolved1;
    public boolean isLibraryPuzzleSolved2;
    
    public int currentMap; 
    
    @SuppressWarnings("unchecked")
    public ArrayList<Integer>[] removedObjects = (ArrayList<Integer>[]) new ArrayList<?>[10];
    public static class PlacedPhotoData implements Serializable {
        private static final long serialVersionUID = -7332467970766656503L;
        public int map, objIndex, worldX, worldY, imageIndex;

        public PlacedPhotoData(int map, int objIndex, int worldX, int worldY, int imageIndex) {
            this.map = map;
            this.objIndex = objIndex;
            this.worldX = worldX;
            this.worldY = worldY;
            this.imageIndex = imageIndex;
        }
    }
    public ArrayList<PlacedPhotoData> placedPhotoList = new ArrayList<>();
}


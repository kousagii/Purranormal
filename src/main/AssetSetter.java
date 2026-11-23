package main;

import object.OBJ_Armor;
import object.OBJ_Cabinet;
import object.OBJ_Chair;
import object.OBJ_Clock;
import object.OBJ_DogBowl;
import object.OBJ_DogHouse;
import object.OBJ_Door;
import object.OBJ_GhostDog;
import object.OBJ_LibraryGame1;
import object.OBJ_LibraryGame2;
import object.OBJ_LongTable;
import object.OBJ_Owner;
import object.OBJ_Photo;
import object.OBJ_PortraitGame;
import object.OBJ_ShadowDoor;
import object.OBJ_SideSofa;
import object.OBJ_Sofa;
import object.OBJ_TVBack;
import object.OBJ_TallLamp;
import object.OBJ_Telephone;
import object.OBJ_ThinLamp;
import object.OBJ_Vase;

public class AssetSetter {
    GamePanel gp;
    
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject() {
        //hallway
        gp.obj[0][0] = new OBJ_Door(48*2, 48*3);
        gp.obj[0][0].worldX =  47 * gp.tileSize;
        gp.obj[0][0].worldY = 34 * gp.tileSize;
        gp.obj[0][0].collision = true;
        
        gp.obj[0][1] = new OBJ_ShadowDoor(48*2, 48);
        gp.obj[0][1].worldX =  58 * gp.tileSize;
        gp.obj[0][1].worldY = 44 * gp.tileSize;
        gp.obj[0][1].collision = true;
        
        gp.obj[0][2] = new OBJ_ShadowDoor(48*2, 48);
        gp.obj[0][2].worldX =  45 * gp.tileSize;
        gp.obj[0][2].worldY = 23 * gp.tileSize;
        gp.obj[0][2].collision = true;
        
        gp.obj[0][3] = new OBJ_ShadowDoor(48*2, 48);
        gp.obj[0][3].worldX =  26 * gp.tileSize;
        gp.obj[0][3].worldY = 15 * gp.tileSize;
        gp.obj[0][3].collision = true;
        
        gp.obj[0][4] = new OBJ_ShadowDoor(48*2, 48);
        gp.obj[0][4].worldX =  10 * gp.tileSize;
        gp.obj[0][4].worldY = 24 * gp.tileSize;
        gp.obj[0][4].collision = true;

        gp.obj[0][5] = new OBJ_ShadowDoor(48*2, 48);
        gp.obj[0][5].worldX = 32 * gp.tileSize;
        gp.obj[0][5].worldY = 49 * gp.tileSize;
        gp.obj[0][5].collision = true;
        
        gp.obj[0][6] = new OBJ_Door(48*2, 48*3);
        gp.obj[0][6].worldX =  59 * gp.tileSize;
        gp.obj[0][6].worldY = 20 * gp.tileSize;
        gp.obj[0][6].collision = true;
        
        gp.obj[0][7] = new OBJ_Photo(48, 48);
        gp.obj[0][7].worldX = 60 * gp.tileSize;
        gp.obj[0][7].worldY = 23 * gp.tileSize;
        gp.obj[0][7].collision = true;
        
        gp.obj[0][8] = new OBJ_Photo(48, 48);
        gp.obj[0][8].worldX = 47 * gp.tileSize;
        gp.obj[0][8].worldY = 8 * gp.tileSize;
        gp.obj[0][8].collision = true;
        
        gp.obj[0][9] = new OBJ_Photo(48, 48);
        gp.obj[0][9].worldX = 1 * gp.tileSize;
        gp.obj[0][9].worldY = 7 * gp.tileSize;
        gp.obj[0][9].collision = true;
        
        gp.obj[0][10] = new OBJ_Photo(48, 48);
        gp.obj[0][10].worldX = 34 * gp.tileSize;
        gp.obj[0][10].worldY = 47 * gp.tileSize;
        gp.obj[0][10].collision = true;               
      
        gp.obj[0][11] = new OBJ_Clock(48, 48*3);
        gp.obj[0][11].worldX = 32 * gp.tileSize;
        gp.obj[0][11].worldY = 34 * gp.tileSize;
        gp.obj[0][11].collision = true;
        
        gp.obj[0][12] = new OBJ_Chair(48, 48*2);
        gp.obj[0][12].worldX = 38 * gp.tileSize;
        gp.obj[0][12].worldY = 36 * gp.tileSize;
        gp.obj[0][12].collision = true;
        
        gp.obj[0][13] = new OBJ_Sofa(48*4, 48*2);
        gp.obj[0][13].worldX = 39 * gp.tileSize;
        gp.obj[0][13].worldY = 36 * gp.tileSize;
        gp.obj[0][13].collision = true;
        
        gp.obj[0][14] = new OBJ_TallLamp(48, 48*3);
        gp.obj[0][14].worldX = 43 * gp.tileSize;
        gp.obj[0][14].worldY = 35 * gp.tileSize;
        gp.obj[0][14].collision = true;
        
        gp.obj[0][15] = new OBJ_TVBack(48*2, 48*2);
        gp.obj[0][15].worldX = 40 * gp.tileSize;
        gp.obj[0][15].worldY = 40 * gp.tileSize;
        gp.obj[0][15].collision = true;
        
        gp.obj[0][16] = new OBJ_Telephone(48, 48);
        gp.obj[0][16].worldX = 55 * gp.tileSize;
        gp.obj[0][16].worldY = 35 * gp.tileSize;
        gp.obj[0][16].collision = true;
        
        gp.obj[0][17] = new OBJ_LongTable(48, 48*5);
        gp.obj[0][17].worldX = 62 * gp.tileSize;
        gp.obj[0][17].worldY = 39 * gp.tileSize;
        gp.obj[0][17].collision = true;
       
        gp.obj[0][18] = new OBJ_Vase(48, 48);
        gp.obj[0][18].worldX = 62 * gp.tileSize;
        gp.obj[0][18].worldY = 24 * gp.tileSize;
        gp.obj[0][18].collision = true;
        
        gp.obj[0][19] = new OBJ_Vase(48, 48);
        gp.obj[0][19].worldX = 57 * gp.tileSize;
        gp.obj[0][19].worldY = 24 * gp.tileSize;
        gp.obj[0][19].collision = true;
        
        gp.obj[0][20] = new OBJ_Telephone(48, 48);
        gp.obj[0][20].worldX = 3 * gp.tileSize;
        gp.obj[0][20].worldY = 4 * gp.tileSize;
        gp.obj[0][20].collision = true;
        
        gp.obj[0][21] = new OBJ_Sofa(48*3, 48*2);
        gp.obj[0][21].worldX = 6 * gp.tileSize;
        gp.obj[0][21].worldY = 4 * gp.tileSize;
        gp.obj[0][21].collision = true;
        
        gp.obj[0][22] = new OBJ_SideSofa(48*2, 48*4);
        gp.obj[0][22].worldX = 1 * gp.tileSize;
        gp.obj[0][22].worldY = 8 * gp.tileSize;
        gp.obj[0][22].collision = true;
        
        gp.obj[0][23] = new OBJ_Vase(48, 48*2);
        gp.obj[0][23].worldX = 30 * gp.tileSize;
        gp.obj[0][23].worldY = 37 * gp.tileSize;
        gp.obj[0][23].collision = true;
        
        gp.obj[0][24] = new OBJ_Cabinet(48*3, 48*3);
        gp.obj[0][24].worldX = 9 * gp.tileSize;
        gp.obj[0][24].worldY = 3 * gp.tileSize;
        gp.obj[0][24].collision = true;
        
        //npc room
        gp.obj[1][0] = new OBJ_Door(48*2, 48*3);
        gp.obj[1][0].worldX =  8 * gp.tileSize;
        gp.obj[1][0].worldY = 3 * gp.tileSize;
        gp.obj[1][0].collision = true;
        
        gp.obj[1][1] = new OBJ_GhostDog(48, 48);
        gp.obj[1][1].worldX =  8 * gp.tileSize;
        gp.obj[1][1].worldY = 11 * gp.tileSize;
        gp.obj[1][1].collision = true;
        
        gp.obj[1][2] = new OBJ_Photo(48, 48);
        gp.obj[1][2].worldX = 15 * gp.tileSize;
        gp.obj[1][2].worldY = 8 * gp.tileSize;
        gp.obj[1][2].collision = true;
        
        gp.obj[1][3] = new OBJ_DogHouse(48*3, 48*2);
        gp.obj[1][3].worldX = 9 * gp.tileSize;
        gp.obj[1][3].worldY = 10* gp.tileSize;
        gp.obj[1][3].collision = true;
        
        gp.obj[1][4] = new OBJ_DogBowl(48, 48);
        gp.obj[1][4].worldX = 12 * gp.tileSize;
        gp.obj[1][4].worldY = 11 * gp.tileSize;
        gp.obj[1][4].collision = true;
        
        
        
    //library
        gp.obj[2][0] = new OBJ_Door(48*2, 48*3);
        gp.obj[2][0].worldX =  8 * gp.tileSize;
        gp.obj[2][0].worldY = 1 * gp.tileSize;
        gp.obj[2][0].collision = true;
        
        gp.obj[2][1] = new OBJ_ShadowDoor(48*2, 48);
        gp.obj[2][1].worldX = 8  * gp.tileSize;
        gp.obj[2][1].worldY =  11 * gp.tileSize;
        gp.obj[2][1].collision = true;
                     
        gp.obj[2][2] = new OBJ_LibraryGame1(48*5, 48);
        gp.obj[2][2].worldX =  1 * gp.tileSize;
        gp.obj[2][2].worldY =  3 * gp.tileSize;
        gp.obj[2][2].collision = true;
        
        gp.obj[2][3] = new OBJ_LibraryGame2(48*5, 48);
        gp.obj[2][3].worldX = 12 * gp.tileSize;
        gp.obj[2][3].worldY =  3 * gp.tileSize;
        gp.obj[2][3].collision = true;
        
    //portrait hall
        gp.obj[3][0] = new OBJ_Door(48*2, 48*3);
        gp.obj[3][0].worldX =  17 * gp.tileSize;
        gp.obj[3][0].worldY = 3 * gp.tileSize;
        gp.obj[3][0].collision = true;
        
        gp.obj[3][1] = new OBJ_PortraitGame(48*6, 48); 
        gp.obj[3][1].worldX = 9 * gp.tileSize; 
        gp.obj[3][1].worldY = 5 * gp.tileSize; 
        gp.obj[3][1].collision = true;
        
        gp.obj[3][2] = new OBJ_PortraitGame(48*6, 48); 
        gp.obj[3][2].worldX = 19 * gp.tileSize; 
        gp.obj[3][2].worldY = 5 * gp.tileSize; 
        gp.obj[3][2].collision = true;
        
        gp.obj[3][3] = new OBJ_Armor(48, 48*3); 
        gp.obj[3][3].worldX = 6 * gp.tileSize; 
        gp.obj[3][3].worldY = 6 * gp.tileSize; 
        gp.obj[3][3].collision = true;
        
        gp.obj[3][4] = new OBJ_Vase(48, 48*2);
        gp.obj[3][4].worldX = 6 * gp.tileSize;
        gp.obj[3][4].worldY = 11 * gp.tileSize;
        gp.obj[3][4].collision = true;
        
        gp.obj[3][5] = new OBJ_ThinLamp(48, 48*4);
        gp.obj[3][5].worldX = 7 * gp.tileSize;
        gp.obj[3][5].worldY = 3 * gp.tileSize;
        gp.obj[3][5].collision = true;
        
        gp.obj[3][6] = new OBJ_ThinLamp(48, 48*4);
        gp.obj[3][6].worldX = 29 * gp.tileSize;
        gp.obj[3][6].worldY = 4 * gp.tileSize;
        gp.obj[3][6].collision = true;
        
        gp.obj[3][7] = new OBJ_Armor(48, 48*3); 
        gp.obj[3][7].worldX = 26 * gp.tileSize; 
        gp.obj[3][7].worldY = 4 * gp.tileSize; 
        gp.obj[3][7].collision = true;        
        
        gp.obj[3][8] = new OBJ_Sofa(48*3, 48*2);
        gp.obj[3][8].worldX = 17 * gp.tileSize;
        gp.obj[3][8].worldY = 11 * gp.tileSize;
        gp.obj[3][8].collision = true;
                
        gp.obj[3][9] = new OBJ_Cabinet(48*3, 48*3); 
        gp.obj[3][9].worldX = 1 * gp.tileSize; 
        gp.obj[3][9].worldY = 6 * gp.tileSize; 
        gp.obj[3][9].collision = true;
        
    //kitchen
        gp.obj[4][0] = new OBJ_Door(48*2, 48*3);
        gp.obj[4][0].worldX =  15 * gp.tileSize;
        gp.obj[4][0].worldY = 5 * gp.tileSize;
        gp.obj[4][0].collision = true;
        
        gp.obj[4][1] = new OBJ_Photo(48, 48);
        gp.obj[4][1].worldX = 27 * gp.tileSize;
        gp.obj[4][1].worldY = 8 * gp.tileSize;
        gp.obj[4][1].collision = true;
        
    //locked room 
        gp.obj[5][0] = new OBJ_Owner(48*2, 48*2);
        gp.obj[5][0].worldX =  7 * gp.tileSize;
        gp.obj[5][0].worldY = 7 * gp.tileSize;
        gp.obj[5][0].collision = true;
                
    }
    
    public void placePlacedPhoto(int objIndex, int map, int worldX, int worldY, int imageIndex) {
        gp.obj[map][objIndex] = new object.OBJ_PlacedPhoto(imageIndex, 48, 48*2);
        gp.obj[map][objIndex].worldX = worldX;
        gp.obj[map][objIndex].worldY = worldY;
        gp.obj[map][objIndex].collision = true;
    }

}

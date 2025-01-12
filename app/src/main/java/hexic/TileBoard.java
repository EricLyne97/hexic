package hexic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class TileBoard {
    private static final int NUM_COLS = 10;
    private static final int NUM_ROWS = 8*2 + 1;
                                                    
    private int queuedRotations = 0;                       

    private final double TILE_X_SPACING = 2 + 3.0*Tile.SIDE_LENGTH/2.0;
    private final double TILE_Y_SPACING = 1 + Tile.SIDE_LENGTH*Math.sin(Math.PI/3);

    private static final Color[] tileColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};

    private Tile[][] tiles;
    static Cursor cursor = new Cursor();

    public void InitBoard(){
        Random rand = new Random();

        // Double the rows so that odd rows can be on the "half" step
        this.tiles = new Tile[NUM_COLS][NUM_ROWS];
        int startingIndex = 0;
        for(int i=0; i < tiles.length; i++) {
            // Even rows start on 1, Odd rows start on 0 
            startingIndex = (i+1)%2;
            for(int j=startingIndex; j < tiles[i].length; j+=2) {
                // tiles[i][j] = new Tile(tileColors[1]);
                tiles[i][j] = new Tile(tileColors[rand.nextInt(tileColors.length)]);
            }
        }
    }

    public void clear(){
        this.tiles = new Tile[NUM_COLS][NUM_ROWS];
    }

    public TileBoard(){
        InitBoard();
    }

    public void boardLogic(){

    }

    public void paint(Graphics g){
        for(int i=0; i < tiles.length; i++) {
            for(int j=0; j < tiles[i].length; j++) {
                if(tiles[i][j]!= null){
                    tiles[i][j].paintTile(g, (int) TILE_X_SPACING*i, (int) TILE_Y_SPACING*j);
                }
            }
        }
    }

    public void describe(){
        for(int i=0; i < tiles.length; i++) {
            for(int j=0; j < tiles[i].length; j+=1) {
                if(tiles[i][j]!= null){
                    System.out.printf("%10s",tiles[i][j].color);
                }
            }
            System.out.println();
        }
        System.out.println("test2");
    }


    public void deleteTiles(boolean[][] selection){
        for(int i=0; i < selection.length; i++) {
            for(int j=0; j < selection[i].length; j+=1) {
                if(selection[i][j]){
                    tiles[i][j] = null;
                }
            }
        }
    }

    public boolean[][] scanForClusters(){
        boolean[][] result = new boolean[NUM_COLS][NUM_ROWS];
        // Check for < shaped clusters
        for(int i=0; i < tiles.length-1; i++) {
            for(int j=1; j < tiles[i].length-1; j++) {
                if(tiles[i][j]!=null &&
                tiles[i+1][j-1]!=null &&
                tiles[i+1][j+1]!=null &&
                tiles[i][j].color == tiles[i+1][j-1].color && 
                tiles[i][j].color == tiles[i+1][j+1].color){
                    result[i][j] = true;
                    result[i+1][j-1] = true;
                    result[i+1][j+1] = true;
                }
            }
        }
        // Check for > shaped clusters
        for(int i=0; i < tiles.length-1; i++) {
            for(int j=0; j < tiles[i].length-2; j++) {
                if(tiles[i][j]!=null &&
                tiles[i][j+2]!=null &&
                tiles[i+1][j+1]!=null&&
                tiles[i][j].color == tiles[i][j+2].color && 
                tiles[i][j].color == tiles[i+1][j+1].color){
                    result[i][j] = true;
                    result[i][j+2] = true;
                    result[i+1][j+1] = true;
                }
            }
        }
        return result;
    }

    public void dropBoard(){
        for(Tile[] col : tiles){
            for(int i = col.length-1 ; i >= 0 ; i--){
                if(col[i]!=null){
                    int j = i;
                    while(j+2 < col.length && col[j+2] == null){
                        col[j+2] = col[j];
                        col[j] = null;
                        j += 2;
                    }
                }
            }
        }
    }
    private void dropColumn(Tile[] col){
        for(int i = col.length ; i >= 0 ; i--){
            int j = i;
            while(j+2 < col.length && col[j+2] == null){
                col[j+2] = col[j];
                col[j] = null;
                j += 2;
            }
        }
    }
}

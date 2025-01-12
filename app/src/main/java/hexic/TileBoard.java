package hexic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class TileBoard {
    private static final int NUM_COLS = 10;
    private static final int NUM_ROWS = 8*2 + 1;
                                    
    private boolean rightRotationTriggered = false;
    private boolean leftRotationTriggered = false;
    private int remainingRotations = 0;

    private final double TILE_X_SPACING = 2 + 3.0*Tile.SIDE_LENGTH/2.0;
    private final double TILE_Y_SPACING = 1 + Tile.SIDE_LENGTH*Math.sin(Math.PI/3);

    private static final Color[] tileColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE};

    private Tile[][] tiles;
    static Cursor cursor = new Cursor();
    public State state = State.SETUP;

    enum State {
        SETUP,
        IDLE,
        ROTATING,
        SCANNING_BOARD,
        DROPPING_TILES,
        ADDING_TILES
    }

    public TileBoard(){
        InitBoard();
    }

    public void boardLogic(){
        switch (state) {
            case SETUP:
                System.out.println("Waiting for board to be ready");
                break;
            case IDLE:
                if (rightRotationTriggered || leftRotationTriggered){
                    state=State.ROTATING;
                    remainingRotations = 3;
                    rightRotationTriggered = false;
                    leftRotationTriggered = false;
                }
                else {
                    System.out.println("Waiting for input");
                }
                break;
            case ROTATING:
                System.out.println("Spinning");
                if(remainingRotations!=0){
                    rotateTiles();
                    state=State.SCANNING_BOARD;
                }
                else{
                    state=State.IDLE;
                }
                break;
            case SCANNING_BOARD:
                System.out.println("Scanning");
                if(scanAndDeleteTiles()){
                    state=State.DROPPING_TILES;
                    remainingRotations = 0;
                }
                else {
                    state=State.ROTATING;
                }
                break;
            case DROPPING_TILES:
                System.out.println("Dropping");
                if(dropTilesOnce()){
                    fillTopRow();
                }
                else if (fillTopRow()){
                    state=State.DROPPING_TILES;
                }
                else{
                    state=State.SCANNING_BOARD;
                }
                break;
            default:
                break;
        }
    }
    
    public void InitBoard(){
        Random rand = new Random();

        // Double the rows so that odd rows can be on the "half" step
        this.tiles = new Tile[NUM_COLS][NUM_ROWS];
        // while(fillTopRow() || dropTilesOnce() || scanAndDeleteTiles()){
        //     System.out.println("im doing work");
        // }
        int startingIndex = 0;
        for(int i=0; i < tiles.length; i++) {
            // Even rows start on 1, Odd rows start on 0 
            startingIndex = (i+1)%2;
            for(int j=startingIndex; j < tiles[i].length; j+=2) {
                // tiles[i][j] = new Tile(tileColors[1]);
                tiles[i][j] = new Tile(tileColors[rand.nextInt(tileColors.length)]);
            }
        }
        state = State.IDLE;
    }

    public void clear(){
        this.tiles = new Tile[NUM_COLS][NUM_ROWS];
    }

    
    public void triggerRotateRight(){
        rightRotationTriggered = true;
    }

    public void triggerRotateLeft(){
        leftRotationTriggered = true;
    }

    private void rotateTiles(){
        if (remainingRotations == 0){
            return;
        }
        else if (remainingRotations > 0){
            return;
        }
        else{
            return;
        }
    }

    /**
     * Scans the board for tiles that match patterns for deletion, and deletes
     * them. Returns true if anything was deleted
     * 
     * @return boolean If at least one tile was delete
     */
    private boolean scanAndDeleteTiles(){
        boolean[][] markedTiles = scanForClusters();
        deleteTiles(markedTiles);
        // TODO: make this cleaner
        boolean result = false;
        for(boolean[] array : markedTiles){
            for (boolean value: array){
                result = result || value;
            }
        }
        return result;
    }

    private void deleteTiles(boolean[][] selection){
        for(int i=0; i < selection.length; i++) {
            for(int j=0; j < selection[i].length; j+=1) {
                if(selection[i][j]){
                    tiles[i][j] = null;
                }
            }
        }
    }

    private boolean[][] scanForClusters(){
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
    
    
    /**
     * Drops a Tile down one position (index + 2) if the position below it is
     * null. Returns true if any Tiles were dropped. 
     * 
     * @return boolean If any tiles were dropped
     */
    public boolean dropTilesOnce(){
        boolean result = false;
        for(Tile[] col : tiles){
            for(int i = col.length-3 ; i >= 0 ; i--){
                if(col[i]!=null && col[i+2] == null){
                    result = true;
                    col[i+2] = col[i];
                    col[i] = null;
                }
            }
        }
        return result;
    }

    public void dropTiles(){
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

    public boolean fillTopRow(){
        boolean result = false;
        Random rand = new Random();
        int startingIndex = 0;
        for(int i = 0 ; i < tiles.length ; i++){
            startingIndex = (i+1)%2;
            if (tiles[i][startingIndex] == null){
                result = true;
                tiles[i][startingIndex] = new Tile(tileColors[rand.nextInt(tileColors.length)]);
            }
        }
        return result;
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
}

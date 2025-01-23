package hexic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hexic.Cursor.Mode;

public class TileBoard {
    private static final int NUM_COLS = 10;
    private static final int NUM_ROWS = 8*2 + 1;
                                    
    private boolean rightRotationTriggered = false;
    private boolean leftRotationTriggered = false;
    private int remainingRotations = 0;

    private final double TILE_X_SPACING = 2.0 + 3.0*Tile.SIDE_LENGTH/2.0;
    private final double TILE_Y_SPACING = 1.0 + Tile.SIDE_LENGTH*Math.sin(Math.PI/3.0);

    private static final Color[] tileColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};

    private Tile[][] tiles;
    private Cursor cursor = new Cursor();

    public State state = State.SETUP;
    public int score;

    enum State {
        SETUP,
        IDLE,
        ROTATING,
        SCANNING_BOARD,
        DROPPING_TILES,
        ADDING_TILES
    }

    enum MoveDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
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
                    int numRotations = 0;
                    if(cursor.mode == Mode.STANDARD){
                        numRotations = 3;
                    }
                    else if(cursor.mode == Mode.STAR){
                        numRotations = 1;
                    }
                    remainingRotations = rightRotationTriggered ? numRotations : -numRotations;
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
                if (cursor.mode != Mode.STANDARD && 
                tiles[cursor.x][cursor.y].getClass() == Tile.class){
                    cursor.mode = Mode.STANDARD;
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
        while(fillTopRow() || dropTilesOnce() || scanAndDeleteTiles()){}
        tiles[4][5] = new Star();
        // int startingIndex = 0;
        // for(int i=0; i < tiles.length; i++) {
        //     // Even rows start on 1, Odd rows start on 0 
        //     startingIndex = (i+1)%2;
        //     for(int j=startingIndex; j < tiles[i].length; j+=2) {
        //         // tiles[i][j] = new Tile(tileColors[1]);
        //         tiles[i][j] = new Tile(tileColors[rand.nextInt(tileColors.length)]);
        //     }
        // }
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

    // TODO make this whole thing cleaner
    public void moveCursor(MoveDirection direction) {
        switch (direction) {
            case MoveDirection.LEFT:
                if (cursor.mode == Mode.STANDARD){
                    // TODO replace this with star type
                    if (tiles[cursor.x][cursor.y] != null && 
                    tiles[cursor.x][cursor.y].color == Color.WHITE){
                        cursor.mode = Mode.STAR;
                    }
                    else{
                        cursor.x = Math.max(cursor.x-1,0);
                    }
                }
                else{
                    cursor.mode = Mode.STANDARD;
                    cursor.x = Math.max(cursor.x-1,0);
                }
                break;

            case MoveDirection.RIGHT:
                if (cursor.mode == Mode.STANDARD){
                    // TODO replace this with star type
                    if (tiles[cursor.x+1][cursor.y] != null && 
                    tiles[cursor.x+1][cursor.y].color == Color.WHITE){
                        cursor.x = Math.min(cursor.x+1,NUM_COLS-1);
                        cursor.mode = Mode.STAR;
                    }
                    else{
                        cursor.x = Math.min(cursor.x+1,NUM_COLS-1);
                    }
                }
                else{
                    cursor.mode = Mode.STANDARD;
                }
                break;

            case MoveDirection.UP:
                cursor.y = Math.max(cursor.y-1,0);
                break;

            case MoveDirection.DOWN:
                cursor.y = Math.min(cursor.y+1,NUM_ROWS-1);
                break;

            default:
                break;
        }
    }

    private void rotateTiles(){
        Tile temp;
        int[][] swapIndices = new int[][] {};
        switch (cursor.mode) {
            case Mode.STANDARD:
                boolean flipped = ((cursor.x+cursor.y)%2 == 0);
                if (flipped){
                    swapIndices  = new int[][]{
                        {cursor.x , cursor.y + 1},
                        {cursor.x + 1, cursor.y},
                        {cursor.x,cursor.y - 1}
                    };
                } 
                else {
                    swapIndices  = new int[][]{
                        {cursor.x,cursor.y},
                        {cursor.x + 1,cursor.y + 1},
                        {cursor.x + 1,cursor.y - 1}
                    };
                }
                break;
            case Mode.STAR:
                swapIndices  = new int[][]{
                    {cursor.x , cursor.y + 2},
                    {cursor.x + 1, cursor.y + 1},
                    {cursor.x + 1,cursor.y - 1},
                    {cursor.x,cursor.y - 2},
                    {cursor.x - 1,cursor.y - 1},
                    {cursor.x - 1,cursor.y + 1},
                };
                break;
            default:
                break;
        }

        if (remainingRotations == 0){
            return;
        }

        for(int[] index : swapIndices){
            if(index[0] < 0 || index[0] >= NUM_COLS ||
            index[1] < 0|| index[0] >= NUM_ROWS ||
            tiles[index[0]][index[1]] == null){
                System.out.println("Skipping illegal move");
                remainingRotations = 0;
                return;
            }
        }
        
        if (remainingRotations < 0){
            List<int[]> list = Arrays.asList(swapIndices);
            Collections.reverse(list);
            swapIndices = list.toArray(swapIndices);
        }

        temp = tiles[swapIndices[0][0]][swapIndices[0][1]];
        for (int i=0 ; i < swapIndices.length-1; i++){
            tiles[swapIndices[i][0]][swapIndices[i][1]] = tiles[swapIndices[i + 1][0]][swapIndices[i + 1][1]];
        }
        tiles[swapIndices[swapIndices.length-1][0]][swapIndices[swapIndices.length-1][1]] = temp;
        remainingRotations += remainingRotations > 0 ? -1 : 1;
    }



    /**
     * Scans the board for tiles that match patterns for deletion, and deletes
     * them. Returns true if anything was deleted
     * 
     * @return boolean If at least one tile was delete
     */
    private boolean scanAndDeleteTiles(){
        boolean result = false;
        scanForPatterns();
        scanForClusters();
        promoteTiles();
        result = deleteTiles();
        return result;
    }

    private void promoteTiles(){
        for(int i=0; i < tiles.length; i++) {
            for(int j=0; j < tiles[i].length; j+=1) {
                if(tiles[i][j]!=null && tiles[i][j].markedForPromotion){
                    tiles[i][j] = new Star();
                }
            }
        }
    }

    private boolean deleteTiles(){
        boolean result = false;
        for(int i=0; i < tiles.length; i++) {
            for(int j=0; j < tiles[i].length; j+=1) {
                if(tiles[i][j]!=null && tiles[i][j].markedForDeletion){
                    tiles[i][j] = null;
                    result = true;
                    score += 50;
                }
            }
        }
        return result;
    }

    private void scanForPatterns(){
        // Check for < shaped clusters
        for(int i=1; i < tiles.length-1; i++) {
            for(int j=2; j < tiles[i].length-2; j++) {
                if(tiles[i][j]  !=  null &&
                tiles[i][j-2]   !=  null &&
                tiles[i+1][j+1] !=  null &&
                tiles[i+1][j-1] !=  null &&
                tiles[i][j+2]   !=  null &&
                tiles[i-1][j-1] !=  null &&
                tiles[i-1][j+1] !=  null &&
                tiles[i][j-2].color == tiles[i+1][j+1].color && 
                tiles[i][j-2].color == tiles[i+1][j-1].color && 
                tiles[i][j-2].color == tiles[i][j+2].color && 
                tiles[i][j-2].color == tiles[i-1][j-1].color && 
                tiles[i][j-2].color == tiles[i-1][j+1].color){
                    tiles[i][j].markForPromotion();
                    tiles[i][j-2].markForDeletion();
                    tiles[i+1][j+1].markForDeletion();
                    tiles[i+1][j-1].markForDeletion();
                    tiles[i][j+2].markForDeletion();
                    tiles[i-1][j-1].markForDeletion();
                    tiles[i-1][j+1].markForDeletion();
                }
            }
        }
    }

    private void scanForClusters(){
        // Check for < shaped clusters
        for(int i=0; i < tiles.length-1; i++) {
            for(int j=1; j < tiles[i].length-1; j++) {
                if(tiles[i][j]!=null &&
                tiles[i+1][j-1]!=null &&
                tiles[i+1][j+1]!=null &&
                tiles[i][j].color == tiles[i+1][j-1].color && 
                tiles[i][j].color == tiles[i+1][j+1].color){
                    tiles[i][j].markForDeletion();
                    tiles[i+1][j-1].markForDeletion();
                    tiles[i+1][j+1].markForDeletion();
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
                    tiles[i][j].markForDeletion();
                    tiles[i][j+2].markForDeletion();
                    tiles[i+1][j+1].markForDeletion();
                }
            }
        }
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
                    tiles[i][j].paintTile(g, TILE_X_SPACING*i, TILE_Y_SPACING*j);
                }
            }
        }
        boolean flipped = ((cursor.x+cursor.y)%2 == 0);
        double flipOffset = flipped ? 0 : Tile.SIDE_LENGTH*Math.cos(Math.PI/3);
        cursor.paint(g, (cursor.x+1)*TILE_X_SPACING + flipOffset, (1+cursor.y)*TILE_Y_SPACING, flipped);
    }
}

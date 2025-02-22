package hexic;

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

public class Tile {
    public Color color = Color.red;

    public static final double SIDE_LENGTH = 20;
    public boolean markedForDeletion;
    public boolean markedForPromotion;
    
    public Tile(Color color){
        this.color = color;
    }

    public void markForDeletion(){
        markedForDeletion = true;
    }

    public void markForPromotion(){
        markedForPromotion = true;
    }

    public void paintTile(Graphics g, double xOffset, double yOffset) {
        // Cast Graphics to Graphics2D for more features
        Graphics2D g2d = (Graphics2D) g;
        int[] xPoints = {(int) Math.round(xOffset+SIDE_LENGTH*Math.cos(Math.PI/3)), 
            (int) Math.round(xOffset+SIDE_LENGTH+SIDE_LENGTH*Math.cos(Math.PI/3)),
            (int) Math.round(xOffset+SIDE_LENGTH+2*SIDE_LENGTH*Math.cos(Math.PI/3)),
            (int) Math.round(xOffset+SIDE_LENGTH+SIDE_LENGTH*Math.cos(Math.PI/3)),
            (int) Math.round(xOffset+SIDE_LENGTH*Math.cos(Math.PI/3)),
            (int) Math.round(xOffset)
        };
        int[] yPoints = {(int) Math.round(yOffset),
            (int) Math.round(yOffset),
            (int) Math.round(yOffset+SIDE_LENGTH*Math.sin(Math.PI/3)),
            (int) Math.round(yOffset+2*SIDE_LENGTH*Math.sin(Math.PI/3)),
            (int) Math.round(yOffset+2*SIDE_LENGTH*Math.sin(Math.PI/3)),
            (int) Math.round(yOffset+SIDE_LENGTH*Math.sin(Math.PI/3))
        };
        g2d.setColor(color);
        g2d.fillPolygon(xPoints,yPoints,6);
    }
}

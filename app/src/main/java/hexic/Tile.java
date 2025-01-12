package hexic;

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

public class Tile {
    public Color color = Color.red;

    public static final int SIDE_LENGTH = 20;
    
    public Tile(Color color){
        this.color = color;
    }

    public void paintTile(Graphics g, int xOffset, int yOffset) {
        // Cast Graphics to Graphics2D for more features
        Graphics2D g2d = (Graphics2D) g;
        int[] xPoints = {xOffset+(int)(SIDE_LENGTH*Math.cos(Math.PI/3)), 
            xOffset+SIDE_LENGTH+(int)(SIDE_LENGTH*Math.cos(Math.PI/3)),
            xOffset+SIDE_LENGTH+(int)(2*SIDE_LENGTH*Math.cos(Math.PI/3)),
            xOffset+SIDE_LENGTH+(int)(SIDE_LENGTH*Math.cos(Math.PI/3)),
            xOffset+(int)(SIDE_LENGTH*Math.cos(Math.PI/3)),
            xOffset
        };
        int[] yPoints = {yOffset,
            yOffset,
            yOffset+(int)(SIDE_LENGTH*Math.sin(Math.PI/3)),
            yOffset+(int)(2*SIDE_LENGTH*Math.sin(Math.PI/3)),
            yOffset+(int)(2*SIDE_LENGTH*Math.sin(Math.PI/3)),
            yOffset+(int)(SIDE_LENGTH*Math.sin(Math.PI/3))
        };
        g2d.setColor(color);
        g2d.fillPolygon(xPoints,yPoints,6);
    }
}

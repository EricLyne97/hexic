package hexic;

import java.awt.Color;
import java.awt.*;

public class Star extends Tile{
    public Star(){
        super(Color.WHITE);
    }

    @Override
    public void markForDeletion(){
        markedForDeletion = false;
    }

    @Override
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
        g2d.setColor(Color.BLACK);
        g2d.drawLine(xPoints[0],yPoints[0],xPoints[3],yPoints[3]);
        g2d.drawLine(xPoints[1],yPoints[1],xPoints[4],yPoints[4]);
        g2d.drawLine(xPoints[2],yPoints[2],xPoints[5],yPoints[5]);
    }

}

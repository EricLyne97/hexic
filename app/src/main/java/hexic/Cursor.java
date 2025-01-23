package hexic;

import javax.swing.*;
import java.awt.*;

public class Cursor {
    public static final Color color = Color.BLACK;

    public static final int TEMP = 20;
    public static final int EDGE_TO_CENTER = 10;

    public Mode mode = Mode.STANDARD;

    public int x = 0;
    public int y= 1;

    enum Mode {
        STANDARD,
        STAR,
        BLACK_PEARL
    }

    public void paint(Graphics g, double xOffset, double yOffset, boolean flipped) {
        // Cast Graphics to Graphics2D for more features
        Graphics2D g2d = (Graphics2D) g;
        int[] xPoints;
        switch (mode) {
            case Mode.STANDARD:
                if(flipped){
                    xPoints = new int[] {(int) Math.round(xOffset - EDGE_TO_CENTER),
                        (int) Math.round(xOffset + EDGE_TO_CENTER*Math.cos(Math.PI/3)),
                        (int) Math.round(xOffset + EDGE_TO_CENTER*Math.cos(Math.PI/3))
                    };
                }
                else{
                    xPoints = new int[] {(int) Math.round(xOffset + EDGE_TO_CENTER),
                        (int) Math.round(xOffset - EDGE_TO_CENTER*Math.cos(Math.PI/3)),
                        (int) Math.round(xOffset - EDGE_TO_CENTER*Math.cos(Math.PI/3))
                    };
                }   
                int[] yPoints = {(int) Math.round(yOffset),
                    (int) (yOffset + EDGE_TO_CENTER*Math.sin(Math.PI/3)),
                    (int) (yOffset - EDGE_TO_CENTER*Math.sin(Math.PI/3))
        
                };
                g2d.setColor(color);
                g2d.fillPolygon(xPoints,yPoints,3);
                break;
            
            case Mode.STAR:
                xPoints = new int[] {(int) xOffset,
                    (int) xOffset + 5,
                    (int) xOffset + 5,
                    (int) xOffset
                };
                yPoints = new int[] { (int) yOffset,
                    (int) yOffset,
                    (int) yOffset + 5,
                    (int) yOffset + 5
                };
                g2d.setColor(color);
                g2d.fillPolygon(xPoints,yPoints,4);
                break;
            
            default:
                break;
        }
    }
    
}

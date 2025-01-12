package hexic;

import javax.swing.*;


import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;

public class HexicPanel extends JPanel {
    private static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;
    static TileBoard tileBoard = new TileBoard();
    
    public HexicPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                boolean[][] test = tileBoard.scanForClusters();
                tileBoard.deleteTiles(test);;
                repaint();
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        tileBoard.paint(g);
        // tileBoard.paint(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT);
    }
}

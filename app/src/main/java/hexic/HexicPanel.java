package hexic;

import javax.swing.*;

import hexic.TileBoard.MoveDirection;
import hexic.TileBoard.State;

import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
                if (tileBoard.state == State.IDLE){
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        tileBoard.triggerRotateLeft();
                        System.out.println("Left");
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        tileBoard.triggerRotateRight();
                        System.out.println("Right");
                    }
                }  
            }
        });
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (tileBoard.state == State.IDLE){
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_LEFT) {
                        tileBoard.moveCursor(MoveDirection.LEFT);
                    }
                
                    if (key == KeyEvent.VK_RIGHT) {
                        tileBoard.moveCursor(MoveDirection.RIGHT);
                    }
                
                    if (key == KeyEvent.VK_UP) {
                        tileBoard.moveCursor(MoveDirection.UP);
                    }
                
                    if (key == KeyEvent.VK_DOWN) {
                        tileBoard.moveCursor(MoveDirection.DOWN);
                    }

                    if (key == KeyEvent.VK_SPACE) {
                        tileBoard.triggerRotateLeft();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tileBoard.boardLogic();
                repaint();
            }
        });
        timer.start();

        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        tileBoard.paint(g);
        g.setColor(Color.WHITE);
        g.drawString(String.format("Score: %d", tileBoard.score), 20, 400);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT);
    }
}

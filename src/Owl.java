
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Owl extends SnakeFriends {
    
    private static final String IMG_FILE = "files/owl.png";
    public static final int SIZE = 30;
    public static final int INIT_POS_X = 130;
    public static final int INIT_POS_Y = 130;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    private  static BufferedImage img;
    private LinkedList<Point> gameObjects;



    public Owl(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        

        gameObjects = new LinkedList<Point>();
        Point p = new Point(INIT_POS_X, INIT_POS_Y);
        gameObjects.addFirst(p);
        

    }
    
    @Override
    public boolean intersects(GameObj that) {
      
        for (Point p: gameObjects) {

            if (p.x + this.getWidth() >= that.getPx()
                  && p.y + this.getHeight() >= that.getPy()
                  && that.getPx() + that.getWidth() >= p.x
                  && that.getPy() + that.getHeight() >= p.y) {
                return true;
            }
        }
        return false;
      

    }
  
  

    @Override
    public int spawn() {
        //Owl only spawns in top left corner - "Owl Zone"
          
        int randomX = (int) (Math.random() * getMaxX() / 2);
        int randomY = (int) (Math.random() * getMaxY() / 2);
          

        Point newObj = new Point(randomX, randomY);
        gameObjects.add(newObj);
        return gameObjects.size() - 1;
    }
          
      

    @Override
    public void draw(Graphics g) {

        for (Point particle: gameObjects) {
            g.drawImage(img, particle.x, particle.y, this.getWidth(), this.getHeight(), null);

        }
    }
    
    @Override
    public LinkedList<Point> getGameObjects() {
        return (LinkedList<Point>) gameObjects.clone();
    }

}

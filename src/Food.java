/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and that all objects
 * created by this constructor share the same image data (i.e. img is static). This is important for
 * efficiency: your program will go very slowly if you try to create a new BufferedImage every time
 * the draw method is invoked.
 */
public class Food extends SnakeFriends {
    
    private static final String IMG_FILE = "files/mouse.jpg";
    public static final int SIZE = 20;
    public static final int INIT_POS_X = 400;
    public static final int INIT_POS_Y = 400;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private static final int MAX_FOOD_PARTICLES = 4;
    
    private static BufferedImage img;
    private LinkedList<Point> gameObjects;


    public Food(int courtWidth, int courtHeight) {
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
      
        for (int i = 0; i < gameObjects.size(); i++) {
            Point p = gameObjects.get(i);
            if (p.x + this.getWidth() >= that.getPx()
                  && p.y + this.getHeight() >= that.getPy()
                  && that.getPx() + that.getWidth() >= p.x
                  && that.getPy() + that.getHeight() >= p.y) {

                gameObjects.remove(i);
                return true;
            }
        }
        return false;
    }
  
  

    @Override
    public int spawn() {
        //chance of spawning will not always be up to max, will not spawn in top left "Owl Zone"
        int limit = (int) (Math.random() * MAX_FOOD_PARTICLES);
        if (limit == 0) {
            limit = 1;
        }
      
        while (gameObjects.size() < limit + 1) {
          
            //random point in right half
            double xProbability = Math.random();
            if (xProbability < 0.5) {
                xProbability += 0.5;
            }
          
            int randomX = (int) (xProbability * getMaxX());                   
            int randomY = (int) (Math.random() * getMaxY());
          
            //random point in bottom half
            double yProbability = Math.random();
            if (yProbability < 0.5) {
                yProbability += 0.5;
            }
          
            int randomX1 = (int) (Math.random() * getMaxX());
            int randomY1 = (int) (yProbability * getMaxY());
          
          
            //random chance in right half or bottom half
            if (Math.random() < 0.5) {
                Point newObj = new Point(randomX, randomY);
                gameObjects.add(newObj);
            } else {
                Point newObj1 = new Point(randomX1, randomY1);
                gameObjects.add(newObj1);
            }
          
          
        }
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

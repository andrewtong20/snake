/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.util.LinkedList;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * square of a specified color.
 */
public class Snake extends GameObj {
    public static final int SIZE = 10;
    public static final int INIT_POS_X = 0;
    public static final int INIT_POS_Y = 0;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    
    private int score;

    private Color color;
    private LinkedList<Point> gameObjects;

    public Snake(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        this.color = color;
        this.score = 0;
        
        
        gameObjects = new LinkedList<Point>();
        Point p = new Point(INIT_POS_X, INIT_POS_Y);
        gameObjects.addFirst(p);
    }
    
    //Setters
    
    @Override
    public void move() {
        //body of snake is updated to the point before it
        for (int i = gameObjects.size() - 1; i > 0; i--) {
            Point old = gameObjects.get(i - 1);
            gameObjects.get(i).setLocation(old); 
        }
        
        //head of snake is updated to new point
        int newX = getPx() + getVx();
        int newY = getPy() + getVy();
        
        //to keep snake moving
        setPx(newX);
        setPy(newY);
        Point newHead = new Point(newX, newY);
        gameObjects.set(0, newHead);
       
        clip();
        
    }
    
    @Override
    public boolean intersects(GameObj that) {
       
        for (Point p : gameObjects) {
            if (p.x + this.getWidth() >= that.getPx()
                    && p.y + this.getHeight() >= that.getPy()
                    && that.getPx() + that.getWidth() >= p.x
                    && that.getPy() + that.getHeight() >= p.y) {
                return true;
            }
            
        }
        return false;

    }
    

    
    public void growSnake(int length) { 

        for (int i = 0; i < length; i++) {
            Point tail = new Point(gameObjects.getLast());
            gameObjects.addLast(tail);
        }
        
    }
    
    
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void incrementScore(int increment) {
        this.score += increment;
    }
    
    
    public boolean willHitWall() {
        return ((getPx() + getVx() > getMaxX()) || (getPx() + getVx() < 0) ||
                (getPy() + getVy() > getMaxY()) || (getPy() + getVy() < 0));

    }
    
    public boolean willHitOwnBody() {
        //only factors in when snake's length is greater than 1
        if (gameObjects.size() > 1) {
            Point head = gameObjects.get(0);
            
            for (Point body: gameObjects) {

                if ((head.x + getVx() == body.x) && (head.y + getVy() == body.y)) {
                    return true;
                }
                
            }

        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        
        for (Point segment: gameObjects) {
            g.fillRect(segment.x, segment.y, this.getWidth(), this.getHeight());
        }


    }
    

    
    
    public LinkedList<Point> getGameObjects() {
        return (LinkedList<Point>) gameObjects.clone();
    }
    
    
    //Getters
    public int getScore() {
        return this.score;
    }
    
    public int getLength() {
        return gameObjects.size();
    }
    
   
}
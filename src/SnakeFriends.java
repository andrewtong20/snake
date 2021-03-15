import java.awt.Point;
import java.util.LinkedList;

public abstract class SnakeFriends extends GameObj {
    
    private LinkedList<Point> gameObjects;
    
    public SnakeFriends(int vx, int vy, int px, int py, int width, 
            int height, int courtWidth, int courtHeight) {
        super(vx, vy, px, py, width, height, courtWidth, courtHeight);

        
        gameObjects = new LinkedList<Point>();
        Point p = new Point(px, py);
        gameObjects.addFirst(p);
    }
    
    //spawn method will be overridden in each of the animals snake interacts with
    //returns index of the current object being spawned
    public int spawn() {
        //spawns gameObj at random point in whole canvas, one object at a time
        int randomX = (int) (Math.random() * getMaxX());
        int randomY = (int) (Math.random() * getMaxY());
        Point newObj = new Point(randomX, randomY);
        gameObjects.add(newObj);
        
        return gameObjects.size() - 1;
    }
    
    public void despawn(int index) {
        gameObjects.remove(index);
    }
    
    
    public LinkedList<Point> getGameObjects() {
        return (LinkedList<Point>) gameObjects.clone();
    }
}

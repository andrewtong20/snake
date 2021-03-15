import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import java.util.*;
import java.awt.*;
/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {
    
    Snake snake;
    SnakeFriends superfood;
    SnakeFriends food;
    SnakeFriends owl;
    
    IOProcessing fileProcessor = new IOProcessing();
    
    @BeforeEach
    public void beforeEach() {
        snake = new Snake(600, 600,Color.RED);
        superfood = new Superfood(600, 600);
        food = new Food(600, 600);
        owl = new Owl(600, 600);
        
        fileProcessor = new IOProcessing();
    }
    
    @Test
    public void testSnakeGrow() {
        snake.growSnake(1);
        assertEquals(2, snake.getLength());
       
    }
    
    @Test
    public void testSnakeGrowMultiple() {
        snake.setPx(0);
        snake.setPy(0);
        snake.setVx(5);
        snake.setVy(0);
        snake.move();
        snake.growSnake(5); 
        snake.move();
        snake.move();
        LinkedList<Point> snakeSegments = snake.getGameObjects();
        assertEquals(snakeSegments.size(), 6); //now size 6 because start at 1
        assertEquals(snakeSegments.get(0).x, 15); //head now at 15 bc moved three times
        assertEquals(snakeSegments.get(1).x, 10); //second segment at 10 bc after first segment
        assertEquals(snakeSegments.get(2).x, 5); //third segment at 5
    }
    
    @Test
    public void testSnakeMoveLengthWontChange() {
        snake.move();
        assertEquals(1, snake.getLength());
        
    }
    
    @Test
    public void testSnakeMoveLocation() {
        snake.setPx(0);
        snake.setPy(0);
        snake.setVx(5);
        snake.setVy(0);
        snake.move();
        assertEquals(5, snake.getPx());
    }

    
    @Test
    public void testSnakeTurnOkay() {
        snake.setPx(0);
        snake.setPy(0);
        snake.setVx(5);
        snake.setVy(0);
        snake.growSnake(5); 
        
        //move right 5 times
        for (int i = 0; i < 5; i++) {
            snake.move();
        }
        LinkedList<Point> snakeSegments = snake.getGameObjects();
        
        
        snake.setVx(0);
        snake.setVy(5);
        
        //move down twice
        for (int i = 0; i < 2; i++) {
            snake.move();
        }
        snakeSegments = snake.getGameObjects();
        assertEquals(snakeSegments.get(0).y, 10);
        assertEquals(snakeSegments.get(0).x, 25);
        assertEquals(snakeSegments.size(), 6); //now size 6 because start at 1
    }
    
    @Test
    public void testSnakeDiesWall() {
        //set up to hit left wall
        snake.setPx(0);
        snake.setPy(0);
        snake.setVx(-1);
        snake.setVy(0);
        assertTrue(snake.willHitWall());
    }
    
    
    
    @Test
    public void testSnakeDiesCrossesItself() { //need to redy based on grow
        snake.setPx(0);
        snake.setPy(0);
        snake.setVx(5);
        snake.setVy(0);
        snake.growSnake(5); 
        
        for (int i = 0; i < 5; i++) {
            snake.move();
        }
        LinkedList<Point> snakeSegments = snake.getGameObjects();
        
        
        assertEquals(snakeSegments.size(), 6); 
        assertEquals(snakeSegments.get(0).x, 25); 
        assertEquals(snakeSegments.get(1).x, 20); 
        assertEquals(snakeSegments.get(4).x, 5); 
        
        //move down
        snake.setVx(0);
        snake.setVy(5);
        snake.move();
        snakeSegments = snake.getGameObjects();
        assertEquals(snakeSegments.get(0).x, 25);
        assertEquals(snakeSegments.get(0).y, 5);
        
        
        //move left
        snake.setVx(-5);
        snake.setVy(0);
        snake.move();
        snakeSegments = snake.getGameObjects();
        assertEquals(snakeSegments.get(0).x, 20);
        assertEquals(snakeSegments.get(0).y, 5);
        
        //move up
        snake.setVx(0);
        snake.setVy(-5);

        snakeSegments = snake.getGameObjects();
        //will hit own body (at the 5th segment for the next snake.move())
        assertTrue(snake.willHitOwnBody());
        
    }
    
    @Test
    public void testSnakeIncreaseScore() {
        snake.setScore(0);
        snake.incrementScore(10);
        assertEquals(snake.getScore(), 10);
    }
    
    @Test
    public void testSnakeResetScore() {
        snake.incrementScore(10);
        snake.incrementScore(10);
        snake.setScore(0);
        assertEquals(snake.getScore(), 0);
    }
    
    @Test
    public void testSnakeOutOfBounds() {
        snake.setPx(1000);
        int maxX = snake.getMaxX();
        assertEquals(maxX, snake.getPx());
    }
    
    @Test
    public void testSnakeFriendOutOfBounds() {
        owl.setPx(1000);
        int maxX = owl.getMaxX();
        assertEquals(maxX, owl.getPx());
    }
    
    @Test
    public void testSnakeIntersectsOwl() {
        //set position of owl
        owl.setPx(130);
        owl.setPy(130);
        //set position of snake
        snake.setPx(125);
        snake.setPy(130);
        snake.setVx(5);
        snake.setVy(0);
        snake.move();
        
        LinkedList<Point> owls = owl.getGameObjects();
        assertEquals(snake.getPx(), owls.get(0).x);
        assertEquals(snake.getPy(), owls.get(0).y);
        assertTrue(owl.intersects(snake));
    }
    
    @Test
    public void testSnakeIntersectFood() {
        //set position of food
        food.setPx(400);
        food.setPy(400);
        //set position of snake
        snake.setPx(400);
        snake.setPy(400);
        
        assertTrue(food.intersects(snake));
    }
    
    
    @Test
    public void testSnakeIntersectSuperfood() {
        //set position of superfood
        superfood.setPx(30);
        superfood.setPy(30);
        //set position of snake
        snake.setPx(30);
        snake.setPy(30);
        
        assertTrue(superfood.intersects(snake));
    }
    

    
    @Test
    public void testOwlSpawnTopLeft() { //update so that it's always spawn top left
        int maxX = owl.getMaxX();
        int maxY = owl.getMaxY();
        owl.spawn();
       

       
        LinkedList<Point> owls = owl.getGameObjects();
       
        Point spawnedOwl = owls.get(1); 
       
       
        assertTrue((spawnedOwl.x <= (maxX / 2)) && (spawnedOwl.y <= (maxY / 2)));
    }
    
    @Test
    public void testSuperfoodSpawnTopLeft() { // update so that it always spawns top left
        int maxX = superfood.getMaxX();
        int maxY = superfood.getMaxY();
        superfood.spawn();
        

        
        LinkedList<Point> bullfrogs = superfood.getGameObjects();
        
        Point spawnedBullfrog = bullfrogs.get(1); 
        
        
        assertTrue((spawnedBullfrog.x <= (maxX / 2)) && (spawnedBullfrog.y <= (maxY / 2)));
    }
    
    @Test
    public void testFoodSpawnNotTopLeft() {
        int maxX = food.getMaxX();
        int maxY = food.getMaxY();
        food.spawn();
        
        LinkedList<Point> mice = food.getGameObjects();
        
        Point spawnedMouse = mice.get(1);
        
        assertTrue(((spawnedMouse.x >= maxX / 2) || (spawnedMouse.y >= maxY / 2)));
        
    }
    

    @Test
    public void testEncapsulation() {

        food.spawn();
        
        LinkedList<Point> mice = food.getGameObjects();
        
        Point randomPoint = new Point(1,1);
        
        mice.set(1, randomPoint);
        
        assertNotEquals(food.getGameObjects().get(1).x, randomPoint.x);
    }
    
    


}

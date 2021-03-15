/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;

import java.util.LinkedList;
import java.util.List;
/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic, SnakeFriends static type is SnakeFriends
    private Snake snake; 
    private SnakeFriends superfood;
    private SnakeFriends food;
    private SnakeFriends owl;

    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    
    private IOProcessing fileProcessor;
    private String userName;

    // Game constants
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 600;
    
    
    public static final int SNAKE_VELOCITY = 5;  

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    public static final String FILE_PATH = "files/high_scores.txt";

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.GREEN);
        
        fileProcessor = new IOProcessing();
        fileProcessor.readFile(FILE_PATH);


        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); 

        // Enable keyboard focus on the court area.
        
        setFocusable(true);

        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    snake.setVx(-SNAKE_VELOCITY);
                    snake.setVy(0);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    snake.setVx(SNAKE_VELOCITY);
                    snake.setVy(0);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    snake.setVx(0);
                    snake.setVy(SNAKE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    snake.setVx(0);
                    snake.setVy(-SNAKE_VELOCITY);
                }
            }

        });

        this.status = status;
    }
    
    public void displayLeaderBoardTable() {
        List<String> sortedUserNames = fileProcessor.getSortedUsers();
        List<Integer> sortedScores = fileProcessor.getSortedScores();
        
        String displayText = "Your Score--" + Integer.toString(snake.getScore()) + "\n"
                + "All-time Leaderboard: \n";
        
        if (sortedScores.size() >= 3) {
            int rank = 1;
            for (int i = 0; i < 3; i++) {
                String leaderName = sortedUserNames.get(i);
                int leaderScore = sortedScores.get(i);
                String leaderScoreConverted = Integer.toString(leaderScore);
                
                String rankConverted = Integer.toString(rank);
                displayText += rankConverted + ". " + leaderName + "--" + 
                    leaderScoreConverted + "  \n"; 
                rank++;
            }
        } else if (sortedScores.size() == 0) {
            displayText += "1. None  2. None  3. None\n"; 
        } else if (sortedScores.size() == 1) {
            String leaderName = sortedUserNames.get(0);
            int leaderScore = sortedScores.get(0);
            String leaderScoreConverted = Integer.toString(leaderScore);
            displayText += "1. " + leaderName + "--" + leaderScoreConverted + 
                    "  2. None  3. None\n";
        } else if (sortedScores.size() == 2) {
            int rank = 1;
            for (int i = 0; i < 2; i++) {
                String leaderName = sortedUserNames.get(i);
                int leaderScore = sortedScores.get(i);
                String leaderScoreConverted = Integer.toString(leaderScore);
                
                String rankConverted = Integer.toString(rank);
                displayText += rankConverted + ".  " + leaderName + "--" + leaderScoreConverted 
                        + "  "; 
                rank++;
            }
            
            displayText += "  3. None\n";
        }
        
        status.setText(displayText);
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        
        
        
        snake = new Snake(COURT_WIDTH, COURT_HEIGHT, Color.RED);
        snake.setScore(0);
        superfood = new Superfood(COURT_WIDTH, COURT_HEIGHT);
        food = new Food(COURT_WIDTH, COURT_HEIGHT);
        owl = new Owl(COURT_WIDTH, COURT_HEIGHT);

        playing = true;
        
        displayLeaderBoardTable();

        
        
        String instructions = "Welcome to Andrew's version of Snake!  \nYour goal is to control "
                + "your snake to eat animals for the highest score. \nUse the arrow keys to move "
                + "your snake up, down, left, and right. \nYou can eat mice (normal food) by "
                + "crossing your snake head over the image. \nEating mice "
                + "increases your score slowly and lengthens your snake. \nIf you are brave enough"
                + " you can also choose to eat the bullfrogs (superfood), "
                + "found mainly in the top left of the "
                + "game. \nThese bullfrogs increase your score faster and lengthen your snake at "
                + "a slower rate. \nBut be careful! This top-left quarter is known as the "
                + "'Owl Zone.' \n"
                + "Every bullfrog you eat creates a small chance of an additional owl being spawned"
                + ", \nwhich kill you automatically if you cross your snake over the owl. "
                + "\nDon't worry though, you'll only encounter these owls and bullfrogs"
                + " in the designated 'Owl Zone'! \n"
                + " \nIf you would rather play it safe, don't worry because mice never venture into"
                + " the 'Owl zone'. \nAdditionally, you will also "
                + "die if you hit the boundaries of the screen or run into your own body.\n"
                + "Be careful and good luck! If you are one of the top 3 scorers, \nyou'll also get"
                + " the chance to have your name on the leaderboard! \nTo start the game, please"
                + " first enter your name. \nNote that if you play multiple times with the same"
                + " username the score we display will always be the highest lifetime score.";
        userName = JOptionPane.showInputDialog(instructions);
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {

            snake.move();
            
            
            if (food.intersects(snake)) {
                snake.growSnake(10);
                snake.incrementScore(1);
                int foodIndex = food.spawn();
                
                //handle overlap
                LinkedList<Point> snakeBody = snake.getGameObjects();
                LinkedList<Point> foodObjects = food.getGameObjects();
                Point spawnedFood = foodObjects.get(foodIndex);
                boolean overlap = true;
                boolean intermediary = true;
                while (overlap) {
                    for (Point body : snakeBody) {
                        if (body.x == spawnedFood.x && body.y == spawnedFood.y) {
                            food.despawn(foodIndex);
                            food.spawn();
                            intermediary = true;
                        } else {
                            intermediary = false;
                        }
                    }
                    
                    if (!intermediary) {
                        overlap = false;
                    }
                }
                
                displayLeaderBoardTable();
                
            }
            
            if (superfood.intersects(snake)) {
                snake.growSnake(5);
                snake.incrementScore(5);
                int superfoodIndex = superfood.spawn();
                
                //handle overlap
                LinkedList<Point> snakeBody = snake.getGameObjects();
                LinkedList<Point> superfoodObjects = superfood.getGameObjects();
                Point spawnedSuperfood = superfoodObjects.get(superfoodIndex);
                boolean overlap = true;
                boolean intermediary = true;
                
                while (overlap) {
                    for (Point body : snakeBody) {
                        if (body.x == spawnedSuperfood.x && body.y == spawnedSuperfood.y) {
                            superfood.despawn(superfoodIndex);
                            superfood.spawn();
                            intermediary = true;
                        } else {
                            intermediary = false;
                        }
                    }
                    
                    if (!intermediary) {
                        overlap = false;
                    }
                }
                
                //everytime you eat superfood, there is chance that another owl comes by
                double randomProb = Math.random();
                
                if (randomProb < .05) {
                    int owlIndex = owl.spawn();
                    
                    //handle overlap
                    snakeBody = snake.getGameObjects();
                    LinkedList<Point> owlObjects = owl.getGameObjects();
                    Point spawnedOwl = owlObjects.get(owlIndex);
                    boolean overlap1 = true;
                    boolean intermediary1 = true;
                    while (overlap1) {
                        for (Point body : snakeBody) {
                            if (body.x == spawnedOwl.x && body.y == spawnedOwl.y) {
                                owl.despawn(owlIndex);
                                owl.spawn();
                                intermediary1 = true;
                            } else {
                                intermediary1 = false;
                            }
                        }
                        
                        if (!intermediary1) {
                            overlap1 = false;
                        }
                        
                        
                    }

                    
                }
                displayLeaderBoardTable();
 
            } else if (owl.intersects(snake) || snake.willHitOwnBody() || snake.willHitWall()) {
                playing = false;
                
                
                fileProcessor.writeFile(snake.getScore(), userName);
                fileProcessor.readFile(FILE_PATH); //to update to new score
                displayLeaderBoardTable();
                
                JOptionPane.showMessageDialog(null, "You lose! Check the bottom status bar to see"
                        + " if your score ranks in the top 3. \nNote: if your current score "
                        + "is below your personal best, your score on the leadership board"
                        + " will not be updated.");
            } 
            

            repaint();
            
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
        food.draw(g);
        superfood.draw(g);
        owl.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class GameBoard extends JPanel implements ActionListener {
    private final int boardWidth = 1300;
    private final int boardLength = 750;
    private final int snakeBodySize = 50;
    private final int initialDelay = 175;
    private Timer timer;


    private int snakeBodyInitial = 6;
    private int[] xPos;
    private int[] yPos;
    private int foodXPos;
    private int foodYPos;
    private int score = 0;


    private boolean moveLeft = false;
    private boolean moveRight = true;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean gameCont = false;
    private boolean gameStart = false;


    public GameBoard() {
        this.setPreferredSize(new Dimension(boardWidth, boardLength));
        setFocusable(true); //Allows keyboard input
        addKeyListener(new Adapter());
        xPos = new int[(boardWidth / snakeBodySize) * (boardLength / snakeBodySize)];
        yPos = new int[(boardWidth / snakeBodySize) * (boardLength / snakeBodySize)];
        gameStart = false;
        gameSetup();
        gameCont=true;
        timer = new Timer(initialDelay, this); //Create a new timer with initial delay
        timer.start();
    }


    public void gameSetup() {
        //Initialising snake starting position
        spawnApple();
        for (int i = 0; i < snakeBodyInitial; i++) {
            xPos[i] = 50 - i * snakeBodySize;
            yPos[i] = 50;
        }
    }


    public void resetGame() {
        //Reset game continuity logic
        gameStart = true;
        gameCont = true;
        snakeBodyInitial = 3; //Reset snake length
        score = 0; // Reset score
        //Reset snake direction
        moveLeft = false;
        moveRight = true;
        moveUp = false;
        moveDown = false;
        gameSetup();


        if (timer != null) {
            timer.stop(); // Stop the previous timer
        }
        timer = new Timer(initialDelay, this); // Create a new timer with initial delay
        timer.start(); // Start the new timer
       
        repaint();
    }


    public void gameOver(Graphics g) {
        //Game over screen text format
        String endScreenMsg = "Game over!";
        String gameStartMsg = "Press an arrow key to restart";
        String finalScore = "Score: " + score;
        Font font = new Font("Serif", Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);
        g.setFont(font);
        g.setColor(new Color(28, 42, 24));
        g.drawString(endScreenMsg, (boardWidth - metrics.stringWidth(endScreenMsg)) / 2, (boardLength - metrics.stringWidth(endScreenMsg)) / 2);
        g.drawString(gameStartMsg, (boardWidth - metrics.stringWidth(gameStartMsg)) / 2 - 5, (boardLength - metrics.stringWidth(gameStartMsg)) / 2 + 200);
        g.drawString(finalScore, (boardWidth - metrics.stringWidth(finalScore)) / 2, (boardLength - metrics.stringWidth(finalScore)) / 2 + 100);
    }


    public void startScreen(Graphics g) {
        //Starting screen text format
        String gameStartMsg = "Press an arrow key to start";
        Font font = new Font("Serif", Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);
        g.setFont(font);
        g.setColor(new Color(28, 42, 24));
        g.drawString(gameStartMsg, (boardWidth - metrics.stringWidth(gameStartMsg)) / 2 - 5, (boardLength - metrics.stringWidth(gameStartMsg)) / 2 + 200);
    }


    public void spawnApple() {
        Random rand = new Random();
        //Random x and y position for the apple
        foodXPos = rand.nextInt(boardWidth / snakeBodySize) * snakeBodySize;
        foodYPos = rand.nextInt(boardLength / snakeBodySize) * snakeBodySize;
    }


    public void eatsApple(){
        if (xPos[0] == foodXPos && yPos[0] == foodYPos) {
            snakeBodyInitial++;   //Increase the length of the snake
            score++; //Increase the score
            spawnApple(); //Spawn new food
        }
    }


    public void movement(){
        // Snake body movement
        for (int i = snakeBodyInitial - 1; i > 0; i--) {
            xPos[i] = xPos[i - 1];
            yPos[i] = yPos[i - 1];
        }


        // Snake moves in the direction entered by the user
        if (moveLeft) {
            xPos[0] -= snakeBodySize;
        }
        if (moveRight) {
            xPos[0] += snakeBodySize;
        }
        if (moveUp) {
            yPos[0] -= snakeBodySize;
        }
        if (moveDown) {
            yPos[0] += snakeBodySize;
        }
    }


    public void collision() {
        // Collision with itself
        for (int i = snakeBodyInitial; i > 0; i--) {
            if (xPos[0] == xPos[i] && yPos[0] == yPos[i]) {
                gameCont = false;
                break;
            }
        }


        // Collision with border
        if (xPos[0] >= boardWidth || xPos[0] < 0 || yPos[0] >= boardLength || yPos[0] < 0) {
            gameCont = false;
        }


        if(!gameCont) {
            timer.stop();
        }  
    }


    public void drawGame(Graphics g){
        //Board color
        g.setColor(new Color(124, 152, 0));
        g.fillRect(0, 0, boardWidth, boardLength);


        //Starting screen
        if (!gameStart) {
            startScreen(g);
            return;
        }
        //Game over screen
        if (!gameCont) {
            gameOver(g);
            return;
        }


        //Drawing the snake
        Random rand = new Random();
        for (int i = 0; i < snakeBodyInitial; i++) {
            if(i==0){
                g.setColor(new Color(0, 0, 210));
                g.fillRect(xPos[i], yPos[i], snakeBodySize, snakeBodySize);
            }
            else{
                g.setColor(new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200))); //Different color for each body part
                g.fillRect(xPos[i], yPos[i], snakeBodySize, snakeBodySize);
            }
        }


        //Drawing the apple
        g.setColor(Color.RED);
        g.fillOval(foodXPos, foodYPos, snakeBodySize, snakeBodySize);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }


    public class Adapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent k) {
            int key = k.getKeyCode();


            //Check if game is over and reset game if a movement key is pressed
            if (!gameCont && (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)) {
                resetGame();
                return;
            }


            //Movement handling
            if (key == KeyEvent.VK_LEFT) {  //Left key pressed
                if (!moveRight) {
                    moveLeft = true;
                    moveUp = false;
                    moveDown = false;
                    moveRight = false;
                }
            }
            if (key == KeyEvent.VK_RIGHT) {   //Right key pressed
                if (!moveLeft) {
                    moveRight = true;
                    moveLeft = false;
                    moveUp = false;
                    moveDown = false;
                }
            }
            if (key == KeyEvent.VK_UP) {  //Up key pressed
                if (!moveDown) {
                    moveUp = true;
                    moveLeft = false;
                    moveRight = false;
                    moveDown = false;
                }
            }
            if (key == KeyEvent.VK_DOWN) {  //Down key pressed
                if (!moveUp) {
                    moveDown = true;
                    moveLeft = false;
                    moveRight = false;
                    moveUp = false;
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameCont) {
            movement();
            eatsApple();
            collision();
        }
        repaint(); //Redraws the screen
    }
}
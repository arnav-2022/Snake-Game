import javax.swing.JFrame;
import java.awt.BorderLayout;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        GameBoard gameBoard = new GameBoard(); // Create an instance of your game board


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300); // Size of the game window
        frame.setLayout(new BorderLayout());
        frame.add(gameBoard, BorderLayout.CENTER); // Add game board to the frame
        frame.setVisible(true); // Make the frame visible
    }
}

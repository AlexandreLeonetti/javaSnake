import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener {
    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 1000;
    private final int GRID_SIZE = 20;
    private final int GRID_DIM = 800;
    private final int CELL_SIZE = GRID_DIM / GRID_SIZE;
    private LinkedList<Point> snake;
    private Point apple;
    private int direction = KeyEvent.VK_UP; // Initial direction
    private Random random = new Random();

    public SnakeGame() {
        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2)); // Start in the center
        generateApple();
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        new Timer(150, e -> gameLoop()).start();
    }

    private void generateApple() {
        do {
            apple = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
        } while (snake.contains(apple));
    }

    private void gameLoop() {
        Point head = new Point(snake.peekFirst());
        switch (direction) {
            case KeyEvent.VK_UP: head.y--; break;
            case KeyEvent.VK_DOWN: head.y++; break;
            case KeyEvent.VK_LEFT: head.x--; break;
            case KeyEvent.VK_RIGHT: head.x++; break;
        }

        if (head.x < 0 || head.y < 0 || head.x >= GRID_SIZE || head.y >= GRID_SIZE || snake.contains(head)) {
            // Reset snake
            snake.clear();
            snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
            generateApple();
        } else {
            snake.addFirst(head);
            if (head.equals(apple)) {
                generateApple();
            } else {
                snake.removeLast();
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        g.setColor(Color.RED);
        g.fillRect(apple.x * CELL_SIZE, apple.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int newDirection = e.getKeyCode();
        if ((newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
            (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) ||
            (newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
            (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT)) {
            direction = newDirection;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SnakeGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

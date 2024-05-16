import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel implements MouseListener {
    // Constants for game
    // number of ROWS by COLS cell constants
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final String TITLE = "Tic Tac Toe";

    // Constants for dimensions used for drawing
    // cell width and height
    public static final int CELL_SIZE = 100;
    // drawing canvas
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    // Noughts and Crosses are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    // SerialVersionUID field
    private static final long serialVersionUID = 1L;

    /* declare game object variables */
    // the game board
    private Board board;

    // GameState enumeration
    private enum GameState {
        Playing, Draw, Cross_won, Nought_won
    }

    private GameState currentState;

    // the current player
    private Player currentPlayer;
    // for displaying game status message
    private JLabel statusBar;

    /** Constructor to setup the UI and game components on the panel */
    public GameMain() {
        addMouseListener(this); 

        // Status bar to display winner message
        statusBar = new JLabel("           ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.gray);

        // layout of the panel is in border layout
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        // account for statusBar height in overall height
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

        board = new Board(); // Create a new instance of the game "Board" class
        initGame(); // call the method 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g); // Give the drawing to the Board object
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create the main window frame
                JFrame frame = new JFrame(TITLE);

                // Create the GameMain panel
                GameMain gameMainPanel = new GameMain();

                // Add the GameMain panel to the frame's content pane
                frame.getContentPane().add(gameMainPanel);

                // Set the default close operation of frame to exit_on_close
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Pack the frame to fit its components
                frame.pack();

                // Center frame on the screen
                frame.setLocationRelativeTo(null);

                // Make frame visible
                frame.setVisible(true);
            }
        });
    }

    /** Initialise the game-board contents and the current status of GameState and Player) */
    public void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                // all cells empty
                board.cells[row][col].content = Player.Empty;
            }
        }
        currentState = GameState.Playing;
        currentPlayer = Player.Cross;
    }

    /**
     * After each turn check to see if the current player hasWon by putting their symbol in that position,
     * If they have the GameState is set to won for that player
     * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING.
     */
    public void updateGame(Player thePlayer, int row, int col) {
        // check for win after play
        if (board.hasWon(thePlayer, row, col)) {
            if (thePlayer == Player.Cross) {
                currentState = GameState.Cross_won;
                statusBar.setText("Cross won!");
            } else {
                currentState = GameState.Nought_won;
                statusBar.setText("Nought won!");
            }
        } else if (board.isDraw()) {
            currentState = GameState.Draw;
            statusBar.setText("Draw!");
        }
        // otherwise no change to current state of playing
    }

    /**
     * Event handler for the mouse click on the JPanel.
     */
    public void mouseClicked(MouseEvent e) {
        // get the coordinates of where the click event happened
        int mouseX = e.getX();
        int mouseY = e.getY();
        // Get the row and column clicked
        int rowSelected = mouseY / CELL_SIZE;
        int colSelected = mouseX / CELL_SIZE;
        if (currentState == GameState.Playing) {
            if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
                // move
                board.cells[rowSelected][colSelected].content = currentPlayer;
                // update currentState
                updateGame(currentPlayer, rowSelected, colSelected);
                // Switch player
                currentPlayer = (currentPlayer == Player.Cross) ? Player.Nought : Player.Cross;
            }
        } else {
            // game over and restart
            initGame();
        }
        repaint(); // redraw the graphics on the UI
    }

    // Unused event listeners
    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}

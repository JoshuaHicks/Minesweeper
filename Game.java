import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.lang.Math;

public class Game extends JFrame implements ActionListener
{
	public static Game frame; // Main game frame

	public final int BOARD_WIDTH = 10;
	public JPanel gameBoard;
	public Tile[][] boardTiles;

	public final int MINE = -55;
	public final int NOT_MINE = -50;
	public final int MODE = 0;

	public double rand;
	public Boolean gameOver = false;

	public Game(String title)
	{
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout() );
		this.setTitle(title);

		gameBoard = new JPanel();
		gameBoard.setLayout(new GridLayout(BOARD_WIDTH, BOARD_WIDTH) );

		boardTiles = new Tile[BOARD_WIDTH][BOARD_WIDTH];

		pane.add(gameBoard);

		LabelListener listener = new LabelListener();

		for (int i=0; i<boardTiles.length; i++) {
			for (int j=0; j<boardTiles[i].length; j++) {
				rand = Math.random() * 10;
				if (rand <= 1) {
					boardTiles[i][j] = new Tile(MINE, i, j);

				} else {
					boardTiles[i][j] = new Tile(NOT_MINE, i, j);
				}
				boardTiles[i][j].setBorder(new LineBorder(Color.BLACK) );
				boardTiles[i][j].addMouseListener(listener);
				gameBoard.add(boardTiles[i][j]);
			}
		}

		// Setting the text on each tile
		for (int i=0; i<boardTiles.length; i++) {
			for (int j=0; j<boardTiles[i].length; j++) {
				boardTiles[i][j].setOpaque(true);
	            boardTiles[i][j].setHorizontalAlignment(JLabel.CENTER);

	            Font numberFont = new Font("Serif", Font.BOLD, 20);
	            Font mineFont = new Font("Serif", Font.PLAIN, 12);

	            if (boardTiles[i][j].getMineValue() ) {
	            	boardTiles[i][j].setFont(mineFont);
	            	boardTiles[i][j].setTileText("MINE");
	            	boardTiles[i][j].setNoMines(false);
	            }
	            else {
	            	int numMines = countMines(i, j);
	            	if (numMines != 0) {
	            		boardTiles[i][j].setFont(numberFont);
	            		boardTiles[i][j].setTileText("" + numMines);
	            		boardTiles[i][j].setNoMines(false);
	            		if (numMines == 1)
	            			boardTiles[i][j].setForeground(Color.BLUE);
	            		else if (numMines == 2)
	            			boardTiles[i][j].setForeground(Color.GREEN);
	            		else if (numMines == 3)
	            			boardTiles[i][j].setForeground(Color.RED);
	            		else if (numMines == 4)
	            			boardTiles[i][j].setForeground(new Color(151, 66, 208));
	            		else if (numMines == 5)
	            			boardTiles[i][j].setForeground(new Color(141, 32, 10));
	            		else if (numMines == 6)
	            			boardTiles[i][j].setForeground(new Color(113, 239, 239));
	            		else if (numMines == 7)
	            			boardTiles[i][j].setForeground(Color.BLACK);
	            		else if (numMines == 8)
	            			boardTiles[i][j].setForeground(Color.GRAY);
	            	}
	            }
			}
		}
	}

	public int countMines(int row, int col)
    {
    	int retVal = 0;
     	for (int i=-1+row; i<=1+row; i++) {
       		for (int j=-1+col; j<=1+col; j++) {
       			if (i >= 0 && i < boardTiles.length && j >= 0 && j < boardTiles.length)
       				if (boardTiles[i][j].getMineValue() )
       					retVal++;
       		}
       	}
       	return retVal;
    }

	public static void main(String[] args)
	{
		frame = new Game("Minesweeper");
		frame.setSize(800,800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object input = e.getSource();
	}

	private class LabelListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent event) {
            Object source = event.getSource();
            if(source instanceof Tile) {
				Tile tilePressed = (Tile) source;
				tilePressed.setOnScreen(true);
				if (tilePressed.getMineValue() ) {
					ImageIcon mine;
					if (MODE == 10)
						mine = new ImageIcon("thom.png");
					else
						mine = new ImageIcon("mine.png");
					tilePressed.setIcon(mine);
				} else
					tilePressed.setText(tilePressed.getTileText() );
				tilePressed.setBackground(Color.LIGHT_GRAY);
				if (tilePressed.getMineValue() )
					gameOver();
				else if (tilePressed.hasNoMines() ) {
					showAllNeighbours(tilePressed.getRow(), tilePressed.getCol() );
				}
            }
            checkIfWon();
        }

        public void showAllNeighbours(int row, int col)
        {
        	for (int i=-1+row; i<=1+row; i++) {
        		for (int j=-1+col; j<=1+col; j++) {
        			if (i >= 0 && i < boardTiles.length && j >= 0 && j < boardTiles.length) {
						boardTiles[i][j].setBackground(Color.LIGHT_GRAY);
						boardTiles[i][j].setText(boardTiles[i][j].getTileText() );
						if (!boardTiles[i][j].getOnScreen() ) {
							boardTiles[i][j].setOnScreen(true);
							if (boardTiles[i][j].hasNoMines())
								showAllNeighbours(i, j);
						}
        			}
        		}
        	}
        }

        public void checkIfWon()
        {
        	if (!gameOver) {
	        	Boolean hasWon = true;
	        	for (int i=0; i<boardTiles.length; i++) {
	        		for (int j=0; j<boardTiles[i].length; j++) {
	        			if ((boardTiles[i][j].getOnScreen() == false) && (boardTiles[i][j].getMineValue() == false))
	        				hasWon = false;
	        		}
	        	}
	        	if (hasWon)
	        		infoBox("YOU WIN", "Congratulations!");
        	}
        }

        public void gameOver()
        {
        	for (int i=0; i<boardTiles.length; i++) {
        		for (int j=0; j<boardTiles[i].length; j++) {
        			boardTiles[i][j].setBackground(Color.LIGHT_GRAY);
        			if (boardTiles[i][j].getMineValue() ) {
        				ImageIcon mine;
        				if (MODE == 10)
							mine = new ImageIcon("thom.png");
						else
							mine = new ImageIcon("mine.png");
						boardTiles[i][j].setIcon(mine);
					} else
						boardTiles[i][j].setText(boardTiles[i][j].getTileText() );
					boardTiles[i][j].setOnScreen(true);
        		}
        	}
        	infoBox("GAME OVER!", "OOPS");
        	gameOver = true;
        }

        public void infoBox(String infoMessage, String titleBar)
    	{
        	JOptionPane.showMessageDialog(frame, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    	}

        @Override
        public void mouseEntered(MouseEvent arg0) {}

        @Override
        public void mouseExited(MouseEvent arg0) {}

        @Override
        public void mousePressed(MouseEvent arg0) {}

        @Override
        public void mouseReleased(MouseEvent arg0) {}
    }
}

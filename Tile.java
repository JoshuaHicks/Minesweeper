import java.lang.Boolean;
import java.awt.*;
import javax.swing.*;

// Object class for a mine
public class Tile extends JLabel
{
	Boolean isMine;
	int row, col;
	String tileText;
	Boolean noMines = true;
	Boolean onScreen = false;

	public Tile(int state, int row, int col)
	{
		if (state == -55)
			isMine = true;
		else
			isMine = false;
		this.row = row;
		this.col = col;
	}

	public Boolean getMineValue()
	{
		return this.isMine;
	}

	public int getRow()
	{
		return this.row;
	}

	public int getCol()
	{
		return this.col;
	}

	public void setTileText(String text)
	{
		this.tileText = text;
	}

	public String getTileText()
	{
		return this.tileText;
	}

	public Boolean hasNoMines()
	{
		return this.noMines;
	}

	public void setNoMines(Boolean val)
	{
		this.noMines = val;
	}

	public void setOnScreen(Boolean val)
	{
		this.onScreen = val;
	}

	public Boolean getOnScreen()
	{
		return this.onScreen;
	}
}
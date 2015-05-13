package DecoderData;

public class Position {
	private int row;
	private int col;
	
	public Position(int row, int col) {
		this.row = row;
		this.col= col;
	}
	
	public Position() {
		this(-1,-1);
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.col == ((Position)obj).col && this.row	 == ((Position)obj).row)
			return true;
		return false;
	}
}

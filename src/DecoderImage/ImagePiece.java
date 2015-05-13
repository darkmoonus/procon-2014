package DecoderImage;

import DecoderData.SharedVariables;

public class ImagePiece{
	private int code;
	
	public int getExpectedRow() {
		return code/SharedVariables.numOfColumn;
	}
	
	public void setExpectedRow(int expectedRow) {
		code = SharedVariables.numOfColumn*expectedRow + getExpectedColumn(); 
	}
	
	public int getExpectedColumn() {
		return code%SharedVariables.numOfColumn;
	}
	
	public void setExpectedColumn(int expectedColumn) {
		code = SharedVariables.numOfColumn*getExpectedRow() + expectedColumn;
	}
	
	public void setExpectedPosition(int row, int col) {
		code = row*SharedVariables.numOfColumn + col;
	}
	
	public boolean equals(ImagePiece o) {
		if(this.code == o.code) {
			return true;
		}
		return false;
	}
	
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return "[" + getExpectedRow() + ", "
				+ getExpectedColumn() + "]";
	}
}

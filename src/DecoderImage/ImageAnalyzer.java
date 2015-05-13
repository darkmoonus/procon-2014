package DecoderImage;

import java.util.ArrayList;

import DecoderData.Path;
import DecoderData.Position;

public class ImageAnalyzer {
	public static int getAccurancy(ImageMap image) {
		return image.getRestoredRate();
	}
	
	public static int calculateDistance(ImageMap image, int row, int col) {
		if(image.checkPiece(row, col))
			return 0;
		else {
			ImagePiece piece = image.getPiece(row, col);
			if(piece.getExpectedColumn() == col && piece.getExpectedRow() != row) {
				return Math.abs(row - piece.getExpectedRow());
			} else if(piece.getExpectedColumn() != col && piece.getExpectedRow() == row) {
				return Math.abs(col - piece.getExpectedColumn());
			} else if(piece.getExpectedColumn() != col && piece.getExpectedRow() != row) {
				return Math.abs(col - piece.getExpectedColumn()) + Math.abs(row - piece.getExpectedRow());
			}
		}
		return 0;
	}
	
	public static Position[] getInvalidPieces(ImageMap image) {
		ArrayList<Position> invalid = new ArrayList<>();
		for(int row = 0; row < image.getNumOfRow(); row++) {
			for(int col = 0; col < image.getNumOfColumn(); col++) {
				if(image.checkPiece(row, col))
					continue;
				else {
					ImagePiece piece = null;
					for(int i = 0; i < image.getNumOfRow(); i++) {
						for(int j = 0; j < image.getNumOfColumn(); j++) {
							piece = image.getPiece(i, j);
							if(row == piece.getExpectedRow() && col == piece.getExpectedColumn()) {
								invalid.add(new Position(i,j));
							}
						}
					}
				}
			}
		}
		
		return (Position[]) invalid.toArray(new Position[invalid.size()]);
	}
	
	
	public static boolean isDeadMap(ImageMap image) {
		Position[] invalidPieces = getInvalidPieces(image);
		for(int i = 0; i < invalidPieces.length; i++) {
			if(!isDeadPiece(image, invalidPieces[i].getRow(), invalidPieces[i].getCol())) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isDeadPiece(ImageMap image, int row, int col) {
		if(image.checkPiece(row, col)) {
			return false;
		} else {
			try {
				if(!image.checkPiece(row+1, col)) {
					return false;
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				//do nothing
			}
			try {
				if(!image.checkPiece(row-1, col)) {
					return false;
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				//do nothing
			}
			try {
				if(!image.checkPiece(row, col+1)) {
					return false;
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				//do nothing
			}
			try {
				if(!image.checkPiece(row, col-1)) {
					return false;
				}
			} catch(ArrayIndexOutOfBoundsException e) {
				//do nothing
			}
			
			return true;
		}
	}
	
	/*public static boolean isConnectedPiece(ImageMap image, int row, int col) {
		if(image.chẹckPiece(row, col)) {
			return false;
		}
		else {
			
		}
	}*/
	
	public static Position calculateCurrentSelection(Path path) {
		int selectedRow = path.getStartRow();
		int selectedCol = path.getStartCol();
		
		for(int i = 0; i < path.getSteps().size(); i++) {
			switch (path.getSteps().get(i)) {
			case DOWN:
				selectedRow++;
				break;
			case LEFT:
				selectedCol--;
				break;
			case RIGHT:
				selectedCol++;
				break;
			case UP:
				selectedRow--;
				break;
			default:
				break;
			
			}
		}
		return new Position(selectedRow, selectedCol);
	}
	
	public static Position findExpectedPiece(ImageMap image, int row, int col) {
		ImagePiece piece = null;
		for(int i = 0; i < image.getNumOfRow(); i++) {
			for(int j = 0; j < image.getNumOfColumn(); j++) {
				piece = image.getPiece(i, j);
				if(row == piece.getExpectedRow() && col == piece.getExpectedColumn())
					return new Position(i,j);
			}
		}
		return null;
	}
}

package DecoderImage;

import java.awt.Point;

import DecoderData.DIRECTION;
import DecoderData.Path;
import DecoderData.PathList;
import DecoderData.SharedVariables;

public class ImageMap implements Comparable<ImageMap>, Cloneable {
//	private int numOfRow;
//	private int numOfColumn;
	private ImagePiece map[][];
	private ImageMap baseMap;
	
	public ImageMap(ImageMap baseMap) {
		this.baseMap = baseMap;
//		this.numOfRow = baseMap.numOfRow;
//		this.numOfColumn = baseMap.numOfColumn;
		map = new ImagePiece[SharedVariables.numOfRow][SharedVariables.numOfColumn];
		for(int row = 0; row < SharedVariables.numOfRow; row++) 
			for(int col = 0 ; col < SharedVariables.numOfColumn; col++) {
				map[row][col] = null; 
			}			
	}
	
	public ImageMap(ImageMap baseMap, Path path) {
		this.baseMap = baseMap;
//		this.numOfRow = baseMap.numOfRow;
//		this.numOfColumn = baseMap.numOfColumn;
		map = new ImagePiece[SharedVariables.numOfRow][SharedVariables.numOfColumn];
		for(int row = 0; row < SharedVariables.numOfRow; row++) 
			for(int col = 0 ; col < SharedVariables.numOfColumn; col++) {
				map[row][col] = null; 
			}
		
		int selectedRow = path.getStartRow();
		int selectedCol = path.getStartCol();
		for(int i = 0; i < path.size(); i++) {
			DIRECTION direction = path.get(i);
			swap(selectedRow, selectedCol, direction);
			switch(direction) {
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
	}
	
	public ImageMap(ImageMap baseMap, PathList paths) {
		this.baseMap = baseMap;
		map = new ImagePiece[SharedVariables.numOfRow][SharedVariables.numOfColumn];
		for(int row = 0; row < SharedVariables.numOfRow; row++) 
			for(int col = 0 ; col < SharedVariables.numOfColumn; col++) {
				map[row][col] = null; 
			}
		
		for(int index = 0 ; index < paths.size(); index++) {
			int selectedRow = paths.get(index).getStartRow();
			int selectedCol = paths.get(index).getStartCol();
			for(int i = 0; i < paths.get(index).size(); i++) {
				DIRECTION direction = paths.get(index).get(i);
				swap(selectedRow, selectedCol, direction);
				switch(direction) {
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
		}
	}
	
	public ImageMap(int numOfRow, int numOfColumn) {
		if(validate(numOfRow, numOfColumn)) {
			SharedVariables.numOfRow = numOfRow;
			SharedVariables.numOfColumn = numOfColumn;
			baseMap = null;
			map = new ImagePiece[numOfRow][numOfColumn];
			for(int row = 0; row < numOfRow; row++) 
				for(int col = 0 ; col < numOfColumn; col++) {
					map[row][col] = new ImagePiece(); 
				}
		} else throw new IllegalArgumentException();
	}
	
	public ImageMap(Point[][] matrix) {
		this(matrix.length, matrix[0].length);
		for(int i = 0 ; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				this.getPiece(i, j).setExpectedPosition(matrix[i][j].x, matrix[i][j].y);
			}
		}
	}
	
	public boolean checkPiece(int row, int col) {
		if(getPiece(row, col).getExpectedRow() == row &&
				getPiece(row, col).getExpectedColumn() == col)
			return true;
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		ImageMap c = new ImageMap(SharedVariables.numOfRow, SharedVariables.numOfColumn);
		for(int i = 0; i < SharedVariables.numOfRow; i++) {
			for(int j = 0; j < SharedVariables.numOfColumn; j++) {
				c.getPiece(i, j).setExpectedPosition(getPiece(i, j).getExpectedRow(), getPiece(i, j).getExpectedColumn());
			}
		}
		return c;
	}
	
	@Override
	public int compareTo(ImageMap o) {
		return this.getRestoredRate() - o.getRestoredRate();
	}
	
	public boolean equals(ImageMap o) {
		for(int i = 0 ; i < SharedVariables.numOfRow; i++) 
			for(int j = 0; j < SharedVariables.numOfColumn; j++) {
				if(!getPiece(i, j).equals(o.getPiece(i, j)))
					return false; 
			}
		return true;
	}
	
	public int[] extractCode() {
		int[] result = new int[SharedVariables.numOfColumn*SharedVariables.numOfRow];
		int index = 0;
		for(int row = 0 ; row < SharedVariables.numOfRow; row++) {
			for(int col = 0; col < SharedVariables.numOfColumn; col++) {
				ImagePiece piece = getPiece(row, col);
				result[index] = piece.getCode();
				index++;
			}
		}
		return result;
	}

	public int getRestoredRate() {
		int restoredRate = 0;
		for(int i = 0; i < SharedVariables.numOfRow; i++)
			for(int j = 0; j < SharedVariables.numOfColumn; j++) {
				if(getPiece(i, j).getExpectedRow() == i && getPiece(i, j).getExpectedColumn() == j) {
					restoredRate++;
				}
			}
		return restoredRate;
	}

	public int getNumOfColumn() {
		return SharedVariables.numOfColumn;
	}
	
	public int getNumOfRow() {
		return SharedVariables.numOfRow;
	}

	public ImagePiece getPiece(int row, int col) {
		/*if(map[row][col] != null) {
			return map[row][col];
		} else {
			return baseMap.getPiece(row, col);
		}*/
		
		ImagePiece result = map[row][col];
		ImageMap currImage = this;
		while(result == null) {
			currImage = currImage.baseMap;
			result = currImage.map[row][col];
		}
		return result;
	}
	
	public void show() {
		for(int  i = 0; i < SharedVariables.numOfRow; i++) {
			for(int j = 0; j < SharedVariables.numOfColumn; j++) {
				System.out.print(getPiece(i, j) + "\t");
			}
			System.out.println();
		}
	}
	
	public void swap(int row, int col, DIRECTION direction) {
		switch(direction) {
		case DOWN:
			swap(row, col, row+1, col);
			break;
		case LEFT:
			swap(row, col, row, col-1);
			break;
		case RIGHT:
			swap(row, col, row, col+1);
			break;
		case UP:
			swap(row, col, row-1, col);
			break;
		default:
			break;
		}
	}
	
	private void swap(int fromRow, int fromColumn, int toRow, int toColumn) {
			ImagePiece temp = getPiece(fromRow, fromColumn);
			map[fromRow][fromColumn] = getPiece(toRow, toColumn);
			map[toRow][toColumn] = temp;
	}
	
	private boolean validate(int row, int col) {
		if(row > 0 && col > 0 && row <= SharedVariables.MAX_DIMENSION && col <= SharedVariables.MAX_DIMENSION) {
			return true;
		}
		return false;
	}
}

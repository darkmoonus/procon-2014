package Decoder;

import AStarTree.AStarTree;
import DecoderData.DIRECTION;
import DecoderData.Path;
import DecoderImage.ImageMap;
import Tree.Tree;

public class PathFinder {
	
	public static Path move(int fromRow, int fromCol, int toRow, int toCol) {
		Path result = new Path();
		result.setStartCol(fromCol);
		result.setStartRow(fromRow);
		
		if(fromRow == toRow &&  fromCol != toCol) {
			while(fromCol < toCol) {
				result.getSteps().add(DIRECTION.RIGHT);
				fromCol++;
			}
			while(fromCol > toCol) {
				result.getSteps().add(DIRECTION.LEFT);
				fromCol--;
			}
		} else if(fromRow != toRow && fromCol == toCol) {
			while(fromRow < toRow) {
				result.getSteps().add(DIRECTION.DOWN);
				fromRow++;
			}
			while(fromRow > toRow) {
				result.getSteps().add(DIRECTION.UP);
				fromRow--;
			}
		} else if(fromRow != toRow && fromCol != toCol) {
			if(fromRow > toRow) {
				result.getSteps().addAll(move(fromRow, 0, toRow, 0).getSteps());
				result.getSteps().addAll(move(0, fromCol, 0, toCol).getSteps());
			} else {
				result.getSteps().addAll(move(0, fromCol, 0, toCol).getSteps());
				result.getSteps().addAll(move(fromRow, 0, toRow, 0).getSteps());
			}
		}
		return result;
	}
	
	public static Path shortestPath(ImageMap image, int row, int col) {
		int expectedRow = image.getPiece(row, col).getExpectedRow();
		int expectedCol	 = image.getPiece(row, col).getExpectedColumn();
		return move(row,col,expectedRow,expectedCol);
	}
	
	public static Path simpleHeuristic(ImageMap image, int row, int col, boolean ignoreCorrectPiece, int range) {
		Path result = new Path();
		Tree tree = new Tree(image, row, col);
		tree.build_simpleHeuristic(ignoreCorrectPiece, range);
		result = tree.exportPath();
		return result;
	}
	
	public static Path noHeuristic(ImageMap image, int row, int col, boolean ignoreCorrectPiece, int range) {
		Path result = new Path();
		Tree tree = new Tree(image, row, col);
		tree.build_noHeuristic(ignoreCorrectPiece, range);
		result = tree.exportPath();
		return result;
	}

	public static Path IDAStar(ImageMap image, int row, int col, boolean ignoreCorrectPiece, int range) {
		Path result = new Path();
		AStarTree tree = new AStarTree(image, row, col);
		tree.build_AStar(ignoreCorrectPiece, range);
		result = tree.exportPath();
		return result;
	}
//	
//	public static Path random(ImageMap image, int row, int col, boolean ignoreCorrectPiece) {
//		Position currSelect = new Position(row, col);
//		int numOfCorrectPieces = image.getAccurancy();
//		Path path = new Path(row, col);
//		
//		
//		
//	}
}

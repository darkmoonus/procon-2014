package Decoder;

import Tree.TreeNode;
import DecoderData.DIRECTION;
import DecoderData.SharedVariables;
import DecoderImage.ImageMap;
import DecoderImage.ImagePiece;


public class HeuristicFunction {
	
	public static HeuristicResult 	noHeuristic() {
		HeuristicResult result = new HeuristicResult();
		result.setNode(TreeNode.getCheckpoints().get(0));
		result.setDirection(selectDirection_RoundRobin(result.getNode()));
		return result;
	}

	public static HeuristicResult 	simple() {
		HeuristicResult result = new HeuristicResult();
		int heuristicValue = Integer.MAX_VALUE;
		for(int i = 0; i < TreeNode.getCheckpoints().size(); i++) {
			int newValue = calculateHeuristicValue(TreeNode.getCheckpoints().get(i).getImage());
			if(newValue < heuristicValue) {
				heuristicValue = newValue;
//				System.out.println(newValue);
				result.setNode(TreeNode.getCheckpoints().get(i));
			}
		}
		result.setDirection(selectDirection_RoundRobin(result.getNode()));
		return result;
	}

	public static int calculateHeuristicValue(ImageMap image) {
		int value = 0;
		ImagePiece piece = null;
		for(int row = 0; row < image.getNumOfRow(); row++) {
			for(int  col = 0; col < image.getNumOfColumn(); col++) {
				piece = image.getPiece(row, col);
				value += Math.abs(row - piece.getExpectedRow()) * Math.abs(row - piece.getExpectedRow()) * Math.abs(row - piece.getExpectedRow());
				value += Math.abs(col - piece.getExpectedColumn()) * Math.abs(col - piece.getExpectedColumn()) * Math.abs(col - piece.getExpectedColumn());
			}
		}
		value = value - (int)(0.15*value);
		
		int penalty = 0;
		
		for(int row = 0; row < SharedVariables.numOfRow; row++) {
			for(int col = 0; col < SharedVariables.numOfColumn-1; col++) {
				if(image.getPiece(row, col).getExpectedRow() == row &&
					image.getPiece(row, col).getExpectedColumn() == col+1 &&
					image.getPiece(row, col+1).getExpectedRow() == row &&
					image.getPiece(row, col+1).getExpectedColumn() == col) {
						penalty += 2;
				}
			}
		}
		
		for(int col = 0; col < SharedVariables.numOfColumn; col++) {
			for(int row = 0; row < SharedVariables.numOfRow-1; row++) {
				if(image.getPiece(row, col).getExpectedRow() == col &&
					image.getPiece(row, col).getExpectedColumn() == row+1 &&
					image.getPiece(row+1, col).getExpectedRow() == col &&
					image.getPiece(row+1, col).getExpectedColumn() == row) {
						penalty += 2;
				}
			}
		}
		
		value += penalty;
		return value;
	}
	
	private static DIRECTION selectDirection_RoundRobin(TreeNode node) {
		if(DIRECTION.UP != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.UP)
				&& node.getSelectedRow() > 0) {
			return DIRECTION.UP;
		} else if(DIRECTION.LEFT != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.LEFT) 
				&& node.getSelectedCol() > 0) {
			return DIRECTION.LEFT;
		} else if(DIRECTION.RIGHT != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.RIGHT)
				&& node.getSelectedCol() < SharedVariables.numOfColumn-1) {
			return DIRECTION.RIGHT;
		} else if(DIRECTION.DOWN != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.DOWN)
				&& node.getSelectedRow() < SharedVariables.numOfRow-1) {
			return DIRECTION.DOWN;
		}
		return DIRECTION.NONE;
	}

}

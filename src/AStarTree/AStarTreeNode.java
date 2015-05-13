package AStarTree;

import DecoderData.DIRECTION;
import DecoderData.Path;
import DecoderData.SharedVariables;
import DecoderImage.ImageMap;

public class AStarTreeNode {
	private static AStarTreeNodeContainer allNodes = new AStarTreeNodeContainer(SharedVariables.numOfColumn*SharedVariables.numOfRow); //+1
	
	public static AStarTreeNode contain(ImageMap image) {
		return allNodes.contain(image);
	}   
	public static AStarTreeNodeContainer getAllNodes() {
		return allNodes;
	}
	
	private ImageMap image;
	private AStarNodeSwapDirection directions;
	private DIRECTION parentDirection;
	private int heuristic;
	private int selectedRow;
	private int selectedCol;
	
	public AStarTreeNode(ImageMap image, int selectedRow, int selectedCol) {
		this.heuristic = Integer.MAX_VALUE;
		this.image = image;
		directions = new AStarNodeSwapDirection();
		this.setParentDirection(DIRECTION.NONE);
		this.selectedCol = selectedCol;
		this.selectedRow = selectedRow;
		if(selectedCol == 0) {
			disableDirection(DIRECTION.LEFT);
		} else if(selectedCol == SharedVariables.numOfColumn-1) {
			disableDirection(DIRECTION.RIGHT);
		}
		if(selectedRow == 0) {
			disableDirection(DIRECTION.UP);
		} else if(selectedRow == SharedVariables.numOfRow-1) {
			disableDirection(DIRECTION.DOWN);
		}
		AStarTreeNode.allNodes.add(this);
	}
	
	public AStarTreeNode() {	
		this.heuristic = Integer.MAX_VALUE;
	}
	
	public void create(DIRECTION direction, boolean ignoreCorrectPiece) {
		if(isDirectionInactive(direction))
			return;
		ImageMap newImage = new ImageMap(image);
		AStarTreeNode containedNode = null;
		switch(direction) {
		
		case DOWN:
			if(ignoreCorrectPiece == true && image.checkPiece(selectedRow+1, selectedCol)) {
				disableDirection(direction);
				break;
			}
			newImage.swap(selectedRow, selectedCol, direction);
			containedNode = AStarTreeNode.contain(newImage);
			if(containedNode != null ) {
				if(getLevel()+1 >= containedNode.getLevel()) {
					directions.disable(direction);
					
				} else if(getLevel()+1 < containedNode.getLevel()) {
					containedNode.get(containedNode.parentDirection).disableDirection(containedNode.parentDirection.invert());
					containedNode.disableDirection(containedNode.parentDirection);
					directions.set(direction, containedNode);
					directions.get(direction).enableDirection(direction.invert()); //bug
					directions.get(direction).setParentDirection(direction.invert(), this);
					SharedVariables.merged = direction;
				}
			} else {
				directions.set(direction, new AStarTreeNode(newImage, selectedRow+1, selectedCol));
				directions.get(direction).enableDirection(direction.invert());
				directions.get(direction).setParentDirection(direction.invert(), this);
			}
			break;

		case LEFT:
			if(ignoreCorrectPiece == true && image.checkPiece(selectedRow, selectedCol-1)) {
				disableDirection(direction);
				break;
			}
			newImage.swap(selectedRow, selectedCol, direction);
			containedNode = AStarTreeNode.contain(newImage);
			if(containedNode != null ) {
				if(getLevel()+1 >= containedNode.getLevel()) {
					directions.disable(direction);
					
				} else if(getLevel()+1 < containedNode.getLevel()) {
					containedNode.get(containedNode.parentDirection).disableDirection(containedNode.parentDirection.invert());
					containedNode.disableDirection(containedNode.parentDirection);
					directions.set(direction, containedNode);
					directions.get(direction).enableDirection(direction.invert()); //bug
					directions.get(direction).setParentDirection(direction.invert(), this);
					SharedVariables.merged = direction;
				}
			} else {
				directions.set(direction, new AStarTreeNode(newImage, selectedRow, selectedCol-1));
				directions.get(direction).enableDirection(direction.invert());
				directions.get(direction).setParentDirection(direction.invert(), this);
			}
			break;
			
		case RIGHT:
			if(ignoreCorrectPiece == true && image.checkPiece(selectedRow, selectedCol+1)) {
				disableDirection(direction);
				break;
			}
			newImage.swap(selectedRow, selectedCol, direction);
			containedNode = AStarTreeNode.contain(newImage);
			if(containedNode != null ) {
				if(getLevel()+1 >= containedNode.getLevel()) {
					directions.disable(direction);
					
				} else if(getLevel()+1 < containedNode.getLevel()) {
					containedNode.get(containedNode.parentDirection).disableDirection(containedNode.parentDirection.invert());
					containedNode.disableDirection(containedNode.parentDirection);
					directions.set(direction, containedNode);
					directions.get(direction).enableDirection(direction.invert()); //bug
					directions.get(direction).setParentDirection(direction.invert(), this);
					SharedVariables.merged = direction;
				}
			} else {
				directions.set(direction, new AStarTreeNode(newImage, selectedRow, selectedCol+1));
				directions.get(direction).enableDirection(direction.invert());
				directions.get(direction).setParentDirection(direction.invert(), this);
			}
			break;
			
		case UP:
			if(ignoreCorrectPiece == true && image.checkPiece(selectedRow-1, selectedCol)) {
				disableDirection(direction);
				break;
			}
			newImage.swap(selectedRow, selectedCol, direction);
			containedNode = AStarTreeNode.contain(newImage);
			if(containedNode != null ) {
				if(getLevel()+1 >= containedNode.getLevel()) {
					directions.disable(direction);
					
				} else if(getLevel()+1 < containedNode.getLevel()) {
					containedNode.get(containedNode.parentDirection).disableDirection(containedNode.parentDirection.invert());
					containedNode.disableDirection(containedNode.parentDirection);
					directions.set(direction, containedNode);
					directions.get(direction).enableDirection(direction.invert()); //bug
					directions.get(direction).setParentDirection(direction.invert(), this);
					SharedVariables.merged = direction;
				}
			} else {
				directions.set(direction, new AStarTreeNode(newImage, selectedRow-1, selectedCol));
				directions.get(direction).enableDirection(direction.invert());
				directions.get(direction).setParentDirection(direction.invert(), this);
			}
			break;			
		default:
			break;
		}
	}
		
	public void disableDirection(DIRECTION direction) {
		directions.disable(direction);
	}
	
	public void enableDirection(DIRECTION direction) {
		directions.enable(direction);
	}
	
	public Path exportPath() {
		Path path = new Path();
		DIRECTION parentDir = this.parentDirection;
		AStarTreeNode parent =  this;
		
		while(parentDir != DIRECTION.NONE) {
			path.getSteps().add(parentDir);
			parent = parent.get(parentDir);
			parentDir = parent.getParentDirection();
		}
		
		Path result = new Path();
		result.setStartRow(selectedRow);
		result.setStartCol(selectedCol);
		for(int i = path.getSteps().size()-1; i >=0 ; i--)
			result.getSteps().add(path.getSteps().get(i).invert());
		return result;
	}

	public AStarTreeNode get(DIRECTION direction) {
		return directions.get(direction);
	}
	
	public boolean isDirectionInactive(DIRECTION direction) {
		return directions.isInactive(direction);
	}
 
	public int getAccurancy() {
		return image.getRestoredRate();
	}

	public ImageMap getImage() {
		return image;
	}
	
	public int[] getImageCode() {
		return getImage().extractCode();
	}

	public int getLevel() {
		int level = 0;
		AStarTreeNode currNode = this;
		while(currNode.getParentDirection() != DIRECTION.NONE) {
			level++;
			currNode = currNode.get(currNode.parentDirection);
			
		}
		return level;
	}

	public DIRECTION getParentDirection() {
		return parentDirection;
	} 
	
	public void setImage(ImageMap image) {
		this.image = image;
	}
	
	public void setParentDirection(DIRECTION parent) {
		this.parentDirection = parent;
	}
	
	public void setParentDirection(DIRECTION parent, AStarTreeNode node) {
		this.parentDirection = parent;
		this.directions.set(parent, node);
	}
	
	public int getSelectedRow() {
		return selectedRow;
	}
	public int getSelectedCol() {
		return selectedCol;
	}
	public int getHeuristic() {
		return heuristic;
	}
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
}

class AStarNodeSwapDirection {
	private AStarTreeNode up;
	private AStarTreeNode left;
	private AStarTreeNode down;
	private AStarTreeNode right;
	private boolean inactiveUp;
	private boolean inactiveDown;
	private boolean inactiveLeft;
	private boolean inactiveRight;
	
	
	public AStarNodeSwapDirection() {
		inactiveUp = false;
		inactiveRight = false;
		inactiveLeft = false;
		inactiveDown = false;
		up = null;
		down = null;
		left = null;
		right = null;;
	}
	
	public void disable(DIRECTION direction) {
		switch (direction) {
		case DOWN:
			down = null;
			inactiveDown = true;
			break;
		case LEFT:
			left = null;
			inactiveLeft = true;
			break;
		case RIGHT:
			right = null;
			inactiveRight = true;
			break;
		case UP:
			up = null;
			inactiveUp = true;
			break;
		default:
			break;
		}
	}
	
	public void enable(DIRECTION direction) {
		switch (direction) {
		case DOWN:
			inactiveDown = false;
			break;
		case LEFT:
			inactiveLeft = false;
			break;
		case RIGHT:
			inactiveRight = false;
			break;
		case UP:
			inactiveUp = false;
			break;
		default:
			break;
		}
	}
	
	public AStarTreeNode get(DIRECTION direction) {
		switch(direction) {
		case DOWN:
			return down;
		case LEFT:
			return left;
		case RIGHT:
			return right;
		case UP:
			return up;
		default:
			return null;
		}
	}
	
	public boolean isInactive(DIRECTION direction) {
		switch (direction) {
		case DOWN:
			return inactiveDown;
		case LEFT:
			return inactiveLeft;
		case RIGHT:
			return inactiveRight;
		case UP:
			return inactiveUp;
		default:
			return false;
		}
	}
	
	public void set(DIRECTION direction, AStarTreeNode node) {
		if(isInactive(direction)) {
			return;
		} else {
			switch (direction) {
			case DOWN:
				down = node;
				break;
			case LEFT:
				left = node;
				break;
			case RIGHT:
				right = node;
				break;
			case UP:
				up = node;
				break;
			default:
				break;
			}
		}
	}
}
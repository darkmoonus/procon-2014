package AStarTree;

import Decoder.HeuristicFunction;
import DecoderData.DIRECTION;
import DecoderData.Path;
import DecoderData.SharedVariables;
import DecoderImage.ImageMap;

public class AStarTree {
	private AStarTreeNode root;
	private AStarTreeNode bestNode;
	private AStarTreeNode heuristicNode;
//	@SuppressWarnings("unused")
//	private ImageMap rootImage;
	private int selectedRow;
	private int selectedCol;
	
	
	public AStarTree(ImageMap rootImage, int selectedRow, int selectedCol) {
		this.selectedRow = selectedRow;
		this.selectedCol = selectedCol;
		AStarTreeNode.getAllNodes().clear();
		root = new AStarTreeNode(rootImage,selectedRow, selectedCol);
		bestNode = root;
		heuristicNode = root;
//		this.rootImage = rootImage;
	}
	
	public void build_AStar(boolean ignoreCorrectPiece, int range) {
		System.out.println("start build");
		AStarTreeNode node = root;
		buildNode(node, ignoreCorrectPiece);
		root.setHeuristic(HeuristicFunction.calculateHeuristicValue(root.getImage()));
		DIRECTION direction = selectDirection(node);
//		System.out.println("other tree");
		while(direction != DIRECTION.NONE) {
//			System.out.println("outer loop");
			node = root;
			direction = selectDirection(node);
			int branchCount = 1;
			while(true) {
				if(direction != DIRECTION.NONE) {
					node = node.get(direction);
					if(heuristicNode.getHeuristic() > node.getHeuristic()) {
						heuristicNode = node;
					}
					
					node.get(node.getParentDirection()).disableDirection(node.getParentDirection().invert());
					
					if(node.getLevel() >= 50) {
						node = node.get(node.getParentDirection());
						direction = selectDirection(node);
					} else {
						buildNode(node, ignoreCorrectPiece);
						if(HeuristicFunction.calculateHeuristicValue(heuristicNode.getImage()) == 0)
							return;
						direction = selectDirection(node);
					}
				} else {
					node = node.get(node.getParentDirection());
					if(node == null) 
						break;
		
					direction = selectDirection(node);
				}
				
				branchCount+=3;
//				System.out.println(branchCount);
				if(branchCount == range)
					break;
			}
		}
		System.out.println("end build");
	}
	
	public AStarTreeNode getbestNode() {
		return bestNode;
	}
	
	private DIRECTION selectDirection(AStarTreeNode node) {
		DIRECTION direction = DIRECTION.NONE;
		
		int minHeuristic = Integer.MAX_VALUE;
		
		if(DIRECTION.UP != node.getParentDirection() && node.get(DIRECTION.UP) != null) {
			if(minHeuristic > node.get(DIRECTION.UP).getHeuristic()) {
				minHeuristic = node.get(DIRECTION.UP).getHeuristic();
				direction = DIRECTION.UP;
			}
		}
		if(DIRECTION.DOWN != node.getParentDirection() && node.get(DIRECTION.DOWN) != null) {
			if(minHeuristic > node.get(DIRECTION.DOWN).getHeuristic()) {
				minHeuristic = node.get(DIRECTION.DOWN).getHeuristic();
				direction = DIRECTION.DOWN;
			}
		}
		if(DIRECTION.LEFT != node.getParentDirection() && node.get(DIRECTION.LEFT) != null) {
			if(minHeuristic > node.get(DIRECTION.LEFT).getHeuristic()) {
				minHeuristic = node.get(DIRECTION.LEFT).getHeuristic();
				direction = DIRECTION.LEFT;
			}
		}
		if(DIRECTION.RIGHT != node.getParentDirection() && node.get(DIRECTION.RIGHT) != null) {
			if(minHeuristic > node.get(DIRECTION.RIGHT).getHeuristic()) {
				minHeuristic = node.get(DIRECTION.RIGHT).getHeuristic();
				direction = DIRECTION.RIGHT;
			}
		}
		
		return direction;
	}
	
	public void buildNode(AStarTreeNode node, boolean ignoreCorrectPiece) {
		if(node == null)
			return;
		
		AStarTreeNode createdNode = null;
		if(DIRECTION.UP != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.UP) && node.getSelectedRow() > 0) {
			node.create(DIRECTION.UP, ignoreCorrectPiece);
			if(SharedVariables.merged != DIRECTION.NONE) {
				node.disableDirection(SharedVariables.merged);
				SharedVariables.merged = DIRECTION.NONE;
			}
			createdNode = node.get(DIRECTION.UP);
			if(createdNode != null) {
				createdNode.setHeuristic(HeuristicFunction.calculateHeuristicValue(createdNode.getImage()));
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		if(DIRECTION.DOWN != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.DOWN) && node.getSelectedRow() < SharedVariables.numOfRow-1) {
			node.create(DIRECTION.DOWN, ignoreCorrectPiece);
			if(SharedVariables.merged != DIRECTION.NONE) {
				node.disableDirection(SharedVariables.merged);
				SharedVariables.merged = DIRECTION.NONE;
			}
			createdNode = node.get(DIRECTION.DOWN);
			if(createdNode != null) {
				createdNode.setHeuristic(HeuristicFunction.calculateHeuristicValue(createdNode.getImage()));
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		if(DIRECTION.LEFT != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.LEFT) && node.getSelectedCol() > 0) {
			node.create(DIRECTION.LEFT, ignoreCorrectPiece);
			if(SharedVariables.merged != DIRECTION.NONE) {
				node.disableDirection(SharedVariables.merged);
				SharedVariables.merged = DIRECTION.NONE;
			}
			createdNode = node.get(DIRECTION.LEFT);
			if(createdNode != null) {
				createdNode.setHeuristic(HeuristicFunction.calculateHeuristicValue(createdNode.getImage()));
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		if(DIRECTION.RIGHT != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.RIGHT) && node.getSelectedCol() < SharedVariables.numOfColumn-1) {
			node.create(DIRECTION.RIGHT, ignoreCorrectPiece);
			if(SharedVariables.merged != DIRECTION.NONE) {
				node.disableDirection(SharedVariables.merged);
				SharedVariables.merged = DIRECTION.NONE;
			}
			createdNode = node.get(DIRECTION.RIGHT);
			if(createdNode != null) {
				createdNode.setHeuristic(HeuristicFunction.calculateHeuristicValue(createdNode.getImage()));
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
	}
	
	public Path exportPath() {
		Path path = bestNode.exportPath(); 
		path.setStartRow(selectedRow);
		path.setStartCol(selectedCol);
		return path;
	}
	
	public Path exportHeristicPath() {
		Path path = heuristicNode.exportPath(); 
		path.setStartRow(selectedRow);
		path.setStartCol(selectedCol);
		return path;
	}
		
}

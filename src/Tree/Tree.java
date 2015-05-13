package Tree;

import Decoder.HeuristicFunction;
import Decoder.HeuristicResult;
import DecoderData.DIRECTION;
import DecoderData.Path;
import DecoderData.SharedVariables;
import DecoderImage.ImageMap;

public class Tree {
	private TreeNode root;
	private TreeNode bestNode;
//	private ImageMap rootImage;
	private int selectedRow;
	private int selectedCol;
	
	
	public Tree(ImageMap rootImage, int selectedRow, int selectedCol) {
		this.selectedRow = selectedRow;
		this.selectedCol = selectedCol;
		TreeNode.getCheckpoints().clear();
		TreeNode.getAllNodes().clear();
		root = new TreeNode(rootImage,selectedRow, selectedCol);
		bestNode = root;
//		this.rootImage = rootImage;
	}
	
	public void build_simpleHeuristic(boolean ignoreCorrectPiece, int range) {
		HeuristicResult heuristic = null;
		for(int i = 0; 0 != TreeNode.getCheckpoints().size(); i++) {
			heuristic = HeuristicFunction.simple(); 
			buildNode(heuristic.getNode(), ignoreCorrectPiece);
 			
 			if(i == range) 
 				break;
		}
	}

	
	public void build_noHeuristic(boolean ignoreCorrectPiece, int range) {
		HeuristicResult heuristic = null;
		for(int i = 0; 0 != TreeNode.getCheckpoints().size(); i++) {
			heuristic = HeuristicFunction.noHeuristic(); 
			heuristic.getNode().create(heuristic.getDirection(), ignoreCorrectPiece);
 			
 			TreeNode createdNode = heuristic.getNode().get(heuristic.getDirection());
 			
 			if(createdNode != null && 
 					createdNode.getImage().checkPiece(createdNode.getSelectedRow(), createdNode.getSelectedCol()))
 				TreeNode.getCheckpoints().remove(createdNode);
 			
 			if(createdNode != null && 
					createdNode.getAccurancy() > bestNode.getAccurancy())
				bestNode = heuristic.getNode().get(heuristic.getDirection());
 			
 			if(i == range) 
 				break;
		}
	}
	
	public void buildNode(TreeNode node, boolean ignoreCorrectPiece) {
		if(node == null)
			return;
		
		TreeNode createdNode = null;
		if(DIRECTION.UP != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.UP) && node.getSelectedRow() > 0) {
			node.create(DIRECTION.UP, ignoreCorrectPiece);
			createdNode = node.get(DIRECTION.UP);
			if(createdNode != null) {
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		if(DIRECTION.DOWN != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.DOWN) && node.getSelectedRow() < SharedVariables.numOfRow-1) {
			node.create(DIRECTION.DOWN, ignoreCorrectPiece);
			createdNode = node.get(DIRECTION.DOWN);
			if(createdNode != null) {
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		if(DIRECTION.LEFT != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.LEFT) && node.getSelectedCol() > 0) {
			node.create(DIRECTION.LEFT, ignoreCorrectPiece);
			createdNode = node.get(DIRECTION.LEFT);
			if(createdNode != null) {
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		if(DIRECTION.RIGHT != node.getParentDirection() && !node.isDirectionInactive(DIRECTION.RIGHT) && node.getSelectedCol() < SharedVariables.numOfColumn-1) {
			createdNode = node.get(DIRECTION.RIGHT);
			if(createdNode != null) {
				if(bestNode.getAccurancy() < createdNode.getAccurancy()) {
					bestNode = createdNode;
				}
			}
		}
		TreeNode.getCheckpoints().remove(node);
	}
	
	public Path exportPath() {
		Path path = bestNode.exportPath(); 
		path.setStartRow(selectedRow);
		path.setStartCol(selectedCol);
		return path;
	}	
}

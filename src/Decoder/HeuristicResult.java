package Decoder;

import Tree.TreeNode;
import DecoderData.DIRECTION;

public class HeuristicResult {
	private TreeNode node;
	private DIRECTION direction;
	
	public HeuristicResult(TreeNode node, DIRECTION direction) {
		this.node = node;
		this.direction = direction;
	}
	
	public HeuristicResult() {
		this(null, DIRECTION.NONE);
	}

	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

	public DIRECTION getDirection() {
		return direction;
	}

	public void setDirection(DIRECTION direction) {
		this.direction = direction;
	}
	
	
}

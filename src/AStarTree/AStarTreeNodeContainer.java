 package AStarTree;

import DecoderImage.ImageMap;

public class AStarTreeNodeContainer {
	private AStarBranchNode root;
	private int range;
	private long size;
	
	public AStarTreeNodeContainer(int range) {
		this.size = 0;
		this.range = range;
		root = new AStarBranchNode(range);
		root.setPrevious(null);
	}

	public int getRange() {
		return range;
	}
	
	public void add(AStarTreeNode node) {
		AStarBranchNode pointer = root;
		int[] code = node.getImageCode();
		for(int i = 0; i < code.length; i++) {
			if(pointer.getNext(code[i]) == null) {
				pointer.setNext(code[i]);
			}
			pointer = pointer.getNext(code[i]);
		}
		pointer.setNode(node);
		size++;
	}
	
	public AStarTreeNode contain(ImageMap image) {
		int[] code = image.extractCode();
		AStarBranchNode pointer = root;
		for(int i = 0; i < code.length; i++) {
			pointer = pointer.getNext(code[i]);
			if(pointer == null) 
				return null;
		}
		return pointer.getNode();
	}

	public long getSize() {
		return size;
	}
	
	public void clear() {
		this.size = 0;
		root = new AStarBranchNode(range);
	}
}

class AStarBranchNode {
	private AStarTreeNode node;
	private int range;
	private AStarBranchNode previous;
	private AStarBranchNode[] next;
	
	public AStarBranchNode() {
		
	}
	
	public AStarBranchNode(int range) {
		this.setNode(null);
		this.range = range;
		setPrevious(null);
		next = new AStarBranchNode[range];
		for(int i = 0 ; i < range; i++) {
			next[i] = null;
		}
	}

	public AStarBranchNode getNext(int i) {
		return next[i];
	}

	public void setNext(int i) {
		next[i] = new AStarBranchNode(this.range);
		next[i].setPrevious(this);
	}
	
	public void removeNext(int i) {
		next[i] = null;
	}

	public AStarBranchNode getPrevious() {
		return previous;
	}

	public void setPrevious(AStarBranchNode previous) {
		this.previous = previous;
	}

	public AStarTreeNode getNode() {
		return node;
	}

	public void setNode(AStarTreeNode node) {
		this.node = node;
	}
}

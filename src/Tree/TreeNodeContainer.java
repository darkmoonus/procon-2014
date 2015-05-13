 package Tree;

import DecoderImage.ImageMap;

public class TreeNodeContainer {
	private BranchNode root;
	private int range;
	private long size;
	
	public TreeNodeContainer(int range) {
		this.size = 0;
		this.range = range;
		root = new BranchNode(range);
		root.setPrevious(null);
	}

	public int getRange() {
		return range;
	}
	
	public void add(TreeNode node) {
		BranchNode pointer = root;
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
	
	public TreeNode contain(ImageMap image) {
		int[] code = image.extractCode();
		BranchNode pointer = root;
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
		root = new BranchNode(range);
	}
}

class BranchNode {
	private TreeNode node;
	private int range;
	private BranchNode previous;
	private BranchNode[] next;
	
	public BranchNode() {
		
	}
	
	public BranchNode(int range) {
		this.setNode(null);
		this.range = range;
		setPrevious(null);
		next = new BranchNode[range];
		for(int i = 0 ; i < range; i++) {
			next[i] = null;
		}
	}

	public BranchNode getNext(int i) {
		return next[i];
	}

	public void setNext(int i) {
		next[i] = new BranchNode(this.range);
		next[i].setPrevious(this);
	}
	
	public void removeNext(int i) {
		next[i] = null;
	}

	public BranchNode getPrevious() {
		return previous;
	}

	public void setPrevious(BranchNode previous) {
		this.previous = previous;
	}

	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}
}

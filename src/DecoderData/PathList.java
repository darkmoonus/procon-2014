package DecoderData;

import java.util.ArrayList;

import DecoderImage.ImageAnalyzer;

public class PathList extends ArrayList<Path> implements Comparable<PathList>{	
	private static final long serialVersionUID = 6912832517072130006L;
	private int restoredRate;
	
	public PathList() {
		super();
		this.restoredRate = 0;
	}
	
	@Override
	public boolean add(Path e) {
		if(e.size() == 0)
			return false;
		
		if(size() == 0)
			return super.add(e);
		
		Position newSelect = new Position(e.getStartRow(), e.getStartCol());
		Position currentSelect = ImageAnalyzer.calculateCurrentSelection(get(size()-1));
		if(currentSelect.equals(newSelect)) {
			get(size()-1).getSteps().addAll(e.getSteps());
		} 
		else {
			super.add(e);
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < size(); i++) {
			result.append(get(i).toString() + "\n");
		}
		return result.toString();
	}
	
	@Override
	public Object clone() {
		PathList clone = new PathList();
		for(int i = 0; i < size(); i++) {
			clone.add(get(i));
		}
		clone.restoredRate = this.restoredRate;
		
		return clone;
	}

	public int getRestoredRate() {
		return restoredRate;
	}

	public void setRestoredRate(int restoredRate) {
		this.restoredRate = restoredRate;
	}

	public int getNumOfSelect() {
		return size();
	}
	
	public int getNumOfSwap() {
		int swap = 0;
		for(int i =0; i < size(); i++) {
			swap += get(i).size();
		}
		return swap;
	}
	
	@Override
	public int compareTo(PathList o) {
		if(this.restoredRate - o.restoredRate != 0) {
			return this.restoredRate - o.restoredRate;
		} else if(this.getNumOfSelect() - o.getNumOfSelect() != 0) { 
			return o.getNumOfSelect() - this.getNumOfSelect();
		} else if(o.getNumOfSwap() - this.getNumOfSwap() != 0) {
			return o.getNumOfSwap() - this.getNumOfSwap();
		} else {
			return 0;
		}
	}
}

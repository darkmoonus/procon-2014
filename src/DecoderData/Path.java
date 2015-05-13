package DecoderData;
import java.util.ArrayList;

public final class Path {
	private int startRow;
	private int startCol;
	private ArrayList<DIRECTION> steps;
	
	public Path(int startRow, int startCol) {
		this.startCol = startCol;
		this.startRow = startRow;
		this.steps = new ArrayList<DIRECTION>();
	}
	
	public Path() {
		this(-1,-1);
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public ArrayList<DIRECTION> getSteps() {
		return steps;
	}
	
	public boolean add(DIRECTION direction) {
		return steps.add(direction);
	}
	
	public int size() {
		return steps.size();
	}
	
	public DIRECTION get(int i) {
		return steps.get(i);
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		newString.append(String.format("select[%d,%d]: ", startRow, startCol));
		for(int i = 0; i < steps.size(); i++)
			newString.append(steps.get(i) + " ");
		return newString.toString();
	}
}

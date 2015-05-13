package Decoder;

public class ResultViewer {
	private int correct = 0;
	private int selection = 0;
	private int swap = 0;
	private int sum = 0;
	public ResultViewer() {
		
	}
	public ResultViewer(int correct, int selection, int swap, int sum) {
		setCorrect(correct);
		setSelection(selection);
		setSwap(swap);
		setSum(sum);
	}
	public synchronized int getCorrect() {
		return correct;
	}
	public synchronized void setCorrect(int correct) {
		this.correct = correct;
	}
	public synchronized int getSelection() {
		return selection;
	}
	public synchronized void setSelection(int selection) {
		this.selection = selection;
	}
	public synchronized int getSwap() {
		return swap;
	}
	public synchronized void setSwap(int swap) {
		this.swap = swap;
	}
	public synchronized int getSum() {
		return sum;
	}
	public synchronized void setSum(int sum) {
		this.sum = sum;
	}
}

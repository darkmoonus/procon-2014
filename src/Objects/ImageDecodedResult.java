package Objects;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.ImageIcon;

public class ImageDecodedResult {
	public ImageIcon icon;
	public double sum;
	public Dimension division;
	public Point[][] posArr;;
	public ImageDecodedResult() {
		icon = new ImageIcon();
	}
	public ImageDecodedResult(ImageIcon icon, double sum, Dimension division, Point[][] posArr2) {
		this.division = new Dimension();
		this.division = division;
		posArr = new Point[division.height][division.width];
		for(int i=0; i<division.height; i++) {
			for(int j=0; j<division.width; j++) {
				this.posArr[i][j] = new Point(posArr2[i][j]);
			}
		}
		this.icon = icon;
		this.sum = sum;
		this.posArr = posArr2;
	}
	public String getPosArrToString() {
		String s = "";
		for(int t=0; t<division.height; t++) {
			for(int u=0; u<division.width; u++) {
				s += " (" + posArr[t][u].x + ", " + posArr[t][u].y + ") ";
			}
			s += "\n";
		}
		return s;
	}
	public String getPosArrToNumberString() {
		String s = "";
		for(int t=0; t<division.height; t++) {
			for(int u=0; u<division.width; u++) {
				int kk = division.width * posArr[t][u].x + posArr[t][u].y;
				s += kk + " ";
			}
			s += "\n";
		}
		return s;
	}
}
package Objects;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

public class Piece {
	private Dimension resolution;
	private Pixel[][] pixelsMatrix;
	public Piece() {
		
	}
	public Piece(Dimension resolution) {
		setResolution(resolution);
		pixelsMatrix = new Pixel[resolution.height][resolution.width];
		for(int i=0; i<resolution.height; i++) {
			for(int j=0; j<resolution.width; j++) {
				pixelsMatrix[i][j] = new Pixel();
			}
		}
	}
	public Piece(Dimension resolution, Pixel[][] PixelsMatrix) {
		setResolution(resolution);
		setPixelsMatrix(PixelsMatrix);
	}
	
	public ArrayList<Pixel> getUpPixelsLine() {
		ArrayList<Pixel> upPixelsLine = new ArrayList<Pixel>();
		for(int i=0; i<resolution.width; i++) {
			upPixelsLine.add(pixelsMatrix[0][i]);
		}
		return upPixelsLine;
	}
	public ArrayList<Pixel> getDownPixelsLine() {
		ArrayList<Pixel> downPixelsLine = new ArrayList<Pixel>();
		for(int i=0; i<resolution.width; i++) {
			downPixelsLine.add(pixelsMatrix[resolution.height-1][i]);
		}
		return downPixelsLine;
	}
	public ArrayList<Pixel> getLeftPixelsLine() {
		ArrayList<Pixel> leftPixelsLine = new ArrayList<Pixel>();
		for(int i=0; i<resolution.height; i++) {
			leftPixelsLine.add(pixelsMatrix[i][0]);
		}
		return leftPixelsLine;
	}
	public ArrayList<Pixel> getRightPixelsLine() {
		ArrayList<Pixel> rightPixelsLine = new ArrayList<Pixel>();
		for(int i=0; i<resolution.height; i++) {
			rightPixelsLine.add(pixelsMatrix[i][resolution.width-1]);
		}
		return rightPixelsLine;
	}
	
	public Pixel getPixelAt(Point pos) {
		return this.pixelsMatrix[pos.x][pos.y];
	}
	public void setPixelAt(Point pos, Pixel pixel) {
		this.pixelsMatrix[pos.x][pos.y] = pixel;
	}
	public Pixel[][] getPixelsMatrix() {
		return pixelsMatrix;
	}
	public void setPixelsMatrix(Pixel[][] pixelsMatrix) {
		this.pixelsMatrix = pixelsMatrix;
	}
	public Dimension getResolution() {
		return resolution;
	}
	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
	}

}

package Objects;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.ImageIcon;

import Interfaces.CustomButton;
import Sources.ImageProcessing;

public class Image {
	private Dimension division;
	private Dimension resolution;
	private Piece[][] piecesMatrix;
	private Pixel[][] pixelsMatrix;
	
	private CustomButton decodeProcessingTime;
	private CustomButton decodeProcessingPercent;
	
	class ProcessingTime extends Thread {
		public ProcessingTime() {
			super();
		}
		@Override
		public void run() {
			super.run();
			int i = 0;
			while(true) {
				int m = i/60;
				int s = i%60;
				if(m > 9) {
					if(s > 9) {
						decodeProcessingTime.setText("Time " + m + ":" + s);
					} else {
						decodeProcessingTime.setText("Time " + m + ":0" + s);
					}
				} else {
					if(s > 9) {
						decodeProcessingTime.setText("Time 0" + m + ":" + s);
					} else {
						decodeProcessingTime.setText("Time 0" + m + ":0" + s);
					}
				}
				try {
					sleep(1000);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<ImageDecodedResult> getDecodedImageIcon(CustomButton decodeProcessingTime, CustomButton decodeProcessingPercent) throws IOException {
		
		setDecodeProcessingPercent(decodeProcessingPercent);
		setDecodeProcessingTime(decodeProcessingTime);
		
		new ProcessingTime().start();
		
		ArrayList<ImageDecodedResult> decodedResult = new ArrayList<ImageDecodedResult>();
		
		for(int i=0; i<division.height; i++) {
			for(int j=0; j<division.width; j++) {
				
				int percent = 100 * (i * division.width + j) / (division.width * division.height);
				decodeProcessingPercent.setText("Processing " + percent + "% ...");
				
				double sumsum = 0;
				Point[][] posArr = new Point[division.height][division.width];
				posArr[i][j] = new Point(0, 0);
				
				boolean[][] mark = new boolean[division.height][division.width];
				for(int p=0; p<division.height; p++) {
					for(int q=0; q<division.width; q++) {
						mark[p][q] = true;
					}
				}
				Piece[][] expectedPieces = new Piece[division.height][division.width];
				expectedPieces[0][0] = piecesMatrix[i][j];
				int currentPieceIndex = 1;
				mark[i][j] = false;
				while(currentPieceIndex < division.height * division.width) {
					double min = 99999999;
					int ii = 0;
					int jj = 0;
					for(int k=0; k<division.height; k++) {
						for(int l=0; l<division.width; l++) {
							if(mark[k][l]) {
								if (currentPieceIndex < division.width) {
									double sum = 0;
									int oldHeightIndex = (currentPieceIndex-1)/division.width;
									int oldWidthIndex = (currentPieceIndex-1) % division.width;
									ArrayList<Pixel> leftPixels = piecesMatrix[k][l].getLeftPixelsLine();
									ArrayList<Pixel> rightPixels = expectedPieces[oldHeightIndex][oldWidthIndex].getRightPixelsLine();
									for(int y=0; y<leftPixels.size(); y++) {
										sum += leftPixels.get(y).compareWith(rightPixels.get(y));
									}
									if(sum < min) {
										min = sum;
										sumsum += sum;
										ii = k;
										jj = l;
									}
								} else
								if (currentPieceIndex % division.width == 0) {
									double sum = 0;
									int oldHeightIndex = (currentPieceIndex)/division.width - 1;
									int oldWidthIndex = (currentPieceIndex) % division.width;
									
									ArrayList<Pixel> upPixels = piecesMatrix[k][l].getUpPixelsLine();
									ArrayList<Pixel> downPixels = expectedPieces[oldHeightIndex][oldWidthIndex].getDownPixelsLine();
									for(int y=0; y<upPixels.size(); y++) {
										sum += upPixels.get(y).compareWith(downPixels.get(y));
									}
									if(sum < min) {
										min = sum;
										sumsum += sum;
										ii = k;
										jj = l;
									}
								} else {
									double sum = 0;
									int oldHeightIndex = (currentPieceIndex-1)/division.width;
									int oldWidthIndex = (currentPieceIndex-1) % division.width;
									
									ArrayList<Pixel> leftPixels = piecesMatrix[k][l].getLeftPixelsLine();
									ArrayList<Pixel> rightPixels = expectedPieces[oldHeightIndex][oldWidthIndex].getRightPixelsLine();
									for(int y=0; y<leftPixels.size(); y++) {
										sum += leftPixels.get(y).compareWith(rightPixels.get(y));
									}
									
									int oldHeightIndex2 = currentPieceIndex/division.width - 1;
									int oldWidthIndex2 = currentPieceIndex % division.width;
									
									ArrayList<Pixel> upPixels = piecesMatrix[k][l].getUpPixelsLine();
									ArrayList<Pixel> downPixels = expectedPieces[oldHeightIndex2][oldWidthIndex2].getDownPixelsLine();
									for(int y=0; y<upPixels.size(); y++) {
										sum += upPixels.get(y).compareWith(downPixels.get(y));
									}
									if(sum < min) {
										min = sum;
										sumsum += sum;
										ii = k;
										jj = l;
									}
								}
								
							}
						}
					}
					
					expectedPieces[currentPieceIndex/division.width][currentPieceIndex % division.width] = piecesMatrix[ii][jj];
					posArr[ii][jj] = new Point(currentPieceIndex/division.width, currentPieceIndex%division.width);
					
					currentPieceIndex ++;
					mark[ii][jj] = false;
					
					
				}
				
				Image image = new Image(division, expectedPieces);
				ImageDecodedResult result = new ImageDecodedResult(image.getImageIcon(), sumsum, division, posArr);
				decodedResult.add(result);
				
			}
		}
		
		Collections.sort(decodedResult, new Comparator<ImageDecodedResult>() {
			@Override
			public int compare(ImageDecodedResult image1, ImageDecodedResult image2) {
				if(image1.sum > image2.sum) return 1;
				return -1;
			}
	    });
		
		
		return decodedResult;
	}
	
	//////////////////////////////////////////////////////
	
	public ArrayList<ImageIcon> decodedResult2;
	public int[] xx;
	public boolean check(int v, int i) {
	    for (int j=0; j<i; j++) {
	    	if (xx[j]==v) {
	    		return false;
	    	}
	    }
	    return true;
	}
	public void process(int i, int n) throws IOException {
		if(i>n-1) {
			Piece[][] expectedPieces = new Piece[division.height][division.width];
			int count = 0;
			for(int k=0; k<n; k++) {
				expectedPieces[count/division.width][count%division.width] = piecesMatrix[xx[k]/division.width][xx[k]%division.width];
				count++;
			}
			Image image = new Image(division, expectedPieces);
			decodedResult2.add(image.getImageIcon());
			return;
		}
		for (int v=0; v<n; v++)
			if (check(v, i)) {
				xx[i] = v;
				process(i+1, n);
			}
	}
	
	public void processNoBacktracking(int n) throws IOException {
		int[] x = new int[division.width * division.height + 1];
		int i;
		for(int j=1; j<=n; j++) x[j] = j;
		do {
			Piece[][] expectedPieces = new Piece[division.height][division.width];
			int count = 0;
			for(int j=1; j<=n; j++) {
				expectedPieces[count/division.width][count%division.width] = piecesMatrix[(x[j]-1)/division.width][(x[j]-1)%division.width];
				count++;
			}
			Image image = new Image(division, expectedPieces);
			decodedResult2.add(image.getImageIcon());
			
			i = n-1;
			while(i>0 && x[i] > x[i+1]) i--;
			if(i>0) {
				int k=n;
				while(x[k] < x[i]) k--;
				int temp = x[k];
				x[k] = x[i];
				x[i] = temp;
				int a = i+1;
				int b = n;
				while(a<b) {
					int temp2 = x[a];
					x[a] = x[b];
					x[b] = temp2;
					a++;
					b--;
				}
			}
		} while(i!=0);
	}
	
	public ArrayList<ImageIcon> getDecodedImageIconBacktracking() throws IOException {
		decodedResult2 = new ArrayList<ImageIcon>();
		int n = division.width * division.height;
		xx = new int[division.width * division.height];
		process(0, n);
		return decodedResult2;
	}
	
	public ArrayList<ImageIcon> getDecodedImageIconNoBacktracking() throws IOException {
		decodedResult2 = new ArrayList<ImageIcon>();
		int n = division.width * division.height;
		processNoBacktracking(n);
		return decodedResult2;
	}
	
	///////////////////////////////////////////
	
	
	public Image() {
		division = new Dimension(0, 0);
		resolution = new Dimension(0, 0);
		piecesMatrix = new Piece[0][0];
		pixelsMatrix = new Pixel[0][0];
	}
	public Image(Dimension division, Piece[][] piecesMatrix) {
		setDivision(division);
		setPiecesMatrix(piecesMatrix);
		setResolution(new Dimension(piecesMatrix[0][0].getResolution().width * division.width, piecesMatrix[0][0].getResolution().height * division.height));
		setPixelsMatrix(getPixelsMatrixFromPiecesMatrix(piecesMatrix, resolution, division));
	}
	public Image(Pixel[][] pixelsMatrix2, Dimension division, Dimension resolution) {
		setDivision(division);
		setResolution(resolution);
		setPixelsMatrix(pixelsMatrix2);
		setPiecesMatrix(getPiecesMatrixFromPixelMatrix(pixelsMatrix2, resolution, division));
	}
	public Image(Piece[][] piecesMatrix2, Dimension division, Dimension resolution) {
		setDivision(division);
		setResolution(resolution);
		setPiecesMatrix(piecesMatrix2);
		setPixelsMatrix(getPixelsMatrixFromPiecesMatrix(piecesMatrix2, resolution, division));
	}
	
	public String generateTest(String outputFileName, Dimension resolution, Dimension division) throws IOException {
		Piece[][] piecesMatrix2 = new Piece[division.height][division.width];
		piecesMatrix2 = getPiecesMatrixFromPixelMatrix(getPixelsMatrixFromPiecesMatrix(this.piecesMatrix, this.resolution , this.division), resolution, division);
		for(int i=0; i< division.width * division.height; i++) {
			int i1 = (int)(Math.random() * division.height);
			int i2 = (int)(Math.random() * division.height);
			int j1 = (int)(Math.random() * division.width);
			int j2 = (int)(Math.random() * division.width);
			Piece temp = piecesMatrix2[i1][j1];
			piecesMatrix2[i1][j1] = piecesMatrix2[i2][j2];
			piecesMatrix2[i2][j2] = temp;
		}
		String URL = outputFileName;
		
		Pixel[][] pixelsMatrixNew = getPixelsMatrixFromPiecesMatrix(piecesMatrix2, resolution, division);
		ImageProcessing.convertPixelsMatrixToPNG(pixelsMatrixNew, resolution, URL);
		return URL;
	}
	
	public ImageIcon getImageIcon() throws IOException {
		ImageIcon image = ImageProcessing.getImageIconFromPixelsMatrix(pixelsMatrix, resolution);
		return image;
	}
	
	public Piece[][] getPiecesMatrixFromPixelMatrix(Pixel[][] pixelsMatrix, Dimension resolution, Dimension division) {
		Piece[][] piecesMatrix = new Piece[division.height][division.width];
		int pixelPerWidthPiece = resolution.width / division.width;
		int pixelPerHeightPiece = resolution.height / division.height;
		for(int i=0; i<division.height; i++) {
			for(int j=0; j<division.width; j++) {
				piecesMatrix[i][j] = new Piece(new Dimension(pixelPerWidthPiece, pixelPerHeightPiece));
				for(int k=0; k<pixelPerHeightPiece; k++) {
					for(int l=0; l<pixelPerWidthPiece; l++) {
						piecesMatrix[i][j].setPixelAt(new Point(k, l), pixelsMatrix[i * pixelPerHeightPiece + k][j * pixelPerWidthPiece + l]); 
					}
				}
			}
		}
		return piecesMatrix;
	}
	public Pixel[][] getPixelsMatrixFromPiecesMatrix(Piece[][] piecesMatrix, Dimension resolution, Dimension division) {
		Pixel[][] pixelsMatrix2 = new Pixel[resolution.height][resolution.width];
		int pixelPerWidthPiece = resolution.width / division.width;
		int pixelPerHeightPiece = resolution.height / division.height;
		for(int i=0; i<division.height; i++) {
			for(int j=0; j<division.width; j++) {
				for(int k=0; k<pixelPerHeightPiece; k++) {
					for(int l=0; l<pixelPerWidthPiece; l++) {
						pixelsMatrix2[i * pixelPerHeightPiece + k][j * pixelPerWidthPiece + l] = new Pixel();
						pixelsMatrix2[i * pixelPerHeightPiece + k][j * pixelPerWidthPiece + l] = piecesMatrix[i][j].getPixelAt(new Point(k, l)); 
					}
				}
			}
		}
		return pixelsMatrix2;
	}
	public Dimension getDivision() {
		return division;
	}
	public void setDivision(Dimension division) {
		this.division = division;
	}
	public Dimension getResolution() {
		return resolution;
	}
	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
	}
	public void setPixelsMatrix(Pixel[][] pixelsMatrix) {
		this.pixelsMatrix = pixelsMatrix;
	}
	public Pixel[][] getPixelsMatrix() {
		return this.pixelsMatrix;
	}
	public Pixel getPixelAt(Point pos) {
		return this.pixelsMatrix[pos.x][pos.y];
	}
	public void setPixelAt(Point pos, Pixel pixel) {
		this.pixelsMatrix[pos.x][pos.y] = pixel;
	}
	public Piece getPieceAt(Point pos) {
		return this.piecesMatrix[pos.x][pos.y];
	}
	public void setPieceAt(Point pos, Piece piece) {
		this.piecesMatrix[pos.x][pos.y] = piece;
	}
	public Piece[][] getPiecesMatrix() {
		return this.piecesMatrix;
	}
	public void setPiecesMatrix(Piece[][] piecesMatrix) {
		this.piecesMatrix = piecesMatrix;
	}
	@Override
	public String toString() {
		return "Image [division=" + division + ", resolution=" + resolution
				+ ", piecesMatrix=" + Arrays.toString(piecesMatrix)
				+ ", pixelsMatrix=" + Arrays.toString(pixelsMatrix) + "]";
	}
	public CustomButton getDecodeProcessingTime() {
		return decodeProcessingTime;
	}
	public void setDecodeProcessingTime(CustomButton decodeProcessingTime) {
		this.decodeProcessingTime = decodeProcessingTime;
	}
	public CustomButton getDecodeProcessingPercent() {
		return decodeProcessingPercent;
	}
	public void setDecodeProcessingPercent(CustomButton decodeProcessingPercent) {
		this.decodeProcessingPercent = decodeProcessingPercent;
	}

}

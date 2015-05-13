package Decoder;

import java.awt.Point;

import DecoderData.Path;
import DecoderData.PathList;
import DecoderData.Position;
import DecoderImage.ImageAnalyzer;
import DecoderImage.ImageMap;

public class ImageRestorer {
	public static void main(String[] args) {
		Point[][] image = generate(w, h);
		ResultViewer resultViewer = null;
		System.out.println(getFinalResult(image, 16, true, true, true, 100000, 500, 1000, resultViewer));
	} 
	
	public static int w = 4;
	public static int h = 5;
	
	public static String getFinalResult(Point[][] matrix, int maxSelectTimes, boolean haveNo, boolean haveSimple, boolean haveIDA, int rangeNo, int rangeSimple, int rangeIDA, ResultViewer resultViewer) {
		PathList bestResult = new PathList();
		PathList noHeuristicResult = new PathList();
		PathList simpleHeuristicResult = new PathList();
		PathList idaStarResult = new PathList();
		
		String s = "noHeuristic";
		
		int ratePercent = 0;
		
		if(haveNo) {
			boolean ignoreCorrectPiece = false;
			System.out.println("Start noHeuristic : " + rangeNo);
			noHeuristicResult = noHeuristic(matrix, maxSelectTimes, rangeNo, ignoreCorrectPiece);
			if(noHeuristicResult.compareTo(bestResult) > 0) {
				bestResult = noHeuristicResult;
			}
			System.out.println("done noHeuristic : " + noHeuristicResult.getRestoredRate() + " :: " + ratePercent + "%");
		}
		
		if(haveSimple) {
			boolean ignoreCorrectPiece2 = false;
			System.out.println("Start simple heuristic : " + rangeSimple);
			simpleHeuristicResult = simpleHeuristic(matrix, maxSelectTimes, rangeSimple, ignoreCorrectPiece2);
			if(simpleHeuristicResult.compareTo(bestResult) > 0) {
				bestResult = simpleHeuristicResult;
				s = "simpleHeuristic";
			}
			ratePercent =  100 * simpleHeuristicResult.getRestoredRate() / (w*h);
			System.out.println("done simpleHeuristic : " + simpleHeuristicResult.getRestoredRate() + " :: " + ratePercent + "%");
		}
		
		if(haveIDA){
			boolean ignoreCorrectPiece3 = false;
			System.out.println("Start IDAStar : " + rangeIDA);
			idaStarResult =  IDAStar(matrix, maxSelectTimes, rangeIDA, ignoreCorrectPiece3);
			if(idaStarResult.compareTo(bestResult) > 0) {
				bestResult = idaStarResult;
				s = "idaStar";
			}
			ratePercent =  100 * idaStarResult.getRestoredRate() / (w*h);
			System.out.println("done idaStar : " + idaStarResult.getRestoredRate() + " :: " + ratePercent + "%");
		}
		
		System.out.println(bestResult.getRestoredRate() + "\n Chosen ---> " + s);
		
		resultViewer.setCorrect(bestResult.getRestoredRate());
		resultViewer.setSelection(0);
		resultViewer.setSum(0);
		resultViewer.setSwap(0);
		
		
		return generateAnswer(bestResult);
	}
	
	private static PathList simpleHeuristic(Point[][] matrix, int maxSelectTimes, int range, boolean ignoreCorrectPiece) {
		
		ImageMap image = new ImageMap(matrix);
		
		//initial
		Position[] invalidPieces = ImageAnalyzer.getInvalidPieces(image);
		if(invalidPieces.length == 0)
			return new PathList();
		boolean connected = true;
		int numOfSelect = 0;
		
		//result
		PathList bestResult = new PathList();
		PathList result = new PathList();
		
		//find
		while(invalidPieces.length != 0 && numOfSelect < maxSelectTimes) {
			Path path = null;
			
			if(!ImageAnalyzer.isDeadMap(image) && connected){
		
				path = PathFinder.simpleHeuristic(image, invalidPieces[0].getRow(),invalidPieces[0].getCol(), ignoreCorrectPiece, range);
				
				//try to find better way
				int restoredRate =  (new ImageMap(image, path)).getRestoredRate();
				for(int i = 1; i < invalidPieces.length % 4; i++) {
					Path newPath = PathFinder.simpleHeuristic(image, invalidPieces[i].getRow(),invalidPieces[0].getCol(), ignoreCorrectPiece, range);
					if((new ImageMap(image,newPath)).getRestoredRate() > restoredRate) {
						path = newPath;
					}
				}
				
				//image is not connected, bypass this step
				ImageMap newImage = new ImageMap(image, path);
				if(newImage.getRestoredRate() <= image.getRestoredRate()) {
					connected = false;
					continue;
				}
				
			} else { //solve not connected map, direct swap
				
				Position worstPiece = invalidPieces[0];
				int worstDistance = ImageAnalyzer.calculateDistance(image, worstPiece.getRow(), worstPiece.getCol());
				
				for(int i = 0; i < invalidPieces.length; i++) {
					int newDistance = ImageAnalyzer.calculateDistance(image, invalidPieces[i].getRow(), invalidPieces[i].getCol());
					if( newDistance > worstDistance) {
						worstPiece = invalidPieces[i];
						worstDistance = newDistance;
					}
				}
				
				path = PathFinder.shortestPath(image, worstPiece.getRow(), worstPiece.getCol());				
				connected = true;
			}
			
			//reinitialize
			result.add(path);
			image = new ImageMap(image,path);
			result.setRestoredRate(image.getRestoredRate());
			invalidPieces = ImageAnalyzer.getInvalidPieces(image);
			
			if(result.getRestoredRate() > bestResult.getRestoredRate()) {
				bestResult = (PathList) result.clone();
			}
			 			
 			numOfSelect++;
		}
		
		return bestResult;
	 } 

	private static PathList noHeuristic(Point[][] matrix, int maxSelectTimes, int range, boolean ignoreCorrectPiece) {
		ImageMap image = new ImageMap(matrix);
		
		//initial
		Position[] invalidPieces = ImageAnalyzer.getInvalidPieces(image);
		if(invalidPieces.length == 0)
			return new PathList();
		boolean connected = true;
		int numOfSelect = 0;
		
		//result
		PathList bestResult = new PathList();
		PathList result = new PathList();
		
		//find
		while(invalidPieces.length != 0 && numOfSelect <= maxSelectTimes) {
			Path path = null;
			
			if(!ImageAnalyzer.isDeadMap(image) && connected){
		
				path = PathFinder.noHeuristic(image, invalidPieces[0].getRow(),invalidPieces[0].getCol(), ignoreCorrectPiece, range);
				
				//try to find better way
				int accurancy =  (new ImageMap(image, path)).getRestoredRate();
				for(int i = 1; i < invalidPieces.length % 4; i++) {					
					Path newPath = PathFinder.noHeuristic(image, invalidPieces[i].getRow(),invalidPieces[i].getCol(), ignoreCorrectPiece, range);
					if((new ImageMap(image,newPath)).getRestoredRate() > accurancy) {
						path = newPath;
					}
				}
				
				//image is not connected, bypass this step
				ImageMap newImage = new ImageMap(image, path);
				if(newImage.getRestoredRate() <= image.getRestoredRate()) {
					connected = false;
					continue;
				}
				
			} else { //solve not connected map, direct swap
				Position worstPiece = invalidPieces[0];
				int worstDistance = ImageAnalyzer.calculateDistance(image, worstPiece.getRow(), worstPiece.getCol());
				
				for(int i = 0; i < invalidPieces.length; i++) {
					int newDistance = ImageAnalyzer.calculateDistance(image, invalidPieces[i].getRow(), invalidPieces[i].getCol());
					if( newDistance > worstDistance) {
						worstPiece = invalidPieces[i];
						worstDistance = newDistance;
					}
				}
				
				path = PathFinder.shortestPath(image, worstPiece.getRow(), worstPiece.getCol());				
				connected = true;
			}
			
			//reinitialize
			result.add(path);
			image = new ImageMap(image, path);
			invalidPieces = ImageAnalyzer.getInvalidPieces(image);
			result.setRestoredRate(image.getRestoredRate());
			if(result.getRestoredRate() > bestResult.getRestoredRate()) {
				bestResult = (PathList) result.clone();
			}
			
//			Position currSel = ImageAnalyzer.calculateCurrentSelection(path);
// 			if(image.checkPiece(currSel.getRow(), currSel.getCol()) && invalidPieces.length != 0) {
//				selectedPos.setRow(invalidPieces[0].getRow());
//				selectedPos.setCol(invalidPieces[0].getCol());
//			} else {
//				selectedPos.setRow(currSel.getRow());
//				selectedPos.setCol(currSel.getCol());
//			}
			
			numOfSelect++;
		}
		
		

		return bestResult;
	 } 
	
	private static PathList IDAStar(Point[][] matrix, int maxSelectTimes, int range, boolean ignoreCorrectPiece) {
		ImageMap image = new ImageMap(matrix);
		ImageMap rootImage = image;
		
		//result
		PathList result = new PathList();
		PathList bestResult = new PathList();
		
		//initialize
		Position[] invalids = ImageAnalyzer.getInvalidPieces(image);
		if(invalids.length == 0)
			return result;
		Position nextSelect = invalids[0];
		Path path = null;
		
		while(invalids.length != 0 && result.size() <= maxSelectTimes) {
			if(!ImageAnalyzer.isDeadMap(image)) {
				nextSelect = invalids[0];
				path = PathFinder.IDAStar(image, nextSelect.getRow(), nextSelect.getCol(), ignoreCorrectPiece, range);
			} else {
				Position worstPiece = invalids[0];
				int worstDistance = ImageAnalyzer.calculateDistance(image, worstPiece.getRow(), worstPiece.getCol());
				
				for(int i = 0; i < invalids.length; i++) {
					int newDistance = ImageAnalyzer.calculateDistance(image, invalids[i].getRow(), invalids[i].getCol());
					if( newDistance > worstDistance) {
						worstPiece = invalids[i];
						worstDistance = newDistance;
					}
				}
				
				path = PathFinder.shortestPath(image, worstPiece.getRow(), worstPiece.getCol());
			}
			
			result.add(path);
			
			int size = result.size();
			result.add(path);
			if(result.size() == size) {
				break;
			}
			
			image = new ImageMap(image, path);
			invalids = ImageAnalyzer.getInvalidPieces(image);
			result.setRestoredRate(image.getRestoredRate());
			
			if((new ImageMap(rootImage, bestResult)).getRestoredRate() < image.getRestoredRate()) {
				bestResult = (PathList)result.clone();
						
			}
		}
		
		boolean connected = true;
		invalids = ImageAnalyzer.getInvalidPieces(image);
		while(result.getNumOfSelect() < maxSelectTimes && invalids.length != 0) {
			if(ImageAnalyzer.isDeadMap(image) && connected) {
				path = PathFinder.simpleHeuristic(image, invalids[0].getRow() , invalids[0].getCol(), true, 200000);
				
				if((new ImageMap(image,path)).getRestoredRate() <= image.getRestoredRate()) {
					connected = false;
				}
			} else {
				Position worstPiece = invalids[0];
				int worstDistance = ImageAnalyzer.calculateDistance(image, worstPiece.getRow(), worstPiece.getCol());
				
				for(int i = 0; i < invalids.length; i++) {
					int newDistance = ImageAnalyzer.calculateDistance(image, invalids[i].getRow(), invalids[i].getCol());
					if( newDistance > worstDistance) {
						worstPiece = invalids[i];
						worstDistance = newDistance;
					}
				}
				path = PathFinder.shortestPath(image, worstPiece.getRow(), worstPiece.getCol());				
				connected = true;
			}
			
			result.add(path);
			image = new ImageMap(image, path);
			invalids = ImageAnalyzer.getInvalidPieces(image);
			result.setRestoredRate(image.getRestoredRate());
			
			if(bestResult.getRestoredRate() > result.getRestoredRate()) {
				bestResult = (PathList) result.clone();
			}
			
		}
		
		return bestResult;
	} 

	@SuppressWarnings("unused")
	private static String getFinalResultRandom(Point[][] matrix, int maxSelectTimes) {

		//convert to image type
		ImageMap image = new ImageMap(matrix.length, matrix[0].length);
		for(int i =0 ; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				image.getPiece(i, j).setExpectedPosition(matrix[i][j].x, matrix[i][j].y);
			}
		}
		
		//result
		PathList result = new PathList();
		return generateAnswer(result); 
	}
	
	private static String generateAnswer(PathList paths) {
		StringBuilder answer = new StringBuilder();
		answer.append(paths.size() + "\n");
		for(int i = 0; i < paths.size(); i++) {
			answer.append(Integer.toHexString(paths.get(i).getStartCol()));
			answer.append(Integer.toHexString(paths.get(i).getStartRow()));
			answer.append("\n");
			answer.append(paths.get(i).size() + "\n");
			
			for(int j = 0; j < paths.get(i).size(); j++) {
				answer.append(paths.get(i).get(j));
			}
			answer.append("\n");
		}
		
		return answer.toString();
	}
	
	private static Point[][] generate(int numOfRow, int numOfColumn) {
		Point[][] matrix = new Point[numOfRow][numOfColumn];
		
		int n = numOfRow*numOfColumn;
		int[] code = new int[n];
		
		for(int i = 0; i < n; i++) {
			code[i] = i;
		}
		
		for(int i = 0; i < n*n; i++) {
			int ran1 = (int) (Math.random()*n);
			int ran2 = (int) (Math.random()*n);
			int temp = code[ran1];
			code[ran1] = code[ran2];
			code[ran2] = temp;
		}
		
		
		for(int i = 0; i < code.length; i++) {
			matrix[i/numOfColumn][i%numOfColumn] = new Point();
			matrix[i/numOfColumn][i%numOfColumn].setLocation(code[i]/numOfColumn, code[i]%numOfColumn);
		}
		
		return matrix;
	}
}
	

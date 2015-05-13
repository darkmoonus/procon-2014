package Sources;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Objects.Image;

public class ProconFormat {
	public static String imageType;
	public static Dimension resolution;
	public static Dimension division;
	public static int selectionCost;
	public static int swappingCost;
	public static int selectionLimited;
	public static int maxPixelValue;	
	public static int headerSize;
	public static Image image;
	
	public ProconFormat(String inputFileName, Image image) throws IOException {
		imageType = "P6";
		resolution = new Dimension(0, 0);
		division = new Dimension(0, 0);
		selectionCost = 0;
		swappingCost = 0;
		selectionLimited = 0;
		maxPixelValue = 0;
		headerSize = 0;
		setValueFrom(inputFileName);
		ProconFormat.image = image;
	}
	@SuppressWarnings("resource")
	public static void setValueFrom(String inputFileName) throws IOException{
		String line = null;
		BufferedReader reader = new BufferedReader(new FileReader(new File(inputFileName)));
		for(int i=0; i<6;i++) {
			if((line = reader.readLine()) != null) {
				headerSize += line.length() + 1;
				switch(i) {
				case 0:
					ProconFormat.imageType = line;
					break;
				case 1:
					String[] parts = line.split(" ");
					ProconFormat.division.width = Integer.parseInt(parts[1]);
					ProconFormat.division.height = Integer.parseInt(parts[2]);
					break;
				case 2:
					String[] parts2 = line.split(" ");
					ProconFormat.selectionLimited = Integer.parseInt(parts2[1]);
					break;
				case 3:
					String[] parts3 = line.split(" ");
					ProconFormat.selectionCost = Integer.parseInt(parts3[1]);
					ProconFormat.swappingCost = Integer.parseInt(parts3[2]);
					break;
				case 4:
					String[] parts4 = line.split(" ");
					ProconFormat.resolution.width = Integer.parseInt(parts4[0]);
					ProconFormat.resolution.height = Integer.parseInt(parts4[1]);
					break;
				case 5:
					ProconFormat.maxPixelValue = Integer.parseInt(line);
					break;
				default:
					break;
				}
			} else {
				throw new IOException("ERROR: Reading file " + inputFileName + "!");
			}
		}
		reader.close();
	}

	public static String toFormatString() {
		return ProconFormat.imageType + "\n" + ProconFormat.division.width
				+ " " + ProconFormat.division.height + "\n"
				+ ProconFormat.selectionLimited + "\n" + ProconFormat.selectionCost + " "
				+ ProconFormat.swappingCost + "\n" + ProconFormat.resolution.width
				+ " " + ProconFormat.resolution.height + "\n"
				+ ProconFormat.maxPixelValue;
	}
}
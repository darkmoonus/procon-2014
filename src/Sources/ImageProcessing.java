package Sources;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Objects.Pixel;

public class ImageProcessing {
	
	// GET BINARY PPM INFORMATION
	@SuppressWarnings("resource")
	public static int getHeaderSizeOfPPM(String inputFileName) throws IOException {
		String line = null;
		int headerSize = 0;
		BufferedReader reader = new BufferedReader(new FileReader(new File(inputFileName)));
		for(int i=0; i<6;i++) {
			if((line = reader.readLine()) != null) {
				headerSize += line.length() + 1;
			} else {
				throw new IOException("ERROR: Reading file " + inputFileName + "!");
			}
		}
		reader.close();
		return headerSize;
	}
	public static String getTypeOfPPMImage(String inputFileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(inputFileName)));
		String line = null;
		if ((line = reader.readLine()) != null) {
			reader.close();
			return line;
		}
		reader.close();
		return null;
	}
	public static Dimension getDivisionPPMImage(String inputFileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(inputFileName)));
		String line = null;
		line = reader.readLine();
		if((line = reader.readLine()) != null) {
			String[] parts = line.split(" ");
			int width = Integer.parseInt(parts[1]);
			int height = Integer.parseInt(parts[2]);
			reader.close();
			return new Dimension(width, height);
		}
		reader.close();
		return null;
	}
	public static Dimension getResolutionOfPPMImage(String inputFileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(inputFileName)));
		String line = null;
		line = reader.readLine();
		line = reader.readLine();
		line = reader.readLine();
		line = reader.readLine();
		if((line = reader.readLine()) != null) {
			String[] parts = line.split(" ");
			int width = Integer.parseInt(parts[0]);
			int height = Integer.parseInt(parts[1]);
			reader.close();
			return new Dimension(width, height);
		}
		reader.close();
		return null;
	}
	
	// IMAGEICON
	public static ImageIcon resizeImageFromPPM(String inputFileName, Dimension resolution) throws IOException {
		ImageIcon icon = ImageProcessing.getImageIconFromBinaryPPM(inputFileName);
    	Image scaleImage = icon.getImage().getScaledInstance(resolution.width, resolution.height, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaleImage);
	}
	public static ImageIcon resizeImageFromImageIcon(ImageIcon icon, Dimension resolution) throws IOException {
    	Image scaleImage = icon.getImage().getScaledInstance(resolution.width, resolution.height, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaleImage);
	}
	public static ImageIcon resizeImageFromPNG(String inputFileName, Dimension resolution) throws IOException {
		ImageIcon icon = new ImageIcon(inputFileName);
    	Image scaleImage = icon.getImage().getScaledInstance(resolution.width, resolution.height, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaleImage);
	}
	public static ImageIcon getImageIconFromBinaryPPM(String inputFileName) throws IOException {
		return getImageIconFromPixelsMatrix(getPixelMatrixFromPPMBinary(inputFileName), getResolutionOfPPMImage(inputFileName));
	}
	public static ImageIcon getImageIconFromPixelsMatrix(Pixel[][] pixelsMatrix, Dimension resolution) throws IOException {
		BufferedImage image = new BufferedImage(resolution.width, resolution.height, BufferedImage.TYPE_INT_RGB);
		WritableRaster matrix = image.getRaster();
		for (int h=0; h<resolution.height; h++)
			for (int w=0; w<resolution.width; w++) {
				matrix.setPixel(w, h, new int[] { 
					pixelsMatrix[h][w].getRed(), pixelsMatrix[h][w].getGreen(), pixelsMatrix[h][w].getBlue() 
				});
			}
		return new ImageIcon(image);
	}	
	
	public static Pixel[][] getPixelMatrixFromPPMBinary(String inputFileName) throws IOException {
		Pixel[][] pixelsMatrix = new Pixel[1024][1024];
		byte[] bytes = readBinaryFile(inputFileName);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.order(ByteOrder.LITTLE_ENDIAN); 
		int widthResolution = getResolutionOfPPMImage(inputFileName).width;
		int count = 0;
		for(int i=getHeaderSizeOfPPM(inputFileName); i<bytes.length; i+=3) {
			if(i+2 < bytes.length) {
				pixelsMatrix[count / widthResolution][count % widthResolution] = new Pixel(bytes[i] & 0xFF, bytes[i+1] & 0xFF, bytes[i+2] & 0xFF);
			}
			count++;
		}
		return pixelsMatrix;
	}
	
	// IMAGE CONVERTING
	public static void convertPixelsMatrixToPNG(Pixel[][] pixelsMatrix, Dimension resolution, String outputFileName) throws IOException {
		BufferedImage image = new BufferedImage(resolution.width, resolution.height, BufferedImage.TYPE_INT_RGB);
		WritableRaster matrix = image.getRaster();
		for (int h=0; h<resolution.height; h++)
			for (int w=0; w<resolution.width; w++) {
				matrix.setPixel(w, h, new int[] { 
					pixelsMatrix[h][w].getRed(), pixelsMatrix[h][w].getGreen(), pixelsMatrix[h][w].getBlue() 
				});
			}
		ImageIO.write(image, "PNG", new File(outputFileName));
		
	}	
	public static void convertBinaryPPMToPNG(String inputFileName, String outputFileName) throws IOException {
		convertPixelsMatrixToPNG(getPixelMatrixFromPPMBinary(inputFileName), getResolutionOfPPMImage(inputFileName), outputFileName);
	}
	
//	public static void convertBinaryPPMToASCIIPPM(String inputFileName, String outputFileName) throws IOException {
//		FileWriter writer = new FileWriter(outputFileName);	
//		writer.write("P3\n# " + ProconFormat.pieceDivision.width + " "
//				+ ProconFormat.pieceDivision.height + "\n# "
//				+ ProconFormat.selectionLimited + "\n# " + ProconFormat.selectionCost + " "
//				+ ProconFormat.swappingCost + "\n" + ProconFormat.imageResolution.width
//				+ " " + ProconFormat.imageResolution.height + "\n"
//				+ ProconFormat.maxPixelValue + "\n");
//			
//		byte[] bytes = readBinaryFile(inputFileName);
//		ByteBuffer buffer = ByteBuffer.wrap(bytes);
//		buffer.order(ByteOrder.LITTLE_ENDIAN); 
//		for(int i=ProconFormat.headerSize; i<bytes.length; i++) {
//			writer.write((bytes[i] & 0xFF) + "\n");
//		}
//		writer.close();	
//	}
	public static void convertPixelsMatrixToBinaryPPM(Pixel[][] pixelsMatrix, Dimension imageResolution, Dimension pieceDivision, int selectionCost, int swappingCost, int selectionLimited, int maxPixelValue, String outputFileName ) throws IOException {
//		FileWriter writer = new FileWriter(outputFileName);	
//		writer.write("P6\n# " + pieceDivision.width + " "
//				+ pieceDivision.height + "\n# "
//				+ selectionLimited + "\n# " + selectionCost + " "
//				+ swappingCost + "\n" + imageResolution.width
//				+ " " + imageResolution.height + "\n"
//				+ maxPixelValue + "\n");
			
//		String s = "";
//		System.out.println("Processing...");
//		for(int i=0; i<imageResolution.height; i++) {
//			for(int j=0; j<imageResolution.width; j++) {
//				s += pixelsMatrix[i][j].getRed() + " " + pixelsMatrix[i][j].getGreen() + " " + pixelsMatrix[i][j].getBlue();
//				System.out.println(i + " " + j);
//			}
//		}
//		System.out.println(s);
		
		
//		String s = "123 657 345 250";
//		byte[] bytes = s.getBytes() ;
//		for(int i=0; i<bytes.length; i++) {
//			System.out.println(bytes[i] & 0xFF);
//		}
//
//		writeBinaryFile(bytes, outputFileName);
//		
//		boolean append = false;
//		ByteBuffer buf = ByteBuffer.allocateDirect(s.length());
//		buf.put(s.getBytes());
//		FileChannel channel = new FileOutputStream("src/xxx.ppm", append).getChannel();
//		buf.flip();
//		channel.write(buf);
//		channel.close();
//		writer.close();	
	}
	public static byte[] readBinaryFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		return Files.readAllBytes(path);
	}
	public static void writeBinaryFile(byte[] aBytes, String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		Files.write(path, aBytes); 
	}
	
	
}

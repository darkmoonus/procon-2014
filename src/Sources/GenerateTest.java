package Sources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Interfaces.Configure;
import Interfaces.CustomButton;
import Interfaces.CustomFont;
import Interfaces.HintTextField;
import Interfaces.SlideImagesHorizontal;
import Objects.Image;
import Objects.Pixel;

public class GenerateTest extends JPanel implements Serializable {
	private static final long serialVersionUID = -1856528498629961001L;
	private CustomButton generateTestOpenFile;
	private CustomButton generateTestOriginalImage;
	private CustomButton clear;
	private CustomButton info;
	private HintTextField nameSeries;
	private HintTextField divisionWidth;
	private HintTextField divisionHeight;
	private HintTextField quantity;
	private CustomButton generate;
	private CustomButton viewGeneratedImage;
	private CustomButton viewOriginalImage;
	private CustomButton viewImageResolution;
	private CustomButton generatedBack;
	private CustomButton next;
	private CustomButton prev;
	private Color backGround;
	private Point pos;
	private Dimension dimension;
	private LayoutManager layoutManager;
	private JPanel parentPanel;
	private SlideImagesHorizontal imageSlideView;
	
	private String originalImageURL = "";
	
	public GenerateTest(JPanel parentPanel, Color backGround, Point pos, Dimension dimension, LayoutManager layout) throws IOException {
		setVisible(false);
		setParentPanel(parentPanel);
		parentPanel.add(GenerateTest.this);
		setPos(pos);
		setDimension(dimension);
		setLayout(layout);
		setBackGround(backGround);
		setBackground(backGround);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		
		setGenerateTestOpenFile(new CustomButton("Open original ppm image", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 23), false, false, Color.WHITE, true, new Point(0, Configure.SCREEN_HEIGHT/2 - 30), new Dimension(Configure.SCREEN_WIDTH-Configure.LEFT_PANEL_WIDTH - 10, 40), this));
		setGenerateTestOriginalImage(new CustomButton(new ImageIcon("src/none.png"), "", null, null, false, false, null, false, new Point(5, 5), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 20, Configure.SCREEN_HEIGHT - 65), this, SwingConstants.CENTER, SwingConstants.CENTER));
		setClear(new CustomButton("Clear", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, true, new Point(5, Configure.SCREEN_HEIGHT - 55), new Dimension(70, 30), this));
		setInfo(new CustomButton(Configure.STRING_INFO, Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.WHITE, true, new Point(80, Configure.SCREEN_HEIGHT - 55), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 70 - 110 - 115 - 75 - 100 - 45 - 105, 30), this));
		setViewOriginalImage(new CustomButton(new ImageIcon("src/none.png"), "", null, null, false, false, null, true, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 5), new Dimension(205, 150), this, SwingConstants.CENTER, SwingConstants.CENTER));
		setViewImageResolution(new CustomButton("", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 13), false, false, Color.WHITE, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 160), new Dimension(205, 20), GenerateTest.this));
		setgeneratedBack(new CustomButton("Back", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 300), new Dimension(80, 30), GenerateTest.this));
		setNameSeries(new HintTextField(" Name series", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 - 80 - 120 - 115 - 105, Configure.SCREEN_HEIGHT - 55), new Dimension(100, 30), this, false));
		setDivisionWidth(new HintTextField(" Division width", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 - 80 - 120 - 115, Configure.SCREEN_HEIGHT - 55), new Dimension(110, 30), this, false));
		setDivisionHeight(new HintTextField(" Division height", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 - 80 - 120, Configure.SCREEN_HEIGHT - 55), new Dimension(115, 30), this, false));
		setQuantity(new HintTextField(" Quantity", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 - 80, Configure.SCREEN_HEIGHT - 55), new Dimension(75, 30), this, false));
		setGenerate(new CustomButton("Generate", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, true, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115, Configure.SCREEN_HEIGHT - 55), new Dimension(100, 30), this));
		
		
		generatedBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generate.setVisible(true);
				imageSlideView.setVisible(false);
				generateTestOpenFile.setVisible(false);
				quantity.setVisible(true);
				generateTestOriginalImage.setVisible(true);
				divisionHeight.setVisible(true);
				divisionWidth.setVisible(true);
				info.setVisible(true);
				clear.setVisible(true);
				nameSeries.setVisible(true);
				viewImageResolution.setVisible(false);
				generatedBack.setVisible(false);
				viewGeneratedImage.setVisible(false);
			}
		});
		
		generate.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				int divisionWidthInt = Integer.parseInt(divisionWidth.getText().toString());
				int divisionHeightInt = Integer.parseInt(divisionHeight.getText().toString());
				int quantityNumber = Integer.parseInt(quantity.getText().toString());
				String imageName = nameSeries.getText().toString();
				generate.setVisible(false);
				generateTestOpenFile.setVisible(false);
				quantity.setVisible(false);
				generateTestOriginalImage.setVisible(false);
				divisionHeight.setVisible(false);
				divisionWidth.setVisible(false);
				info.setVisible(false);
				clear.setVisible(false);
				nameSeries.setVisible(false);
				viewImageResolution.setVisible(true);
				generatedBack.setVisible(true);
				try {
					viewOriginalImage.setIcon(ImageProcessing.resizeImageFromPPM(originalImageURL, new Dimension(205, 150)));
					Pixel[][] pixelsMatrix = ImageProcessing.getPixelMatrixFromPPMBinary(originalImageURL);
					Dimension division = ImageProcessing.getDivisionPPMImage(originalImageURL);
					Dimension resolution = ImageProcessing.getResolutionOfPPMImage(originalImageURL);
					Image image = new Image(pixelsMatrix, division, resolution);
					ArrayList<String> imageURL = new ArrayList<String>();
					for(int i=0; i<quantityNumber; i++) {
						imageURL.add(image.generateTest("src/Test/Generated/" + imageName + i + ".png", resolution, new Dimension(divisionWidthInt, divisionHeightInt)));
					}
					setViewGeneratedImage(new CustomButton(ImageProcessing.resizeImageFromPNG("src/Test/Generated/" + imageName + 0 + ".png", new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170)), "", null, null, false, false, null, true, new Point(5, 5), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170), GenerateTest.this, SwingConstants.CENTER, SwingConstants.CENTER));
					
					viewImageResolution.setText("Resolution : ( " + resolution.width + " x " + resolution.height + " )");
					imageSlideView = new SlideImagesHorizontal("PNG", imageURL, viewGeneratedImage, GenerateTest.this, Color.WHITE, new Point(5, Configure.SCREEN_HEIGHT - 160), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH-20, 153), 200, null);
					imageSlideView.finishAddComponents();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			    
			}
		});
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				divisionHeight.setText(" Division height");
				divisionWidth.setText(" Division width");
				quantity.setText(" Quantity");
				nameSeries.setText(" Name series");
				generateTestOpenFile.setVisible(true);
				generateTestOriginalImage.setVisible(false);
			}
		});
		generateTestOpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser fileChooser = new JFileChooser();
            	File file = new File("src/");
            	fileChooser.setCurrentDirectory(file);
				fileChooser.setDialogTitle("Open image");   
				int userSelection = fileChooser.showOpenDialog(GenerateTest.this);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileChosen = fileChooser.getSelectedFile();
				    originalImageURL = fileChosen.getAbsolutePath();	    
				    try {
				    	ImageIcon image = ImageProcessing.resizeImageFromPPM(originalImageURL, new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 20, Configure.SCREEN_HEIGHT - 65));
				    	generateTestOriginalImage.setIcon(image);
					    generateTestOriginalImage.setVisible(true);
					    generateTestOpenFile.setVisible(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
	}
	public CustomButton getGenerateTestOpenFile() {
		return generateTestOpenFile;
	}
	public void setGenerateTestOpenFile(CustomButton generateTestOpenFile) {
		this.generateTestOpenFile = generateTestOpenFile;
	}
	public CustomButton getGenerateTestOriginalImage() {
		return generateTestOriginalImage;
	}
	public void setGenerateTestOriginalImage(CustomButton generateTestOriginalImage) {
		this.generateTestOriginalImage = generateTestOriginalImage;
	}
	public CustomButton getClear() {
		return clear;
	}
	public void setClear(CustomButton clear) {
		this.clear = clear;
	}
	public CustomButton getInfo() {
		return info;
	}
	public void setInfo(CustomButton info) {
		this.info = info;
	}
	public HintTextField getNameSeries() {
		return nameSeries;
	}
	public void setNameSeries(HintTextField nameSeries) {
		this.nameSeries = nameSeries;
	}
	public HintTextField getDivisionWidth() {
		return divisionWidth;
	}
	public void setDivisionWidth(HintTextField divisionWidth) {
		this.divisionWidth = divisionWidth;
	}
	public HintTextField getDivisionHeight() {
		return divisionHeight;
	}
	public void setDivisionHeight(HintTextField divisionHeight) {
		this.divisionHeight = divisionHeight;
	}
	public HintTextField getQuantity() {
		return quantity;
	}
	public void setQuantity(HintTextField quantity) {
		this.quantity = quantity;
	}
	public CustomButton getGenerate() {
		return generate;
	}
	public void setGenerate(CustomButton generate) {
		this.generate = generate;
	}
	public CustomButton getViewGeneratedImage() {
		return viewGeneratedImage;
	}
	public void setViewGeneratedImage(CustomButton viewGeneratedImage) {
		this.viewGeneratedImage = viewGeneratedImage;
	}
	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public Dimension getDimension() {
		return dimension;
	}
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	public LayoutManager getLayoutManager() {
		return layoutManager;
	}
	public void setLayoutManager(LayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}
	public Color getBackGround() {
		return backGround;
	}
	public void setBackGround(Color backGround) {
		this.backGround = backGround;
	}
	public JPanel getParentPanel() {
		return parentPanel;
	}
	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	public CustomButton getViewOriginalImage() {
		return viewOriginalImage;
	}
	public void setViewOriginalImage(CustomButton viewOriginalImage) {
		this.viewOriginalImage = viewOriginalImage;
	}
	public CustomButton getViewImageResolution() {
		return viewImageResolution;
	}
	public void setViewImageResolution(CustomButton viewImageResolution) {
		this.viewImageResolution = viewImageResolution;
	}
	public CustomButton getgeneratedBack() {
		return generatedBack;
	}
	public void setgeneratedBack(CustomButton generatedBack) {
		this.generatedBack = generatedBack;
	}
	public  CustomButton getNext() {
		return next;
	}
	public void setNext(CustomButton next) {
		this.next = next;
	}
	public CustomButton getPrev() {
		return prev;
	}
	public void setPrev(CustomButton prev) {
		this.prev = prev;
	}
}

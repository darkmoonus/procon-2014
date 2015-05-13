package Sources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Decoder.ImageRestorer;
import Decoder.ResultViewer;
import Interfaces.Configure;
import Interfaces.CustomButton;
import Interfaces.CustomCheckbox;
import Interfaces.CustomFont;
import Interfaces.CustomLabel;
import Interfaces.HintTextField;
import Interfaces.SlideImagesHorizontal;
import Objects.Image;
import Objects.ImageDecodedResult;
import Objects.Pixel;

public class ImageDecoder extends JPanel implements Serializable {
	
	class FinalDecodingWaiting extends Thread {
		public FinalDecodingWaiting() {
		}
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			super.run();
			viewProgressFinalResult.setVisible(true);
			viewMatrixResult.setVisible(false);
			viewImageInfo.setVisible(false);
			
			ResultViewer resultViewer = new ResultViewer();
			
			if(fullViewControl != null) fullViewControl.setVisible(false);
			Point[][] matrixResult = imageResult.get(imageSlideView.getChosenIndex()).posArr;
			String s = ImageRestorer.getFinalResult(matrixResult,
					ProconFormat.selectionLimited, chooseAlgorithmNoHeuristic.isSelected(), 
					chooseAlgorithmSimpleHeuristic.isSelected(), 
					chooseAlgorithmIDAStarHeuristic.isSelected(), 
					Integer.parseInt(chooseAlgorithmNoHeuristicText.getText().toString()), 
					Integer.parseInt(chooseAlgorithmSimpleHeuristicText.getText().toString()), 
					Integer.parseInt(chooseAlgorithmIDAStarHeuristicText.getText().toString()), 
					resultViewer);

			StringSelection stringSelection = new StringSelection (s);
			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents (stringSelection, null);
			
			viewDecodeResultInfo.setText("CO: " + resultViewer.getCorrect()
					+ " SE: " + resultViewer.getSelection() + " SW: "
					+ resultViewer.getSwap() + " SU: " + resultViewer.getSum());

			viewMatrixResult.setText(s);
			viewProgressFinalResult.setVisible(false);
			viewMatrixResult.setVisible(true);
			viewDecodeResultInfo.setVisible(true);
			
			this.stop();
		}
	}
	
	class FullViewControl extends JFrame {
		private static final long serialVersionUID = -5727656756883855482L;
		private Dimension dimension = new Dimension(Configure.SCREEN_WIDTH - 100, Configure.SCREEN_HEIGHT - 300);
		private Point position = new Point();
		private SlideImagesHorizontal imageSlideView;
		public FullViewControl() throws IOException {
			super();
			setUndecorated(true);
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			setBounds((screenSize.width-dimension.width)/2,(screenSize.height- dimension.height)/2, dimension.width, dimension.height);
			position.x = (screenSize.width-dimension.width)/2;
			position.y = (screenSize.height-dimension.height)/2;
			setSize(new java.awt.Dimension(this.dimension.width, this.dimension.height));
			setPreferredSize(new java.awt.Dimension(this.dimension.width, this.dimension.height));
			setBackground(Color.GRAY);
			JPanel mainPanel = new JPanel();
			mainPanel.setBackground(Color.WHITE);
			mainPanel.setSize(new Dimension(dimension.width, dimension.height));
			mainPanel.setLayout(null);
			imageSlideView = new SlideImagesHorizontal(viewMatrixResult, imageResult, viewDecodedImage, mainPanel, Color.WHITE, new Point(5, 5), new Dimension(this.dimension.width - 10, this.dimension.height + 7), this.dimension.height / 2 * 3, null);
			imageSlideView.finishAddComponents();
			this.add(mainPanel);
			mainPanel.setVisible(true);
			this.setVisible(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	class DecodingWaiting extends Thread {
		public DecodingWaiting() {
			super();
		}
		
		@Override
		public void run() {
			super.run();
			
			chooseAlgorithmIDAStarHeuristic.setVisible(true);
			chooseAlgorithmIDAStarHeuristicText.setVisible(true);
			chooseAlgorithmNoHeuristic.setVisible(true);
			chooseAlgorithmNoHeuristicText.setVisible(true);
			chooseAlgorithmSimpleHeuristic.setVisible(true);
			chooseAlgorithmSimpleHeuristicText.setVisible(true);
			
			openImageInfoText.setVisible(false);
			decodeProcessingProgressBar.setVisible(true);
			decodeProcessingPercent.setVisible(true);
			decodeProcessingTime.setVisible(true);
			clear.setVisible(false);
			decode.setVisible(false);
			try {
				imageResult = ProconFormat.image.getDecodedImageIcon(decodeProcessingTime, decodeProcessingPercent);
				setViewDecodedImage(new CustomButton(ImageProcessing.resizeImageFromImageIcon(imageResult.get(0).icon, new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170)), "", null, null, false, false, null, true, new Point(5, 5), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170), ImageDecoder.this, SwingConstants.CENTER, SwingConstants.CENTER));
				
				viewMatrixResult.setText(imageResult.get(0).getPosArrToString());
				
				imageSlideView = new SlideImagesHorizontal(viewMatrixResult, imageResult, viewDecodedImage, ImageDecoder.this, Color.WHITE, new Point(5, Configure.SCREEN_HEIGHT - 160), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH-20, 153), 200, null);
				imageSlideView.finishAddComponents();
				viewMatrixResult.setVisible(true);
				viewOriginalImage.setIcon(ImageProcessing.resizeImageFromImageIcon(ProconFormat.image.getImageIcon(), new Dimension(205, 150)));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			decodeProcessingProgressBar.setVisible(false);
			decodeProcessingPercent.setVisible(false);
	    	decodeProcessingTime.setVisible(false);
	    	
	    	viewImageInfo.setVisible(true);
	    	viewMatrixResult.setVisible(false);
			viewOriginalImage.setVisible(true);
			
			viewImageInfo.setText("  Resolution : ( " + ProconFormat.resolution.width + " x " + ProconFormat.resolution.height + " )\n-----------------------------\n" +
					"  Image division : ( " + ProconFormat.division.width + " x " + ProconFormat.division.height + " )\n-----------------------------\n" +
					"  Selection limited : " + ProconFormat.selectionLimited + "\n-----------------------------\n" + 
					"  Selection cost : " + ProconFormat.selectionCost + "\n-----------------------------\n" + 
					"  Swapping cost : " + ProconFormat.swappingCost + "\n-----------------------------\n");
	    	
	    	imageDecoderOpenFile.setVisible(false);
	    	decodeOriginalImage.setVisible(false);
	    	openImageInfoText.setVisible(false);
	    	imageSlideView.setVisible(true);
	    	
	    	back.setVisible(true);
	    	start.setVisible(true);
	    	next.setVisible(true);
	    	prev.setVisible(true);
	    	reload.setVisible(true);
	    	fullView.setVisible(true);
	    	reloadFull.setVisible(true);
	    	reloadQuick.setVisible(true);
			
	    	this.stop();
		}
	}
	
	private static final long serialVersionUID = -9216527755344439892L;
	private CustomButton imageDecoderOpenFile;
	private CustomButton decodeOriginalImage;
	private JPanel parentPanel;
	private Color backGround;
	private Point pos;
	private Dimension dimension;
	private LayoutManager layoutManager;
	
	private CustomButton decode;
	private CustomButton clear;
	private SlideImagesHorizontal imageSlideView;
	private CustomButton openImageInfoText;
	
	private CustomButton decodeProcessingProgressBar;
	private CustomButton decodeProcessingTime;
	private CustomButton decodeProcessingPercent;
	
	private String originalImageURL = "";
	
	private CustomButton back;
	private CustomButton refresh;
	private CustomButton start;
	private CustomButton next;
	private CustomButton prev;
	private CustomButton reload;
	private CustomButton fullView;
	private CustomCheckbox reloadFull;
	private CustomCheckbox reloadQuick;
	
	private CustomCheckbox chooseAlgorithmNoHeuristic;
	private CustomCheckbox chooseAlgorithmSimpleHeuristic;
	private CustomCheckbox chooseAlgorithmIDAStarHeuristic;
	private HintTextField chooseAlgorithmNoHeuristicText;
	private HintTextField chooseAlgorithmSimpleHeuristicText;
	private HintTextField chooseAlgorithmIDAStarHeuristicText;
	
	public synchronized HintTextField getChooseAlgorithmNoHeuristicText() {
		return chooseAlgorithmNoHeuristicText;
	}
	public synchronized void setChooseAlgorithmNoHeuristicText(
			HintTextField chooseAlgorithmNoHeuristicText) {
		this.chooseAlgorithmNoHeuristicText = chooseAlgorithmNoHeuristicText;
	}
	public synchronized HintTextField getChooseAlgorithmSimpleHeuristicText() {
		return chooseAlgorithmSimpleHeuristicText;
	}
	public synchronized void setChooseAlgorithmSimpleHeuristicText(
			HintTextField chooseAlgorithmSimpleHeuristicText) {
		this.chooseAlgorithmSimpleHeuristicText = chooseAlgorithmSimpleHeuristicText;
	}
	public synchronized HintTextField getChooseAlgorithmIDAStarHeuristicText() {
		return chooseAlgorithmIDAStarHeuristicText;
	}
	public synchronized void setChooseAlgorithmIDAStarHeuristicText(
			HintTextField chooseAlgorithmIDAStarHeuristicText) {
		this.chooseAlgorithmIDAStarHeuristicText = chooseAlgorithmIDAStarHeuristicText;
	}
	
	
	public CustomCheckbox getChooseAlgorithmNoHeuristic() {
		return chooseAlgorithmNoHeuristic;
	}
	public void setChooseAlgorithmNoHeuristic(
			CustomCheckbox chooseAlgorithmNoHeuristic) {
		this.chooseAlgorithmNoHeuristic = chooseAlgorithmNoHeuristic;
	}
	public CustomCheckbox getChooseAlgorithmSimpleHeuristic() {
		return chooseAlgorithmSimpleHeuristic;
	}
	public void setChooseAlgorithmSimpleHeuristic(
			CustomCheckbox chooseAlgorithmSimpleHeuristic) {
		this.chooseAlgorithmSimpleHeuristic = chooseAlgorithmSimpleHeuristic;
	}
	public CustomCheckbox getChooseAlgorithmIDAStarHeuristic() {
		return chooseAlgorithmIDAStarHeuristic;
	}
	public void setChooseAlgorithmIDAStarHeuristic(
			CustomCheckbox chooseAlgorithmIDAStarHeuristic) {
		this.chooseAlgorithmIDAStarHeuristic = chooseAlgorithmIDAStarHeuristic;
	}
	private FullViewControl fullViewControl;
	
	private CustomButton viewDecodedImage;
	private CustomButton viewOriginalImage;
	private JTextArea viewImageInfo;
	private TextArea viewMatrixResult;
	private CustomButton viewProgressFinalResult;
	private CustomLabel viewDecodeResultInfo;
	
	private ArrayList<ImageDecodedResult> imageResult;
	
	public ImageDecoder(JPanel parentPanel, Color backGround, Point pos, Dimension dimension, LayoutManager layoutManager) throws IOException {
		setVisible(true);
		setBackGround(backGround);
		setParentPanel(parentPanel);
		parentPanel.add(ImageDecoder.this);
		setPos(pos);
		setDimension(dimension);
		setBackground(backGround);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		setLayout(layoutManager);
		
		setViewImageInfo(new JTextArea());
		viewImageInfo.setFont(CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15));
		viewImageInfo.setBackground(Color.WHITE);
		viewImageInfo.setForeground(Color.GRAY);
		viewImageInfo.setBounds(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 160, 205, Configure.SCREEN_HEIGHT - 470);
		viewImageInfo.setVisible(false);
		viewImageInfo.setEditable(false);
		this.add(viewImageInfo);
		
		setViewOriginalImage(new CustomButton(new ImageIcon("src/none.png"), "", null, null, false, false, null, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 5), new Dimension(205, 150), this, SwingConstants.CENTER, SwingConstants.CENTER));
		
		setViewDecodeResultInfo(new CustomLabel("", Color.GRAY, Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15),new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 160), new Dimension(205, 30), false, SwingConstants.CENTER, SwingConstants.CENTER, ImageDecoder.this));
		
		setViewMatrixResult(new TextArea());
		viewMatrixResult.setBackground(Color.WHITE);
		viewMatrixResult.setForeground(Color.GRAY);
		viewMatrixResult.setFont(CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 12));
		viewMatrixResult.setBounds(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 195, 205, Configure.SCREEN_HEIGHT - 470);
		viewMatrixResult.setVisible(false);
		viewMatrixResult.setEditable(false);
		
		setViewProgressFinalResult(new CustomButton(new ImageIcon("src/Resources/metro.bin"), " Processing ...", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 16), false, false, null, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, 160), new Dimension(205, Configure.SCREEN_HEIGHT - 470), ImageDecoder.this, SwingConstants.CENTER, SwingConstants.CENTER));
		
		ImageDecoder.this.add(viewMatrixResult);
		setDecode(new CustomButton("Start now", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, true, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115, Configure.SCREEN_HEIGHT - 55), new Dimension(100, 30), this));
		setClear(new CustomButton("Clear", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, true, new Point(5, Configure.SCREEN_HEIGHT - 55), new Dimension(70, 30), this));
		setOpenImageInfoText(new CustomButton("", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.WHITE, true, new Point(80, Configure.SCREEN_HEIGHT - 55), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 - 85, 30), this));
		
		setDecodeProcessingPercent(new CustomButton("", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.WHITE, false, new Point(5, Configure.SCREEN_HEIGHT - 55), new Dimension(155, 30), this));
		setDecodeProcessingTime(new CustomButton("", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.WHITE, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 120, Configure.SCREEN_HEIGHT - 55), new Dimension(115, 30), this));
		setDecodeProcessingProgressBar(new CustomButton(new ImageIcon("src/Resources/progress.bin"), "", null, null, false, false, null, false, new Point(160, Configure.SCREEN_HEIGHT - 50), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 - 85 - 130, 30), this, SwingConstants.CENTER, SwingConstants.CENTER));
		decodeProcessingProgressBar.setEnabled(false);
		
		setDecodeOriginalImage(new CustomButton(new ImageIcon("src/none.png"), "", null, null, false, false, null, false, new Point(5, 5), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 20, Configure.SCREEN_HEIGHT - 65), this, SwingConstants.CENTER, SwingConstants.CENTER));
		
		setReload(new CustomButton("Reload", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115, Configure.SCREEN_HEIGHT - 270), new Dimension(100, 40), this));
		setReloadFull(new CustomCheckbox(" Full scan", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 14), null, false, false, false, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 5, Configure.SCREEN_HEIGHT - 245), new Dimension(90, 20), this));
		setReloadQuick(new CustomCheckbox(" Quick ", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 14), null, false, false, true, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 5, Configure.SCREEN_HEIGHT - 265), new Dimension(90, 20), this));
		
		setFullView(new CustomButton("+", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 67, Configure.SCREEN_HEIGHT - 305), new Dimension(50, 30), this));
		setBack(new CustomButton("<- B", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 15), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220, Configure.SCREEN_HEIGHT - 305), new Dimension(63, 30), this));
		setNext(new CustomButton(">", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 11), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 + 60, Configure.SCREEN_HEIGHT - 305), new Dimension(40, 30), this));
		setPrev(new CustomButton("<", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 11), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115 + 16, Configure.SCREEN_HEIGHT - 305), new Dimension(40, 30), this));
		
		setChooseAlgorithmIDAStarHeuristic(new CustomCheckbox("", null, null, false, false, true, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 5, Configure.SCREEN_HEIGHT - 185), new Dimension(20, 20), ImageDecoder.this));
		setChooseAlgorithmSimpleHeuristic(new CustomCheckbox("", null, null, false, false, true, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 5, Configure.SCREEN_HEIGHT - 205), new Dimension(20, 20), ImageDecoder.this));
		setChooseAlgorithmNoHeuristic(new CustomCheckbox("", null, null, false, false, true, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 5, Configure.SCREEN_HEIGHT - 225), new Dimension(20, 20), ImageDecoder.this));
		setChooseAlgorithmIDAStarHeuristicText(new HintTextField("1000", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 10), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 32, Configure.SCREEN_HEIGHT - 185 + 2), new Dimension(65, 18), ImageDecoder.this, false));
		setChooseAlgorithmSimpleHeuristicText(new HintTextField("500", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 10), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 32, Configure.SCREEN_HEIGHT - 205 + 2), new Dimension(65, 18), ImageDecoder.this, false));
		setChooseAlgorithmNoHeuristicText(new HintTextField("50000", CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 10), new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 220 + 32, Configure.SCREEN_HEIGHT - 225 + 2), new Dimension(65, 18), ImageDecoder.this, false));
		chooseAlgorithmIDAStarHeuristic.setVisible(false);
		chooseAlgorithmIDAStarHeuristicText.setVisible(false);
		chooseAlgorithmNoHeuristic.setVisible(false);
		chooseAlgorithmNoHeuristicText.setVisible(false);
		chooseAlgorithmSimpleHeuristic.setVisible(false);
		chooseAlgorithmSimpleHeuristicText.setVisible(false);
		
		setStart(new CustomButton("Start", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 26), false, false, Color.GRAY, false, new Point(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 115, Configure.SCREEN_HEIGHT - 225), new Dimension(100, 60), this));
		
		imageDecoderOpenFile = new CustomButton("Open original ppm image", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 23), false, false, Color.WHITE, true, new Point(0, Configure.SCREEN_HEIGHT/2 - 30), new Dimension(Configure.SCREEN_WIDTH-Configure.LEFT_PANEL_WIDTH - 10, 40), ImageDecoder.this);
		imageDecoderOpenFile.setVisible(true);
		
		reload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(reloadFull.isSelected()) {
					
				} else {
					
				}
			}
		});
		
		reloadFull.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reloadFull.setSelected(true);
				reloadQuick.setSelected(false);
			}
		});
		reloadQuick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				reloadFull.setSelected(false);
				reloadQuick.setSelected(true);
			}
		});
		
		fullView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					fullViewControl = new FullViewControl();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		next.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(imageSlideView.getChosenIndex() < imageSlideView.getImageList().size() - 1)
				imageSlideView.setChosenIndex(imageSlideView.getChosenIndex() + 1);
				try {
					viewDecodedImage.setIcon(ImageProcessing.resizeImageFromImageIcon(imageResult.get(imageSlideView.getChosenIndex()).icon, new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				viewMatrixResult.setText(imageResult.get(imageSlideView.getChosenIndex()).getPosArrToString());
			}
		});
		
		prev.addActionListener(new ActionListener()  {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(imageSlideView.getChosenIndex() > 0)
				imageSlideView.setChosenIndex(imageSlideView.getChosenIndex() - 1);
				try {
					viewDecodedImage.setIcon(ImageProcessing.resizeImageFromImageIcon(imageResult.get(imageSlideView.getChosenIndex()).icon, new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				viewMatrixResult.setText(imageResult.get(imageSlideView.getChosenIndex()).getPosArrToString());
			}
		});
		
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				new FinalDecodingWaiting().start();
				
			}
		});
		
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imageSlideView.setVisible(false);
				viewDecodedImage.setVisible(false);
				viewImageInfo.setVisible(false);
				viewOriginalImage.setVisible(false);
				viewMatrixResult.setVisible(false);
				viewImageInfo.setVisible(false);
				viewDecodeResultInfo.setVisible(false);
				
				back.setVisible(false);
				prev.setVisible(false);
				start.setVisible(false);
				next.setVisible(false);
				reload.setVisible(false);
				fullView.setVisible(false);
				reloadFull.setVisible(false);
		    	reloadQuick.setVisible(false);
				
		    	chooseAlgorithmIDAStarHeuristic.setVisible(false);
				chooseAlgorithmIDAStarHeuristicText.setVisible(false);
				chooseAlgorithmNoHeuristic.setVisible(false);
				chooseAlgorithmNoHeuristicText.setVisible(false);
				chooseAlgorithmSimpleHeuristic.setVisible(false);
				chooseAlgorithmSimpleHeuristicText.setVisible(false);
		    	
		    	decodeOriginalImage.setVisible(true);
				openImageInfoText.setVisible(true);
				decode.setVisible(true);
				clear.setVisible(true);
				
				
			}
		});
		
		decode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
		    	new DecodingWaiting().start();
			}
		});
		
		imageDecoderOpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser fileChooser = new JFileChooser();
            	File file = new File("src/");
            	fileChooser.setCurrentDirectory(file);
				fileChooser.setDialogTitle("Open image");   
				int userSelection = fileChooser.showOpenDialog(ImageDecoder.this);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileChosen = fileChooser.getSelectedFile();
				    setOriginalImageURL(fileChosen.getAbsolutePath());	
				    ImageIcon image;
					try {
						image = ImageProcessing.resizeImageFromPPM(originalImageURL, new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 20, Configure.SCREEN_HEIGHT - 65));
						decodeOriginalImage.setIcon(image);
					    imageDecoderOpenFile.setVisible(false);
					    decodeOriginalImage.setVisible(true);
					    
					    Pixel[][] pixelsMatrix = ImageProcessing.getPixelMatrixFromPPMBinary(originalImageURL);
						Dimension division = ImageProcessing.getDivisionPPMImage(originalImageURL);
						Dimension resolution = ImageProcessing.getResolutionOfPPMImage(originalImageURL);
						Image image2 = new Image(pixelsMatrix, division, resolution);
						new ProconFormat(originalImageURL, image2);
						
						openImageInfoText.setText("Division: " + "("
								+ ProconFormat.division.width + " x "
								+ ProconFormat.division.height + ")   -   "
								+ "Resolution: " + "("
								+ ProconFormat.resolution.width + " x "
								+ ProconFormat.resolution.height + ")   -   "
								+ "Limited: " + ProconFormat.selectionLimited
								+ "   -   " + "Selection cost: "
								+ ProconFormat.selectionCost + "   -   "
								+ "Swap cost: " + ProconFormat.swappingCost);
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decodeOriginalImage.setVisible(false);
				imageDecoderOpenFile.setVisible(true);
				openImageInfoText.setText("");
			}
		});
	}
	public JPanel getParentPanel() {
		return parentPanel;
	}
	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	public Color getBackGround() {
		return backGround;
	}
	public void setBackGround(Color backGround) {
		this.backGround = backGround;
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
	public String getOriginalImageURL() {
		return originalImageURL;
	}
	public void setOriginalImageURL(String originalImageURL) {
		this.originalImageURL = originalImageURL;
	}
	public CustomButton getViewDecodedImage() {
		return viewDecodedImage;
	}
	public void setViewDecodedImage(CustomButton viewDecodedImage) {
		this.viewDecodedImage = viewDecodedImage;
	}
	public CustomButton getViewOriginalImage() {
		return viewOriginalImage;
	}
	public void setViewOriginalImage(CustomButton viewOriginalImage) {
		this.viewOriginalImage = viewOriginalImage;
	}
	public CustomButton getClear() {
		return clear;
	}
	public void setClear(CustomButton clear) {
		this.clear = clear;
	}
	public CustomButton getDecode() {
		return decode;
	}
	public void setDecode(CustomButton decode) {
		this.decode = decode;
	}
	public CustomButton getDecodeOriginalImage() {
		return decodeOriginalImage;
	}
	public void setDecodeOriginalImage(CustomButton decodeOriginalImage) {
		this.decodeOriginalImage = decodeOriginalImage;
	}
	public SlideImagesHorizontal getImageSlideView() {
		return imageSlideView;
	}
	public void setImageSlideView(SlideImagesHorizontal imageSlideView) {
		this.imageSlideView = imageSlideView;
	}
	public CustomButton getImageDecoderOpenFile() {
		return imageDecoderOpenFile;
	}
	public void setImageDecoderOpenFile(CustomButton imageDecoderOpenFile) {
		this.imageDecoderOpenFile = imageDecoderOpenFile;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public CustomButton getOpenImageInfoText() {
		return openImageInfoText;
	}
	public void setOpenImageInfoText(CustomButton openImageInfoText) {
		this.openImageInfoText = openImageInfoText;
	}
	public CustomButton getBack() {
		return back;
	}
	public void setBack(CustomButton back) {
		this.back = back;
	}
	public CustomButton getRefresh() {
		return refresh;
	}
	public void setRefresh(CustomButton refresh) {
		this.refresh = refresh;
	}
	public CustomButton getStart() {
		return start;
	}
	public void setStart(CustomButton start) {
		this.start = start;
	}
	public TextArea getViewMatrixResult() {
		return viewMatrixResult;
	}
	public void setViewMatrixResult(TextArea viewMatrixResult) {
		this.viewMatrixResult = viewMatrixResult;
	}
	public ArrayList<ImageDecodedResult> getImageResult() {
		return imageResult;
	}
	public void setImageResult(ArrayList<ImageDecodedResult> imageResult) {
		this.imageResult = imageResult;
	}
	public CustomButton getDecodeProcessingProgressBar() {
		return decodeProcessingProgressBar;
	}
	public void setDecodeProcessingProgressBar(CustomButton decodeProcessingProgressBar) {
		this.decodeProcessingProgressBar = decodeProcessingProgressBar;
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
	public CustomButton getNext() {
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
	public CustomButton getReload() {
		return reload;
	}
	public void setReload(CustomButton reload) {
		this.reload = reload;
	}
	public CustomButton getFullView() {
		return fullView;
	}
	public void setFullView(CustomButton fullView) {
		this.fullView = fullView;
	}
	public CustomCheckbox getReloadFull() {
		return reloadFull;
	}
	public void setReloadFull(CustomCheckbox reloadFull) {
		this.reloadFull = reloadFull;
	}
	public CustomCheckbox getReloadQuick() {
		return reloadQuick;
	}
	public void setReloadQuick(CustomCheckbox reloadQuick) {
		this.reloadQuick = reloadQuick;
	}
	public FullViewControl getFullViewControl() {
		return fullViewControl;
	}
	public void setFullViewControl(FullViewControl fullViewControl) {
		this.fullViewControl = fullViewControl;
	}
	public JTextArea getViewImageInfo() {
		return viewImageInfo;
	}
	public void setViewImageInfo(JTextArea viewImageInfo) {
		this.viewImageInfo = viewImageInfo;
	}
	public CustomButton getViewProgressFinalResult() {
		return viewProgressFinalResult;
	}
	public void setViewProgressFinalResult(CustomButton viewProgressFinalResult) {
		this.viewProgressFinalResult = viewProgressFinalResult;
	}
	public CustomLabel getViewDecodeResultInfo() {
		return viewDecodeResultInfo;
	}
	public void setViewDecodeResultInfo(CustomLabel viewDecodeResultInfo) {
		this.viewDecodeResultInfo = viewDecodeResultInfo;
	}
}

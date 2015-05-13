package Interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import Objects.ImageDecodedResult;
import Sources.ImageProcessing;

public class SlideImagesHorizontal extends JPanel implements Serializable {
	private static final long serialVersionUID = 8400041197768965357L;

	private ArrayList<CustomButton> imageList = new ArrayList<CustomButton>();
	private JPanel parentPanel;
	private Dimension dimension;
	private Point pos;
	private int itemWidth;
	private int itemHeight;
	private TextArea viewMatrixResult;
	
	private int chosenIndex = 0;
	
	private CustomButton generatedImageView;
	public SlideImagesHorizontal() {
		setImageList(new ArrayList<CustomButton>());
	}
	
	public SlideImagesHorizontal(final TextArea viewMatrixResult, final ArrayList<ImageDecodedResult> imageIcon,
			final CustomButton generatedImageView, JPanel parentPanel,
			Color backGround, Point pos, Dimension dimension,
			final int itemWidth, LayoutManager layout) throws IOException {
		setVisible(false);
		setParentPanel(parentPanel);
		setBackground(backGround);
		setViewMatrixResult(viewMatrixResult);
		setDimension(dimension);
		setPos(pos);
		setGeneratedImageView(generatedImageView);
		setBounds(pos.x, pos.y, dimension.width, dimension.height-18);
		setLayout(layout);
		
		setItemheight(dimension.height-18);
		setItemWidth(itemWidth);
		
		for(int i=0; i<imageIcon.size(); i++) {
			CustomButton image = null;
			image = new CustomButton(ImageProcessing.resizeImageFromImageIcon(imageIcon.get(i).icon, new Dimension(itemWidth, itemHeight)), "", null, null, true, true, null, true, new Point((itemWidth+5)*i, 0), new Dimension(itemWidth, itemHeight), SlideImagesHorizontal.this, SwingConstants.CENTER, SwingConstants.CENTER);
			imageList.add(image);
			final int k = i;
			image.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						viewMatrixResult.setText(imageIcon.get(k).getPosArrToString());
						generatedImageView.setIcon(ImageProcessing.resizeImageFromImageIcon(imageIcon.get(k).icon, new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170)));
						chosenIndex = k;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch(Exception ex){System.out.println(Configure.STRING_UNABLE_LOAD_LOOK_AND_FEEL);}
		
		scrollSlide = new JScrollPane(SlideImagesHorizontal.this);
		scrollSlide.setBorder(null);
		parentPanel.add(scrollSlide);
		scrollSlide.setSize(new Dimension(dimension.width, dimension.height));
		this.setPreferredSize(new Dimension(imageIcon.size() * (itemWidth + 5) - 5, dimension.height-18));
		scrollSlide.setBounds(pos.x, pos.y, dimension.width, dimension.height);
		scrollSlide.setVisible(false);
		scrollSlide.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollSlide.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
	}
	
	public SlideImagesHorizontal(String tag, final ArrayList<String> imageURL,
			final CustomButton generatedImageView, JPanel parentPanel,
			Color backGround, Point pos, Dimension dimension,
			final int itemWidth, LayoutManager layout) throws IOException {
		setVisible(false);
		setParentPanel(parentPanel);
		setBackground(backGround);
		setDimension(dimension);
		setPos(pos);
		setGeneratedImageView(generatedImageView);
		setBounds(pos.x, pos.y, dimension.width, dimension.height-18);
		setLayout(layout);
		
		setItemheight(dimension.height-18);
		setItemWidth(itemWidth);
		
		for(int i=0; i<imageURL.size(); i++) {
			CustomButton image = null;
			if(tag == "PPM") {
				image = new CustomButton(ImageProcessing.resizeImageFromPPM(imageURL.get(i), new Dimension(itemWidth, itemHeight)), "", null, null, true, true, null, true, new Point((itemWidth+5)*i, 0), new Dimension(itemWidth, itemHeight), SlideImagesHorizontal.this, SwingConstants.CENTER, SwingConstants.CENTER);
			} else if(tag == "PNG") {
				image = new CustomButton(ImageProcessing.resizeImageFromPNG(imageURL.get(i), new Dimension(itemWidth, itemHeight)), "", null, null, true, true, null, true, new Point((itemWidth+5)*i, 0), new Dimension(itemWidth, itemHeight), SlideImagesHorizontal.this, SwingConstants.CENTER, SwingConstants.CENTER);
			}
			imageList.add(image);
			final int k = i;
			image.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						
						generatedImageView.setIcon(ImageProcessing.resizeImageFromPNG(imageURL.get(k), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 230, Configure.SCREEN_HEIGHT - 170)));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch(Exception ex){System.out.println(Configure.STRING_UNABLE_LOAD_LOOK_AND_FEEL);}
		
		scrollSlide = new JScrollPane(SlideImagesHorizontal.this);
		scrollSlide.setBorder(null);
		parentPanel.add(scrollSlide);
		scrollSlide.setSize(new Dimension(dimension.width, dimension.height));
		this.setPreferredSize(new Dimension(imageURL.size() * (itemWidth + 5) - 5, dimension.height-18));
		scrollSlide.setBounds(pos.x, pos.y, dimension.width, dimension.height);
		scrollSlide.setVisible(false);
		scrollSlide.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollSlide.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
	}
	
	private JScrollPane scrollSlide;
	
	public void finishAddComponents() {
		setVisible(true);
		scrollSlide.setVisible(true);
	}
	public ArrayList<CustomButton> getImageList() {
		return imageList;
	}
	public void setImageList(ArrayList<CustomButton> imageList) {
		this.imageList = imageList;
	}
	public JPanel getParentPanel() {
		return parentPanel;
	}
	public void setParentPanel(JPanel parentPanel) {
		this.parentPanel = parentPanel;
	}
	public Dimension getDimension() {
		return dimension;
	}
	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public int getItemWidth() {
		return itemWidth;
	}
	public void setItemWidth(int itemWidth) {
		this.itemWidth = itemWidth;
	}
	public int getItemHeight() {
		return itemHeight;
	}
	public void setItemheight(int itemHeight) {
		this.itemHeight = itemHeight;
	}
	
	public CustomButton getGeneratedImageView() {
		return generatedImageView;
	}
	public void setGeneratedImageView(CustomButton generatedImageView) {
		this.generatedImageView = generatedImageView;
	}

	public TextArea getViewMatrixResult() {
		return viewMatrixResult;
	}

	public void setViewMatrixResult(TextArea viewMatrixResult) {
		this.viewMatrixResult = viewMatrixResult;
	}

	public int getChosenIndex() {
		return chosenIndex;
	}

	public void setChosenIndex(int chosenIndex) {
		this.chosenIndex = chosenIndex;
	}
}

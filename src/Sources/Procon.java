package Sources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.UIManager;

import Interfaces.Configure;
import Interfaces.CustomButton;
import Interfaces.CustomFont;
import Interfaces.CustomFrame;
import Interfaces.CustomPanel;
import Interfaces.CustomCanvas;

public class Procon implements Serializable {
	
	private static final long serialVersionUID = -8692208439710033981L;
	private static GenerateTest generateTestPanel;
	private static ImageDecoder imageDecoderPanel;
	private static CustomButton generateTest;
	private static CustomButton imageDecoder;
	
	public static void main(String args[]) throws IOException {
		
		CustomFrame mainFrame = new CustomFrame("NAPROCK Procon 2014", true, false, new Dimension(Configure.SCREEN_WIDTH, Configure.SCREEN_HEIGHT));
		CustomCanvas mainPane = new CustomCanvas(mainFrame, Color.GRAY);
		mainFrame.setLayout(null);
	
		CustomPanel leftPanel = new CustomPanel(mainPane, Color.GRAY, new Point(0, 0), new Dimension(Configure.LEFT_PANEL_WIDTH, Configure.SCREEN_HEIGHT), null);
		int numberOfTabs = 2;
		generateTest = new CustomButton("Generate test", Color.WHITE, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 18), false, false, Color.GRAY, true, new Point(0, Configure.SCREEN_HEIGHT/2 - numberOfTabs * 20 + 0*40), new Dimension(Configure.LEFT_PANEL_WIDTH, 40), leftPanel);
		imageDecoder = new CustomButton("Image decoder", Color.GRAY, CustomFont.getFont(Configure.DEFAULT_FONT, Font.PLAIN, 18), false, false, Color.WHITE, true, new Point(0, Configure.SCREEN_HEIGHT/2 - numberOfTabs * 20 + 1*40), new Dimension(Configure.LEFT_PANEL_WIDTH, 40), leftPanel);
		
		generateTestPanel = new GenerateTest(mainPane, Color.WHITE, new Point(Configure.LEFT_PANEL_WIDTH, 10), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 10, Configure.SCREEN_HEIGHT-20), null);
		imageDecoderPanel = new ImageDecoder(mainPane, Color.WHITE, new Point(Configure.LEFT_PANEL_WIDTH, 10), new Dimension(Configure.SCREEN_WIDTH - Configure.LEFT_PANEL_WIDTH - 10, Configure.SCREEN_HEIGHT-20), null);

		generateTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				imageDecoder.setBackground(Color.GRAY);
				imageDecoder.setForeground(Color.WHITE);
				generateTest.setBackground(Color.WHITE);
				generateTest.setForeground(Color.GRAY);
				generateTestPanel.setVisible(true);
				imageDecoderPanel.setVisible(false);
			}
		});
		imageDecoder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imageDecoder.setBackground(Color.WHITE);
				imageDecoder.setForeground(Color.GRAY);
				generateTest.setBackground(Color.GRAY);
				generateTest.setForeground(Color.WHITE);
				generateTestPanel.setVisible(false);
				imageDecoderPanel.setVisible(true);
			}
		});
		
		leftPanel.finishAddComponent();
		mainPane.finishAddComponent();
		mainFrame.finishAddComponent();
		
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch(Exception ex){System.out.println(Configure.STRING_UNABLE_LOAD_LOOK_AND_FEEL);}
		
	}
}

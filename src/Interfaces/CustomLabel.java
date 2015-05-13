package Interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomLabel extends JLabel implements Serializable {
	private static final long serialVersionUID = -639220403481837750L;

	public CustomLabel(String text, Color textColor, Color background,
			Font font, Point pos, Dimension dimension, boolean visible,
			int horizontalAlignment, int verticalAlignment, JPanel parentPane) {
		super(text);
		setBackground(background);
		setFont(font);
		setForeground(textColor);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		setVisible(visible);
		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(verticalAlignment);
		parentPane.add(this);
	}
	public CustomLabel(String text, Color textColor, Color background, Font font, Point pos, Dimension dimension) {
		super(text);
		setBackground(background);
		setFont(font);
		setForeground(textColor);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
	}
		
	public CustomLabel(ImageIcon image, Point pos, Dimension dimension, boolean visible, JPanel parentPane) {
		super(image);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		setVisible(visible);
		parentPane.add(this);
	}
	
	public CustomLabel(BufferedImage image, Point pos, Dimension dimension, boolean visible, JPanel parentPane) {
		super();
		setIcon((Icon) image);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		setVisible(visible);
		parentPane.add(this);
	}
}

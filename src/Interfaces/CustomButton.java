package Interfaces;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

public class CustomButton extends JButton implements Serializable {
	private static final long serialVersionUID = -3670650049257691324L;
	public CustomButton(String text, Color textColor, Font font,
			boolean borderPainted, boolean focusPainted, Color background,
			boolean visible, Point pos, Dimension dimension, JPanel parentPane) {
		super(text);
		setForeground(textColor);
		setFont(font);
		setBorderPainted(borderPainted);
		setFocusPainted(false);
		setBackground(background);
		setVisible(visible);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		parentPane.add(this);
	}

	public CustomButton(ImageIcon image, String text, Color textColor,
			Font font, boolean borderPainted, boolean focusPainted,
			Color background, boolean visible, Point pos, Dimension dimension,
			JPanel parentPane, int horizontalAlignment, int verticalAlignment) {
		super(text);
		setForeground(textColor);
		setFont(font);
		setBorderPainted(borderPainted);
		setFocusPainted(false);
		setBackground(background);
		setVisible(visible);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		setIcon(image);
		setHorizontalAlignment(horizontalAlignment);
		setVerticalAlignment(verticalAlignment);
		parentPane.add(this);
	}
}

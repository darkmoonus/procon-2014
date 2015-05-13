package Interfaces;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class CustomCheckbox extends JCheckBox {
	private static final long serialVersionUID = -5617978145299641808L;

	public CustomCheckbox(String text, Font font, Color background, boolean borderPainted, boolean focusPainted, boolean isChecked, boolean visible, Point pos, Dimension dimension, JPanel parentPane) {
		super(text);
		setBackground(background);
		setBorderPainted(borderPainted);
		setFocusPainted(focusPainted);
		setSelected(isChecked);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		parentPane.add(this);
		setVisible(visible);
		setFont(font);
	}
}

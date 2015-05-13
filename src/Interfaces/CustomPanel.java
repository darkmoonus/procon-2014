package Interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.io.Serializable;

import javax.swing.JPanel;

public class CustomPanel extends JPanel implements Serializable {
	private static final long serialVersionUID = 1L;
	private JPanel parentPanel;
	private Dimension dimension = new Dimension();
	private Point pos = new Point();
	public CustomPanel(JPanel parentPanel, Color backGround, Point pos, Dimension dimension, LayoutManager layout) {
		super();
		this.setParentPanel(parentPanel);
		this.setDimension(dimension);
		this.setPos(pos);
		setBackground(backGround);
		setLayout(layout);
		setBounds(pos.x, pos.y, dimension.width, dimension.height);
		parentPanel.add(this);
		setVisible(false);
	}
	
	public void finishAddComponent() {
		setVisible(true);
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
}

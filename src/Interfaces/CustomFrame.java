package Interfaces;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;

import javax.swing.JFrame;

public class CustomFrame extends JFrame implements Serializable {
	private static final long serialVersionUID = -5670659850751618295L;
	private Dimension dimension = new Dimension();
	private Point position = new Point();
	public CustomFrame(String title, boolean undecorate, boolean resizeable, Dimension dimension) {
		super(title);
		setUndecorated(undecorate);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-dimension.width)/2,(screenSize.height- dimension.height)/2, dimension.width, dimension.height);
		position.x = (screenSize.width-dimension.width)/2;
		position.y = (screenSize.height-dimension.height)/2;
		this.dimension = dimension;
		setResizable(resizeable);
		setSize(new java.awt.Dimension(this.dimension.width, this.dimension.height));
		setPreferredSize(new java.awt.Dimension(this.dimension.width, this.dimension.height));
		setVisible(false);
	}
	public void finishAddComponent() {
		setVisible(true);
	}
	public Dimension getDimension() {
		return this.dimension;
	}
	public Point getPosition() {
		return this.position;
	}
}

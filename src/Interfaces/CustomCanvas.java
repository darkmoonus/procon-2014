package Interfaces;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.swing.JPanel;

public class CustomCanvas extends JPanel implements Serializable, MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 1L;
	private CustomFrame parentFrame;
	private Point beginPoint, endPoint;
	public static boolean canRemove = false;
	public CustomCanvas(CustomFrame parentFrame, Color background) {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.parentFrame = parentFrame;
		setBackground(background);
		setSize(parentFrame.getDimension().width, parentFrame.getDimension().height);
		setVisible(false);
		beginPoint = new Point();
		endPoint = new Point();
		parentFrame.setContentPane(this);
	}
	
	public void finishAddComponent() {
		setVisible(true);
	}
        
	@Override
	public void mouseDragged(MouseEvent e) {
		if(canRemove) {
			endPoint = new Point(e.getX(), e.getY());
			if(endPoint.x > beginPoint.x) endPoint.x = beginPoint.x + Configure.SMOOTHLY; else endPoint.x = beginPoint.x - Configure.SMOOTHLY;
			if(endPoint.y > beginPoint.y) endPoint.y = beginPoint.y + Configure.SMOOTHLY; else endPoint.y = beginPoint.y - Configure.SMOOTHLY;
			parentFrame.getPosition().x = parentFrame.getPosition().x + endPoint.x - beginPoint.x;
			parentFrame.getPosition().y = parentFrame.getPosition().y + endPoint.y - beginPoint.y;
			parentFrame.setBounds(parentFrame.getPosition().x, parentFrame.getPosition().y, parentFrame.getSize().width, parentFrame.getSize().height);
			beginPoint = endPoint;
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		canRemove = true;
		beginPoint = new Point(e.getX(), e.getY());
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		canRemove = false;
	}

}

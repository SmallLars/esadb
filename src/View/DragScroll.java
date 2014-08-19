package View;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;


public class DragScroll extends MouseInputAdapter {
	Component component;
	int xDiff, yDiff;
	
	public DragScroll(Component component) {
		this.component = component;
	}

	public void mouseDragged(MouseEvent e) {
		Container c = component.getParent();
		if (c instanceof JViewport) {
			JViewport jv = (JViewport) c;
			Point p = jv.getViewPosition();
			int newX = p.x - (e.getX() - xDiff);
			int newY = p.y - (e.getY() - yDiff);

			int maxX = component.getWidth() - jv.getWidth();
			int maxY = component.getHeight() - jv.getHeight();
			if (newX < 0) newX = 0;
			if (newX > maxX) newX = maxX;
			if (newY < 0) newY = 0;
			if (newY > maxY) newY = maxY;

			jv.setViewPosition(new Point(newX, newY));
		}
	}

	public void mousePressed(MouseEvent e) {
		xDiff = e.getX();
		yDiff = e.getY();
	}
}
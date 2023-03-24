package pg.bgraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Canvas is a <i>Swing</i> component capable of drawing objects whose classes implement 
 * the ShapeImplementation interface.
 * 
 * @see ShapeImplementation, JComponent
 */
public final class Canvas extends JComponent {
	/**
	 * All shapes to be drawn in this canvas
	 */
	private List<BShape> shapes;
	/**
	 * Background color of the Canvas
	 */
	private BColor backgroundColor;
//	/**
//	 * 
//	 */
//	private ArrayList<CanvasRemoveListener> removeListeners;
//
//	private void notifyRemove(BShape shape) {
//		for(CanvasRemoveListener listener : removeListeners)
//			listener.onRemove(shape);
//	}	
	
	protected void executeOnGUIThread(final Runnable executor) {
//		try {
			//SwingUtilities.invokeAndWait(executor);
			SwingUtilities.invokeLater(executor);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		repaint();
	}	

	/**
	 * @param dimensions Determines the width and height of the canvas
	 */
	public Canvas(Dimension dimensions) {
		//removeListeners = new ArrayList<CanvasRemoveListener>();
		shapes = Collections.synchronizedList(new ArrayList<BShape>());
		setPreferredSize(dimensions);
		backgroundColor = new BColor(Color.BLACK);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
	}	

	@Override
	protected void paintComponent(Graphics g) {
		//g.setColor(backgroundColor.getAWTColor());
		//g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2D = (Graphics2D) g;
		synchronized (shapes) {
			for(BShape s: shapes) {
				s.draw(g2D);
			}			
		}
	}

	/**
	 *  
	 * @param shape New shape to be painted
	 */
	public void addShape(final BShape shape) {
		shape.setCanvas(this);
		shapes.add(shape);
		repaint(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
	}
	
	public void removeShape(final BShape shape) {
		shapes.remove(shape);
		//notifyRemove(shape);
		//repaint();
		repaint(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
	}
	
	/**
	 * 
	 * @param bkColor The new background color
	 */
	public void setBackgroundColor(BColor backgroundColor) {
		this.backgroundColor = backgroundColor;
		repaint();
	}
	
}

package pg.bgraphics;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public abstract class GraphicWindowBase {
	// the only window
	private JFrame frame;
	// the only place to draw
	private Canvas canvas = null;
	
	protected Canvas getCanvas() {
		return canvas;
	}
	
	public GraphicWindowBase (final String title, final int width, final int height) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					frame = new JFrame();
					frame.setTitle(title);
					frame.add(canvas = new Canvas(new Dimension(width, height)));
					frame.pack();
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent arg0) {
							System.exit(0);
						}
					});
					frame.setResizable(false);
					frame.setVisible(true);
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Hides the window.
	 */
	public void hide() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					frame.setVisible(false);
				}				
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public void setBackgroundColor(int color) {
		setBackgroundColor(new BColor(color));
	}
	public void setBackgroundColor(BColor color) {
		if (color == null) color = BColor.None;
		canvas.setBackgroundColor(color);
	}
	
//	public void add(BShape shape) {
//		canvas.addShape(shape);
//	}
//	
//	public void removeShape(BShape shape) {
//		canvas.removeShape(shape);
//	}
}

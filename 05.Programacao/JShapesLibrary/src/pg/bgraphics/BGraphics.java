package pg.bgraphics;

import java.awt.Graphics;


/**
 * Provides a simple Graphic User Interface (GUI). Draw Points, Lines, Rectangle, Circle and Ellipses, 
 * filled or empty. Line and fill color can be set.
 * All points can be retrieved or set.
 * 
 * Only one window can be displayed.
 * @author jsimao
 */
public class BGraphics extends GraphicWindowBase {
private static BGraphics graphics;
//	// default window height
//	private static int heigth = 400;
//	// default window width
//	private static int width  = 400;
    // Color used to fill shapes
	private BColor currentFillColor = BColor.BLUE;
	// Color used to draw the outline of shapes
	private BColor currentPenColor = BColor.GREEN;
	// Pencil width to draw the outline of shapes
	private int currentPenWidth = 1;

	/**
	 * Shows the window. This method must be called before drawing.
	 */
	public BGraphics(String title, int heigth, int width) {
		super(title, heigth, width);
	}
	
	/**
	 * Do nothing for some miliseconds
	 */
	public void delay(long miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			
		}
	}
	
	/**
	 * 
	 * @param width
	 */
	public void setPenWidth(int width) {
		if (width >= 1)
			currentPenWidth = width;
	}
	
	/**
	 * 
	 * @param color A color described in Red-Green-Blue (RGB) components.
	 */
	public void setFillColor(int color) {
		setFillColor(new BColor(color));
	}
	public void setFillColor(BColor color) {
		if (color == null) color = BColor.None;
		currentFillColor = color;
	}
	
	/**
	 * 
	 * @param color
	 */
	public void setPenColor(int color) {
		setPenColor(new BColor(color));
	}
	public void setPenColor(BColor color) {
		if (color == null) color = BColor.None;
		currentPenColor = color;
	}
	
	public BBitmap createBitmap(int x, int y, int columns, int lines) {
		BBitmap bitmap = new BBitmap(getCanvas(), x, y, columns, lines);
		getCanvas().addShape(bitmap);
		return bitmap;
	}
	
	public ShapeGroup createShapeGroup(int x, int y) {
		ShapeGroup group = new ShapeGroup(x, y);
		getCanvas().addShape(group);
		return group;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public BRectangle drawRectangle(int x, int y, int width, int height) {
		return createRectangle(x, y, width, height, false);
	}
		
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public BRectangle fillRectangle(int x, int y, int width, int height) {
		return createRectangle(x, y, width, height, true);
	}

	
	private BRectangle createRectangle(int x, int y, int width, int height, boolean filled) {
		BRectangle rectangle = new BRectangle(
				getCanvas(), x, y, width-currentPenWidth, height-currentPenWidth,
				filled?currentFillColor:BColor.None, currentPenColor, currentPenWidth);
		getCanvas().addShape(rectangle);
		return rectangle;
	}
	
	public BCircle drawCircle(int x, int y, int radius) {
		return createCircle(x, y, radius, false);
	}

	public BCircle fillCircle(int x, int y, int radius) {
		return createCircle(x, y, radius, true);
	}
	
	private BCircle createCircle(int x, int y, int radius, boolean filled) {
		BCircle circle = new BCircle(
				getCanvas(), x, y, radius,
				filled?currentFillColor:BColor.None, currentPenColor, currentPenWidth);
		getCanvas().addShape(circle);
		return circle;
	}

	public static void init(String string, int i, int j) {
		graphics = new BGraphics(string, i, j);
	}
		
}

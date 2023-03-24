package pg.bgraphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class BShape {
	protected static final BColor fillColor = BColor.None;
	protected static final BColor penColor = BColor.BLUE;
	protected Canvas owner;
	
	protected BShape(Canvas owner) {
		this.owner = owner;
	}
	
	
	/**
	 * 
	 * @return Top left coordinate in the x-axis.
	 */
	public abstract int getX();

	/**
	 * 
	 * @return Top left coordinate in the y-axis.
	 */
	public abstract int getY();

	/**
	 * 
	 * @return The width of the rectangular area that encloses the shape.
	 */
	public abstract int getWidth();

	/**
	 * 
	 * @return The height of the rectangular area that encloses the shape.
	 */
	public abstract int getHeight();

	/**
	 * Paints this shape in the graphic context
	 * @param g Graphic context
	 */
	public abstract void draw(Graphics2D g);

	public boolean intersects(BShape shape) {
		Rectangle r = new Rectangle(getX(), getY(), getWidth(), getHeight());
		return r.intersects(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
	}

	public boolean intersects(int x, int y, int width, int height) {
		Rectangle r = new Rectangle(getX(), getY(), getWidth(), getHeight());
		return r.intersects(x, y, width, height);	
	}
		
	/** 
	 * Removes the shape from the associated Canvas
	 * @param dx
	 * @param dy
	 */
	protected final void delete() {		
		owner.removeShape(this);
	}
	
	public final void move(final int dx, final int dy) {
		if (owner != null)
			owner.executeOnGUIThread(new Runnable() {			
				@Override
				public void run() {
					internalMove(dx, dy);
				}
			});
		else
			internalMove(dx, dy);
	}
	/**
	 * Moves the shape.
	 * @param dx Offset in x-axis
	 * @param dy Offset in y-axis
	 */
	protected abstract void internalMove(int dx, int dy);


	protected void setCanvas(Canvas canvas) {
		owner = canvas;
	}

}
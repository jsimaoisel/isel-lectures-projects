package pg.bgraphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

public abstract class AbstractBShape extends BShape {
	protected static BColor defaultFillColor = new BColor(Color.white);
	protected static BColor defaultPenColor  = new BColor(Color.black);
	protected static int defaultPenWidth = 1;
	private BColor fillColor;
	private BColor penColor;
	private BasicStroke stroke;
	protected Shape java2DShape; 
		
	public AbstractBShape(Canvas owner, Shape java2DShape, BColor fillColor, BColor penColor, int penWidth) {
		super(owner);
		this.java2DShape = java2DShape;
		this.fillColor = fillColor;
		this.penColor = penColor;
		this.stroke = new BasicStroke(penWidth);
	}

	/**
	 * 
	 * @return The BColor representing the color to fill the shape. <code>null</code> represents a non-filled shape.
	 */
	public BColor getFillColor() {
		return fillColor;
	}
	/**
	 * 
	 * @param fillColor The new color to fill the shape. Can be <code>null</code>, meaning the shape should not be filled.
	 */
	public void setFillColor(BColor fillColor) {
		this.fillColor = fillColor;
		Rectangle r = java2DShape.getBounds();
		owner.repaint(r.x,r.y,r.width,r.height);
	}
	
	/**
	 * 
	 * @return The BColor representing the color of the shape's border.
	 */
	public BColor getPenColor() {
		return penColor;
	}
	/**
	 * 
	 * @param penColor The new BColor for the shape's border.
	 */
	public void setPenColor(BColor penColor) {
		this.penColor = penColor;
		owner.repaint();
	}
	
	/* (non-Javadoc)
	 * @see pg.bgraphics.ShapeInterface#getX()
	 */
	public int getX() {
		return (int) java2DShape.getBounds().getX();
	}
	/* (non-Javadoc)
	 * @see pg.bgraphics.ShapeInterface#getY()
	 */
	public int getY() {
		return (int) java2DShape.getBounds().getY();
	}

	/* (non-Javadoc)
	 * @see pg.bgraphics.ShapeInterface#getWidth()
	 */
	public int getWidth() {
		return java2DShape.getBounds().width;
	}

	/* (non-Javadoc)
	 * @see pg.bgraphics.ShapeInterface#getHeight()
	 */
	public int getHeight() {
		return java2DShape.getBounds().height;
	}

	/**
	 * Paints this shape in the graphic context
	 * @param g Graphic context
	 */
	public void draw(Graphics2D g) {
		g.setStroke(stroke);
		if (fillColor != BColor.None) {
			g.setPaint(fillColor.getAWTColor());
			g.fill(java2DShape);
		}
		if (penColor != BColor.None) {
			g.setColor(penColor.getAWTColor());
			g.draw(java2DShape);
		}
	}
	
}

package pg.bgraphics;

import java.awt.geom.Ellipse2D;

public class BCircle extends BRectangular {

	private int radius;

	protected BCircle(Canvas owner, 
			  int x, int y, int radius,
			  BColor fillColor, BColor penColor, int penWidth) {
		super(owner,
			  new Ellipse2D.Double(x, y, radius*2, radius*2),
			  fillColor, penColor, penWidth);
		this.radius = radius;
	}

	public BCircle(int x, int y, int radius,
			  	   BColor fillColor, BColor penColor, int penWidth)	{
		this(null,x,y,radius,fillColor,penColor,penWidth);
	}
	
	public BCircle(int x, int y, int radius) {
		this(null,x,y,radius,defaultFillColor,defaultPenColor,defaultPenWidth);
	}

	public int getRadius() {
		return radius;
	}
	public void setRadius(final int radius) {
		owner.executeOnGUIThread(new Runnable() {
			@Override
			public void run() {
				Ellipse2D thisShape = (Ellipse2D) java2DShape;
				thisShape.setFrame(thisShape.getX(), thisShape.getY(), radius, radius);
			}
		});
	}

	public void setFillColor(int color) {
		setFillColor(new BColor(color));
	}

}

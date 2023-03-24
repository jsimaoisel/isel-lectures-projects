package pg.bgraphics;

import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class BRectangle extends BRectangular {
	private static final double ARCW = 1.5, ARCH = 1.5;
	protected BRectangle(Canvas owner, 
					  int x, int y, int width,  int height,
					  BColor fillColor, BColor penColor, int penWidth) 
	{
		super(owner, 
			  new RoundRectangle2D.Double(x,y,width,height,ARCW,ARCH),
			  fillColor, penColor, penWidth);
	}
	
	public BRectangle(int x, int y, int width, int height,
			          BColor fillColor, BColor penColor, int penWidth) 
	{
		this(null,x, y, width, height, fillColor, penColor, penWidth);
	}

}

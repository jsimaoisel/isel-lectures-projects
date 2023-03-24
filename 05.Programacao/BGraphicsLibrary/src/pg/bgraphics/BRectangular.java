package pg.bgraphics;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

public abstract class BRectangular extends AbstractBShape {

	protected BRectangular(Canvas owner, Shape java2dShape, 
						BColor fillColor,
						BColor penColor, int penWidth) 
	{
		super(owner, java2dShape, fillColor, penColor, penWidth);
	}

	@Override
	protected void internalMove(int dx, int dy) {
		Rectangle2D bounds = java2DShape.getBounds2D();
		//((RectangularShape) java2DShape).setFrame(bounds.getX()+dx, bounds.getY()+dy, bounds.getWidth(), bounds.getHeight());
		((RectangularShape) java2DShape).setFrame(dx, dy, bounds.getWidth(), bounds.getHeight());
	}

	public void setHeight(final int height) {
		owner.executeOnGUIThread(new Runnable() {
			@Override
			public void run() {
				RoundRectangle2D.Double thisShape = (RoundRectangle2D.Double) java2DShape;
				thisShape.setFrame(thisShape.getX(), thisShape.getY(), thisShape.getWidth(), height);
			}		
		});
	}
	public void setWidth(final int width) {
		owner.executeOnGUIThread(new Runnable() {
			@Override
			public void run() {
				RoundRectangle2D.Double thisShape = (RoundRectangle2D.Double) java2DShape;
				thisShape.setFrame(thisShape.getX(), thisShape.getY(), width, thisShape.getHeight());
			}
		});
	}
}

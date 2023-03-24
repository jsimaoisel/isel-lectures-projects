package pg.bgraphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ShapeGroup extends BShape {
	private ArrayList<BShape> shapes;
	private int X;
	private int Y;
	
	protected ShapeGroup(Canvas owner, int X, int Y) {
		super(owner);
		shapes = new ArrayList<BShape>();
		this.X = X;
		this.Y = Y;
	}
	
	public ShapeGroup(int X, int Y) {
		this(null, X, Y);
	}
	
	@Override
	public void draw(Graphics2D g) {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		for (BShape shape : shapes)
			shape.draw(graphics);
		g.drawImage(image, X, Y, null);
	}

	@Override
	public int getHeight() {
		int maxY=0;
		for (BShape shape: shapes) {
			int currentY = shape.getY()+shape.getHeight();
			if (maxY < currentY)
				maxY = currentY;
		}
		return maxY+1;
	}

	@Override
	public int getWidth() {
		int maxX=0;
		for (BShape shape: shapes) {
			int currentX = shape.getX()+shape.getWidth();
			if (maxX < currentX)
				maxX = currentX;
		}
		return maxX+1;
	}

	@Override
	public int getX() {
		return X;
	}

	@Override
	public int getY() {
		return Y;
	}

	@Override
	protected void internalMove(int dx, int dy) {
		X = dx;
		Y = dy;
	}
	
	public void addShape(BShape shape) {
		shapes.add(shape);
		if (owner!=null)
			owner.repaint();
	}

}

package pg.bgraphics;

import java.awt.Graphics2D;

public class MoveableShape extends BShape {
	private BShape shape;
	private int X;
	private int Y;
	
	protected MoveableShape(Canvas owner, int X, int Y) {
		super(owner);
		this.X = X;
		this.Y = Y;
	}
	
	public MoveableShape(int X, int Y) {
		this(null, X, Y);
	}

	@Override
	public void draw(Graphics2D g) {
		shape.draw(g);
	}

	@Override
	public int getHeight() {
		return shape.getHeight();
	}

	@Override
	public int getWidth() {
		return 0;
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
		// TODO Auto-generated method stub

	}


}

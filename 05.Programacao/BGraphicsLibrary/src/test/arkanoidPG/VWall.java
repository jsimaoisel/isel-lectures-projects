package test.arkanoidPG;
import pg.bgraphics.BCircle;
import pg.bgraphics.BColor;
import pg.bgraphics.BRectangle;


public class VWall {
	private int width;
	private int height;
	private int x;
	private int y;
	
	public VWall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public BRectangle getShape() {
		return new BRectangle(x, y, width, height, BColor.GREEN, BColor.BLUE, 1);
	}
}

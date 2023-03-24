package pg.bgraphics;

public class Point {
	private java.awt.Point point;
	public Point(int x, int y) {
		point = new java.awt.Point(x, y);
	}
	public void setX(int x) {
		point.x=x;
	}
	public void setY(int y) {
		point.y=y;
	}
	public int getX() {
		return point.x;
	}
	public int getY() {
		return point.y;
	}
	public void translate(int dx, int dy) {
		point.translate(dx, dy);
	}
}

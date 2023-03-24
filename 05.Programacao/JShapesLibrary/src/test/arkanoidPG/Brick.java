package test.arkanoidPG;
import pg.bgraphics.BColor;
import pg.bgraphics.BRectangle;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.GameEnviromentPG;
import pg.graphicenviroment.IntersectionInfo;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.RandomNumbers;


public class Brick {
	private BRectangle rectangle;	
	public Brick(int x, int y, int w, int h, BColor fillColor) {
		this.rectangle = new BRectangle(x, y, w, h, fillColor, BColor.BLUE, 1);
	}	
	public BRectangle getShape() {
		return rectangle;
	}	
}


//if (RandomNumbers.generate(0, 3) == 3) {
//GameEnviromentPG.addObjectToLevel(
//		new BonusBrick(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight())
//);
//}
//else
//	rectangle.setFillColor(new BColor(hitCounter*50, 100, 100));		

//public class Brick {
//
//	private BRectangle rectangle; 
//	private int hitCounter = 2;
//	
//	public Brick(int x, int y, int w, int h) {
//		this.rectangle = new BRectangle(x, y, w, h, BColor.RED, BColor.BLUE, 1);
//	}
//	
//	public void onCollision(OnCollisionParams params, Ball b) {
//		if (--hitCounter == 0) {
//			GameEnviromentPG.removeOjectFromLevel(this);
//		}
//	}
//		
//	public BRectangle getShape() {
//		return rectangle;
//	}
//	
//	public int getHitCounter() {
//		return hitCounter;
//	}
//}
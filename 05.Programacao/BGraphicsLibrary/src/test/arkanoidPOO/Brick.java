package test.arkanoidPOO;
import pg.bgraphics.BColor;
import pg.bgraphics.BRectangle;
import pg.graphicenviroment.GameEnviromentPOO;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.interfaces.IGameObject;


public class Brick extends AbstractStaticObject {

	private BRectangle rectangle; 
	private int hitCounter = 2;
	
	public Brick(int x, int y, int w, int h) {
		this.rectangle = new BRectangle(x, y, w, h, BColor.RED, BColor.BLUE, 1);
	}
	
	@Override
	public BRectangle getShape() {
		return rectangle;
	}

	@Override
	public void onCollision(OnCollisionParams params, IGameObject colisionObject) {
		((CollidingInterface)colisionObject).colideWith(params, this);
//		if (colisionObject instanceof Ball) {
//			if (--hitCounter == 0) {
//				GameEnviromentPOO.removeOjectFromLevel(this);
//				GameEnviromentPOO.addObjectToLevel(
//						new BonusBrick(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight())
//				);
//			}
//			else
//				rectangle.setFillColor(new BColor(hitCounter*50, 100, 100));
//		}
	}

	@Override
	public void colideWith(OnCollisionParams params, Ball b) {
		if (--hitCounter == 0) {
			GameEnviromentPOO.removeOjectFromLevel(this);
			GameEnviromentPOO.addObjectToLevel(
					new BonusBrick(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight())
			);
		}
		else
			rectangle.setFillColor(new BColor(hitCounter*50, 100, 100));
	}


}

package test.arkanoidPOO;
import pg.bgraphics.BCircle;
import pg.bgraphics.BColor;
import pg.bgraphics.Point;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.GameEnviromentPOO;
import pg.graphicenviroment.IntersectionInfo;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.RandomNumbers;
import pg.graphicenviroment.interfaces.IGameObject;


public class Ball extends AbstractMovingObject {
	private LinearMove movingStrategy;
	private BCircle shape;
	public Ball(int x, int y) {
		movingStrategy = new LinearMove();
		this.shape = new BCircle(x, y, 10,  
				new BColor(RandomNumbers.generate(0, 255), RandomNumbers.generate(0, 255), RandomNumbers.generate(0, 255)),
				BColor.RED, 1);
	}
	
	
	@Override
	public void onCollision(OnCollisionParams params, IGameObject colisionObject) {
		((CollidingInterface)colisionObject).colideWith(params, this);
	}

//	@Override
//	public void onCollision(OnCollisionParams params, IGameObject colisionObject) {
//		//((CollidingInterface)colisionObject).colideWith(this);
//		if (colisionObject instanceof BonusBrick) 
//			return;
//		if (colisionObject == GameEnviromentPOO.getObjectByName("Floor"))
//			GameEnviromentPOO.actorDied();
//		onCollisionInternal(params);
//	}
	
	@Override
	public void onMove(GEPoint point) {
		movingStrategy.onMove(point);
	}
	
	@Override
	public BCircle getShape() {
		return shape;
	}

	private void onCollisionInternal(OnCollisionParams params) {
		if (params.isFinalCollision) {
			if (params.intersectionInfo.getWidth() >= params.intersectionInfo.getHeight()) {
				movingStrategy.stickY(params);
				movingStrategy.invY();
			}
			else {
				movingStrategy.stickX(params);
				movingStrategy.invX();
			}
		}		
	}

	@Override
	public void colideWith(OnCollisionParams params, Brick b) {
		System.out.println("Ball collide with brick");
		onCollisionInternal(params);
	}
	
}

package test.arkanoidPG;
import pg.bgraphics.BCircle;
import pg.bgraphics.BColor;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.GameEnviromentPG;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.RandomNumbers;
import test.arkanoidPOO.LinearMove;

public class Ball {
	private LinearMove movingStrategy; 
	private BCircle shape;
	public Ball(int x, int y) {
		this.movingStrategy = new LinearMove();
		this.shape = new BCircle(x, y, 10,  
				new BColor(RandomNumbers.generate(0, 255), RandomNumbers.generate(0, 255), RandomNumbers.generate(0, 255)),
				BColor.RED, 1);
	}
	
	public void onCollision(OnCollisionParams params, Wall w) {
		if (w == GameEnviromentPG.getObjectByName("Floor"))
			GameEnviromentPG.actorDied();
		else
			onCollisionInternal(params);
	}
	
	public void onCollision(OnCollisionParams params, HitBrick brick) {
		onCollisionInternal(params);
	}
	
	public void onCollision(OnCollisionParams params, Racket racket) {
		onCollisionInternal(params);
	}
	
	public void onMove(GEPoint newPosition) {
		movingStrategy.onMove(newPosition);
	}
	
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
}

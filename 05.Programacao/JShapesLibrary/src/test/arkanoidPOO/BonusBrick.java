package test.arkanoidPOO;

import pg.bgraphics.BColor;
import pg.bgraphics.BRectangle;
import pg.bgraphics.Point;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.GameEnviromentPG;
import pg.graphicenviroment.GameEnviromentPOO;
import pg.graphicenviroment.MovingStrategyProxy;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.OnMoveParams;
import pg.graphicenviroment.interfaces.IActor;
import pg.graphicenviroment.interfaces.IGameObject;
import test.arkanoidPOO.LinearMove;

public class BonusBrick extends AbstractMovingObject {
	private BRectangle shape;
	private LinearMove movement;
	
	public BonusBrick(int x, int y, int width, int height) {
		shape = new BRectangle(x, y, width, height, new BColor(255, 100, 100), BColor.BLUE, 1);
		movement = new LinearMove(0, 5);
	}
	
//	public void onCollision(OnCollisionParams params, HWall wall) {
//		params.gameEnviroment.removeActor(this);
//	}
//	
//	public void onMove() {
//		Point p = movement.onMove(shape.getX(), shape.getY());
//		shape.move(p.getX(), p.getY());
//	}
	
	public BRectangle getShape() {
		return shape;
	}

	@Override
	public void onCollision(OnCollisionParams params, IGameObject collisionObject) {
		if (!(collisionObject instanceof HWall)) return;
		if (collisionObject == GameEnviromentPOO.getObjectByName("Floor"))
			GameEnviromentPOO.removeOjectFromLevel(this);
	}

	@Override
	public void onMove(GEPoint newActorPosition) {
		movement.onMove(newActorPosition);
	}
	
	
}

package test.arkanoidPOO;

import pg.bgraphics.BShape;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.interfaces.IActor;
import pg.graphicenviroment.interfaces.IGameObject;

public abstract class AbstractMovingObject implements IActor, CollidingInterface  {

	@Override
	public void onKey(char key, String modifiers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMove(GEPoint newActorPosition) {
	}

	@Override
	public BShape getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollision(OnCollisionParams params, IGameObject colisionObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void colideWith(OnCollisionParams param, Ball b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void colideWith(OnCollisionParams param, Brick b) {
		// TODO Auto-generated method stub
		
	}
	
	
}

package test.arkanoidPOO;

import pg.bgraphics.BShape;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.interfaces.IGameObject;

public class AbstractStaticObject implements IGameObject, CollidingInterface {

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

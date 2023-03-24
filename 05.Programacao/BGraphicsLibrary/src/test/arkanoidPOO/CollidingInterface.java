package test.arkanoidPOO;

import pg.graphicenviroment.OnCollisionParams;

public interface CollidingInterface {
	void colideWith(OnCollisionParams param, Ball b);
	void colideWith(OnCollisionParams param, Brick b);
}

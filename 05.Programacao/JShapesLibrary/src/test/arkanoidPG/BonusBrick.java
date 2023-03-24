package test.arkanoidPG;

import pg.graphicenviroment.GameEnviromentPG;
import pg.graphicenviroment.OnCollisionParams;

public class BonusBrick extends HitBrick {	
	public BonusBrick(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	public void onCollision(OnCollisionParams params, Ball b) {
		super.onCollision(params, b);
		if (getHitCounter() == 0)
			GameEnviromentPG.addObjectToLevel(new Bonus(/*...*/));
	}
}

package test.arkanoidPG;

import pg.bgraphics.BColor;
import pg.graphicenviroment.GameEnviromentPG;
import pg.graphicenviroment.OnCollisionParams;

public class HitBrick extends Brick {
	private BColor[] colors;
	private int hitCounter;	
	public HitBrick(int x, int y, int w, int h) {
		super(x, y, w, h, BColor.RED);
		colors = new BColor[] { BColor.GREEN, BColor.RED };
		hitCounter = colors.length-1;
	}
	public void onCollision(OnCollisionParams params, Ball b) {
		if (hitCounter-- == 0)
			GameEnviromentPG.removeOjectFromLevel(this);
		else
			getShape().setFillColor(colors[hitCounter]);
	}
	public int getHitCounter() { return hitCounter; }
}

//BRectangle rectangle = getShape();
//super.onCollision(params, b);
//if (getHitCounter() == 0)
//	GameEnviromentPG.addObjectToLevel(
//			new BonusBrick(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()));

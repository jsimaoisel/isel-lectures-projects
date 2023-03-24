package test.arkanoidPOO;
import pg.bgraphics.Point;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.IntersectionInfo;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.OnMoveParams;
import pg.graphicenviroment.RandomNumbers;

public class LinearMove {
	private int incX, incY;
	
	public LinearMove() {
		incX = 3;
		incY = -6;//RandomNumbers.generate(-10,-5);
	}
	public LinearMove(int incX, int incY) {
		this.incX = incX;
		this.incY = incY;
	}
	
	public void stickX(OnCollisionParams params) {
		if (incX < 0)
			params.actorPosition.x += params.intersectionInfo.getWidth();
		else
			params.actorPosition.x -= params.intersectionInfo.getWidth();		
	}
	
	public void stickY(OnCollisionParams params) {
		if (incY < 0)
			params.actorPosition.y += params.intersectionInfo.getHeight();
		else
			params.actorPosition.y -= params.intersectionInfo.getHeight();
	}
	
	public void invX() {
		incX = -incX;	
	}
	
	public void invY() {
		incY = -incY;
	}

	public void onMove(GEPoint point) {
		point.x += incX;
		point.y += incY;
	}
}

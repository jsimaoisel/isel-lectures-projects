package test.arkanoidPG;
import pg.bgraphics.BCircle;
import pg.bgraphics.BColor;
import pg.bgraphics.BRectangle;
import pg.graphicenviroment.GEPoint;
import pg.graphicenviroment.GameEnviroment;
import pg.graphicenviroment.GameEnviromentPG;


public class Racket {
	private BRectangle shape;
	private final int DELTA;
	public Racket() {
		DELTA = 10;
		shape = new BRectangle(200, 450, 70, 15, BColor.random(), BColor.random(), 1);
	}
	
	public BRectangle getShape() {
		return shape;
	}
	
	public void onKeyLeft() {
		//point.x -= DELTA
		Wall wall = GameEnviromentPG.getObjectByName("LeftW");
		if (shape.getX()-DELTA > wall.getShape().getX() + wall.getShape().getWidth())
			shape.move(shape.getX()-DELTA, shape.getY());
		else
			shape.move(wall.getShape().getX() + wall.getShape().getWidth() + 1, shape.getY());
	}
	
	public void onKeyRight() {
		// point.x += DELTA
		Wall wall = GameEnviromentPG.getObjectByName("RightW");
		if (shape.getX()+shape.getWidth()+DELTA < wall.getShape().getX())
			shape.move(shape.getX()+DELTA, shape.getY());
		else
			shape.move(wall.getShape().getX() - shape.getWidth() - 1, shape.getY());
	}
	
	/*
	public void onCollision(OnCollisionParams params, Wall w) {
		if (w == GameEnviromentPG.getObjectByName("LeftWall"))
			params.x += params.intersectionInfo.getWidth();
		else
			params.x -= params.intersectionInfo.getWidth();
	}
    */
}

package test.arkanoidPOO;
import pg.bgraphics.BCircle;
import pg.bgraphics.BColor;
import pg.bgraphics.BRectangle;
import pg.graphicenviroment.GameEnviroment;
import pg.graphicenviroment.GameEnviromentPOO;
import pg.graphicenviroment.OnCollisionParams;
import pg.graphicenviroment.interfaces.IActor;
import pg.graphicenviroment.interfaces.IGameObject;


public class Racket extends AbstractMovingObject {
	private BRectangle shape;
	private final int DELTA;
	public Racket() {
		DELTA = 10;
		shape = new BRectangle(200, 450, 70, 15, BColor.random(), BColor.random(), 1);
	}
	
	public BRectangle getShape() {
		return shape;
	}
	
	@Override
	public void onKeyLeft( ) {
		VWall wall = (VWall) GameEnviromentPOO.getObjectByName("LeftW");
		if (shape.getX()-DELTA > wall.getShape().getX() + wall.getShape().getWidth())
			shape.move(shape.getX()-DELTA, shape.getY());
		else
			shape.move(wall.getShape().getX() + wall.getShape().getWidth() + 1, shape.getY());
	}
	
	@Override
	public void onKeyRight( ) {
		VWall wall = (VWall) GameEnviromentPOO.getObjectByName("RightW");
		if (shape.getX()+shape.getWidth()+DELTA < wall.getShape().getX())
			shape.move(shape.getX()+DELTA, shape.getY());
		else
			shape.move(wall.getShape().getX() - shape.getWidth() - 1, shape.getY());
	}

	
}

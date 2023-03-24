package pg.graphicenviroment;

public class CollisionInfo {
	private GEDimension collisionDimension;
	public CollisionInfo(GEDimension collisionDimension) {
		this.collisionDimension = collisionDimension;
	}
	public CollisionInfo(int width, int height) {
		this.collisionDimension = new GEDimension(width, height);
	}
	public GEDimension getCollisionDimension() {
		return collisionDimension;
	}
}

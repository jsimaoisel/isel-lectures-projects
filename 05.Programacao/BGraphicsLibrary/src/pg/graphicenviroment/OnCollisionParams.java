package pg.graphicenviroment;

public class OnCollisionParams {
	public GEPoint actorPosition;
	public IntersectionInfo intersectionInfo;
//	public GameEnviroment gameEnviroment;
	public boolean isFinalCollision;
	public OnCollisionParams(GEPoint point, IntersectionInfo intersectionInfo, boolean isFinalCollision) { //, GameEnviroment gameEnviroment) {
		this.actorPosition = point;
		this.intersectionInfo = intersectionInfo;
//		this.gameEnviroment = gameEnviroment;
		this.isFinalCollision = isFinalCollision;
	}
}

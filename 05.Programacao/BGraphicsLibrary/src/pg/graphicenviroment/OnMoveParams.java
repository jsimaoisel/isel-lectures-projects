package pg.graphicenviroment;

public class OnMoveParams {
	public GEPoint newActorPosition;
	//public GameEnviroment gameEnviroment;
	public OnMoveParams(GEPoint actorPosition) { //, GameEnviroment gameEnviroment) {
		//this.gameEnviroment = gameEnviroment;
		this.newActorPosition = actorPosition;
	}
}

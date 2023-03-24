package pg.graphicenviroment;

import pg.graphicenviroment.interfaces.IMovingStrategy;

public class NullMovingStrategy implements IMovingStrategy {

	@Override
	public void onMove(OnMoveParams params, Object movingActor, Class<?> movingActorClass) {
		System.out.println("Null moving strategy");
	}

}

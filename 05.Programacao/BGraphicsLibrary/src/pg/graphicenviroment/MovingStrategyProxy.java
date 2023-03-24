package pg.graphicenviroment;

import pg.graphicenviroment.interfaces.IMovingStrategy;
import pg.reflectionutils.GenericObject;

public class MovingStrategyProxy extends GenericObject implements IMovingStrategy {
	
	public static boolean isMovingStrategy(Object untypedStrategy, Class<?> sourceClass) {
		return isCompatibleWithInterface(untypedStrategy, IMovingStrategy.class);
	}
	
	public MovingStrategyProxy(Object strategy) throws Exception {
		super(strategy, IMovingStrategy.class);
	}
	
	@Override
	public void onMove(OnMoveParams params, Object movingActor, Class<?> movingActorClass) {
		call("onMove", new Object[] { params, movingActor }, new Class<?>[] { OnMoveParams.class, movingActorClass });
	}

}

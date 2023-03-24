package pg.graphicenviroment;

import pg.graphicenviroment.interfaces.IActor;


public final class ActorProxy extends GameObjectProxy implements IActor  {
	
	public static boolean isActor(Object obj) {
		return isCompatibleWithInterface(obj, IActor.class);
	}
	
	//private IMovingStrategy movingStrategy;

	public ActorProxy(Object obj) throws Exception {
		super(obj, IActor.class);
	}

//	@Override
//	public IMovingStrategy getMovingStrategy() {
//		//if (movingStrategy == null) {
//		Object untypedStrategy = call("getMovingStrategy", new Object[] {}, new Class<?>[] {});
//		try {
//			if (untypedStrategy != movingStrategy)
//				return new MovingStrategyProxy(untypedStrategy);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			//movingStrategy = new NullMovingStrategy();
//		}
//		return movingStrategy;
//	}
	
	public void onKey(char key, String modifiers) {
		call("onKey", new Object[] { key, modifiers }, new Class<?>[] {char.class, String.class});
	}
	
	public void onKeyLeft() {
		call("onKeyLeft", new Object[] {  }, new Class<?>[] {});
	}
	
	public void onKeyRight() {
		call("onKeyRight", new Object[] {  }, new Class<?>[] { });
	}
	
	public void onKeyTop() {
		call("onKeyTop", new Object[] {  }, new Class<?>[] { });
	}

	public void onKeyDown() {
		call("onKeyDown", new Object[] {  }, new Class<?>[] {});
	}

	@Override
	public void onMove(GEPoint newActorPosition) {
		call("onMove", new Object[] { newActorPosition }, new Class<?>[] { GEPoint.class });
	}
}

package pg.graphicenviroment;

import pg.bgraphics.BShape;
import pg.graphicenviroment.interfaces.IGameObject;
import pg.reflectionutils.GenericObject;

public class GameObjectProxy extends GenericObject implements IGameObject {
	public static boolean isGameObject(Object obj) {
		//return hasMethod(obj, "getShape");
		return isCompatibleWithInterface(obj, IGameObject.class);
	}

//	private BShape representingBShape;
	
	public GameObjectProxy(Object obj) throws Exception {
		super(obj, IGameObject.class);
	}
	
	protected GameObjectProxy(Object obj, Class<?> interfaceType) throws Exception {
		super(obj, interfaceType);
	}

	@Override
	public final BShape getShape() {
//		if (representingBShape == null) {
//			Object untypedBShape = call("getShape", new Object[] {}, new Class<?>[] {});
//			if (GenericObject.isAssignableWith(untypedBShape, BShape.class))
//				representingBShape = (BShape) untypedBShape; 
//			else {
//				// TODO tratar resultado que não é BShape	
//			}
//		}
//		return representingBShape;
		return (BShape) call("getShape", new Object[] {}, new Class<?>[] {});
	}
	
//	@Override
//	public final void onCollision(OnCollisionParams params, Object colisionObject, Class<?> collisionObjectClass) {
//		call("onCollision", new Object[] {params, colisionObject}, 
// 			                new Class<?>[] {OnCollisionParams.class, collisionObjectClass});
//	}
	
//	@Override
//	public final void onCollision(OnCollisionParams params, Object colisionObject) {
//		call("onCollision", new Object[] {params, colisionObject}, 
// 			                new Class<?>[] {OnCollisionParams.class, colisionObject.getClass()});
//	}

	@Override
	public final void onCollision(OnCollisionParams params, IGameObject colisionObject) {
		try {
			GameObjectProxy proxy = (GameObjectProxy) colisionObject;
			Object realCollisionObject = proxy.getRealObject();
			call("onCollision", new Object[] {params, realCollisionObject}, 
		                new Class<?>[] {OnCollisionParams.class, realCollisionObject.getClass()});
		} catch (ClassCastException ex) {
			
		}
	}
}

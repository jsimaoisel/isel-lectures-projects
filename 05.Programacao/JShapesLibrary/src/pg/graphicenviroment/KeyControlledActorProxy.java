package pg.graphicenviroment;

import pg.reflectionutils.GenericObject;
import pg.reflectionutils.Optional;

public class KeyControlledActorProxy extends GameObjectProxy implements IKeyControlledActor {
	
	public KeyControlledActorProxy(Object obj) throws Exception {
		super(obj, IKeyControlledActor.class);
	}
	
	public void onKey(char key, String modifiers) {
		call("onKey", new Object[] { key, modifiers }, new Class<?>[] {char.class, String.class});
	}
	
	public void onKeyLeft() {
		call("onKeyLeft", new Object[] { }, new Class<?>[] { });
	}
	
	public void onKeyRight() {
		call("onKeyRight", new Object[] { }, new Class<?>[] { });
	}
	
	public void onKeyTop() {
		call("onKeyTop", new Object[] { }, new Class<?>[] { });
	}

	public void onKeyDown() {
		call("onKeyDown", new Object[] { }, new Class<?>[] { });
	}
}

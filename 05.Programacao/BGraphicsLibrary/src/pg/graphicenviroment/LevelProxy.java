package pg.graphicenviroment;

import pg.graphicenviroment.interfaces.ILevel;
import pg.reflectionutils.GenericObject;

public class LevelProxy extends GenericObject implements ILevel {

	protected LevelProxy(Object obj) throws Exception {
		super(obj, ILevel.class);
	}

	@Override
	public int onLevelLoad() {
		return ((Integer)call("onLevelLoad", new Object[]{}, new Class<?>[]{}));		
	}

	@Override
	public void onNewLife() {
		call("onNewLife", new Object[]{}, new Class<?>[]{});
	}

}

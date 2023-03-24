package pg.graphicenviroment;

import pg.graphicenviroment.interfaces.IGame;
import pg.graphicenviroment.interfaces.ILevels;
import pg.reflectionutils.GenericObject;

public class GameProxy extends GenericObject implements IGame {

	protected GameProxy(Object obj, Class<?> interfaceClass) throws Exception {
		super(obj, interfaceClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onGameLost() {
		call("onGameLost", new Object[] { }, new Class<?>[] { });			
	}

	@Override
	public void onGameWin() {
		call("onGameLost", new Object[] { }, new Class<?>[] { });					
	}

	@Override
	public void onLevelsSetup() {
		call("onLevelsSetup", new Object[] { }, new Class<?>[] {});			
	}

}

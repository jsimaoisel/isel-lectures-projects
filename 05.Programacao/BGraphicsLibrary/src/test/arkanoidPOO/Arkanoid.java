package test.arkanoidPOO;

import pg.graphicenviroment.GameEnviromentPOO;
import pg.graphicenviroment.interfaces.IGame;

public class Arkanoid implements IGame {

	@Override
	public void onGameLost() {
		System.exit(-1);
	}

	@Override
	public void onGameWin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLevelsSetup() {
		GameEnviromentPOO.addLevelToGame(new LevelOne());
	}

}

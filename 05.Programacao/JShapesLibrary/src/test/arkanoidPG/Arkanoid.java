package test.arkanoidPG;

import pg.graphicenviroment.GameEnviromentPG;
import test.arkanoidPG.LevelOne;

public class Arkanoid {
 	public void onLevelsSetup() {
		GameEnviromentPG.addLevelToGame(new LevelOne());
	}
	public void onGameLost() { }
 	public void onGameWin() { }
}

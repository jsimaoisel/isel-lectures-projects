package pg.graphicenviroment;

import java.awt.Rectangle;
import java.util.ArrayList;

import pg.bgraphics.BShape;
import pg.graphicenviroment.interfaces.IActor;
import pg.graphicenviroment.interfaces.IGame;
import pg.graphicenviroment.interfaces.IGameObject;
import pg.graphicenviroment.interfaces.ILevel;
import pg.graphicenviroment.interfaces.ILevels;
import pg.graphicenviroment.utils.Pair;
import test.arkanoidPOO.LevelOne;

class Levels implements ILevels {
	protected ArrayList<ILevel> levelsContainer;
	
	public Levels() {
		levelsContainer = new ArrayList<ILevel>();
	}
	
	@Override
	public void addLevel(ILevel level) {
		levelsContainer.add(level);
	}
}

public final class GameEnviromentPOO extends GameEnviromentBase<IGameObject> {
	private static GameEnviromentPOO gameEnviromentPOO = null;
	
	static {
				
	}
	
	/** game main loop */
	public static void run(long timeBase, int width, int height, String title, IGame game) {
		if (gameEnviromentPOO == null) {
			gameEnviromentPOO = new GameEnviromentPOO(timeBase, width, height, title);
			gameEnviromentPOO.run(game);
		}
	}

	public static void addObjectToLevel(IGameObject object) {
		if (gameEnviromentPOO == null)
			return;
		gameEnviromentPOO.addObject(object);
	}
	
	public static void addObjectToLevel(IGameObject object, String name) {
		if (gameEnviromentPOO == null)
			return;
		gameEnviromentPOO.addObject(object, name);
	}
	
	public static void removeOjectFromLevel(IGameObject object) {
		if (gameEnviromentPOO == null)
			return;
		gameEnviromentPOO.removeObject(object);
	}
	
	public static void addLevelToGame(ILevel levelOne) {
		if (gameEnviromentPOO == null)
			return;
		gameEnviromentPOO.addGameLevel(levelOne);
	}
	
	public static IGameObject getObjectByName(String name) {
		if (gameEnviromentPOO == null)
			return null;
		return gameEnviromentPOO.getByName(name);
	}

	public static void actorDied() {
		if (gameEnviromentPOO == null)
			return ;
		gameEnviromentPOO.mainActorDied();		
	}
	
//	private void run(IGame game) {
//		try {
//			game.onLevelsSetup();
//			for (int levelNumber=0; levelNumber<levels.size(); ++levelNumber) {
//				ILevel level = levels.get(levelNumber);
//				for (int numberOfLifes=level.onLevelLoad(); numberOfLifes>0; --numberOfLifes) {
//					level.onNewLife();
//					setMainActorAlive();
//					waitLevelEnd();
//					removeAllMovingObject();
//				}
//			}			
//			game.onGameLost();
//			return;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private GameEnviromentPOO(long timeBase, int width, int height, String title) {
		super(timeBase, width, height, title);
		canChangeWorld = true;
		// TODO Auto-generated constructor stub
	}
	
	private void addObject(IGameObject object) {
		addObject(object, String.valueOf(object.hashCode()));
	}
	
	private void addObject(IGameObject object, String name) {
		if (object instanceof IActor)
			addMovingObject((IActor)object, name);
		else
			addSceneOject(object, name);
	}

	private void removeObject(IGameObject object) {
		if (object instanceof IActor)
			removeMovingObject((IActor)object);
		else
			removeSceneObject(object);
	}
	
	private IGameObject getByName(String name) {
		return allObjects.get(name);
	}
	
	public static void main(String[] args) {
		GameEnviromentPOO.run(25, 500, 500, "Game", new test.arkanoidPOO.Arkanoid());
	}
}

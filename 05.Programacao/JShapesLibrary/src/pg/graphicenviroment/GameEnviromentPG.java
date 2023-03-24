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
import pg.reflectionutils.GenericObject;

//class Levels implements ILevels {
//	protected ArrayList<LevelProxy> levelsContainer;
//	
//	public Levels() {
//		levelsContainer = new ArrayList<LevelProxy>();
//	}
//	
//	@Override
//	public void addLevel(ILevel level) {
//		levelsContainer.add(level);
//	}
//}

public class GameEnviromentPG extends GameEnviromentBase<GameObjectProxy> {
	private static GameEnviromentPG gameEnviromentPG = null;

	/** game main loop */
	public static void run(long timeBase, int width, int height, String title, Object game) {
		if (gameEnviromentPG == null) {
			GameProxy proxy;
			try {
				proxy = new GameProxy(game, IGame.class);
				gameEnviromentPG = new GameEnviromentPG(timeBase, width, height, title);
				gameEnviromentPG.run(proxy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void addObjectToLevel(Object object) {
		if (gameEnviromentPG == null)
			return;
		gameEnviromentPG.addObject(object);
	}
	
	public static void addObjectToLevel(Object object, String name) {
		if (gameEnviromentPG == null)
			return;
		gameEnviromentPG.addObject(object, name);
	}
	
	public static void removeOjectFromLevel(Object object) {
		if (gameEnviromentPG == null)
			return;
		gameEnviromentPG.removeObject(object);
	}
	
	public static void addLevelToGame(Object levelOne) {
		if (gameEnviromentPG == null)
			return;
		try {
			ILevel level = new LevelProxy(levelOne);
			gameEnviromentPG.addGameLevel(level);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends Object> E getObjectByName(String name) {
		if (gameEnviromentPG == null)
			return null;
		E realObject = null;
		try {
			realObject = (E) gameEnviromentPG.getByName(name);
		} catch(ClassCastException ex) {
			ex.printStackTrace();
		}
		return realObject; 
	}

	public static void actorDied() {
		if (gameEnviromentPG == null)
			return ;
		gameEnviromentPG.mainActorDied();		
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
	
	protected GameEnviromentPG(long timeBase, int width, int height, String title) {
		super(timeBase, width, height, title);
	}

	public void internalAddObject(Object object, String name) throws Exception {
		GameObjectProxy proxy;
		if (GenericObject.isCompatibleWithInterface(object, IActor.class)) {
			proxy = new ActorProxy(object);
			addMovingObject(proxy, name);
		} else if (GenericObject.isCompatibleWithInterface(object, IGameObject.class)) {
			proxy = new GameObjectProxy(object);
			addSceneOject(proxy, name);
		} else {
			return;
		}		
	}
	
	private void addObject(Object object) {
		try {
			internalAddObject(object, String.valueOf(object.hashCode()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addObject(Object object, String name) {
		try {
			internalAddObject(object, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	private void removeObject(Object object) {
		try {
			if (GenericObject.isCompatibleWithInterface(object, IActor.class))
				removeSceneObject(new ActorProxy(object));
			else
				removeMovingObject(new GameObjectProxy(object));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Object getByName(String name) {
		return allObjects.get(name).getRealObject();
	}
	
//	@Override
//	protected ArrayList<Pair<Rectangle, ? extends IGameObject>> checkColision(IActor actor, BShape actorShape, GEPoint newPoint) 
//	{
//		ArrayList<Pair<Rectangle, ? extends IGameObject>> collisions = new ArrayList<Pair<Rectangle,? extends IGameObject>>();
//		Rectangle actorRectangle = new Rectangle(newPoint.x, newPoint.y, actorShape.getWidth(), actorShape.getHeight());
//		//if (objAndShape.shape.intersects(newPoint.x, newPoint.y, shape.getWidth(), shape.getHeight()))
//		for (IGameObject gameObject: allObjects.values()) {
//			if (actor.equals(gameObject)) 
//				continue;
//			BShape shape = gameObject.getShape();
//			Rectangle objRectangle = 
//				new Rectangle(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
//			Rectangle intersection = actorRectangle.intersection(objRectangle);
//			if (!intersection.isEmpty())
//				collisions.add(new Pair<Rectangle, IGameObject>(intersection, gameObject));
//		}
//		return collisions;
//
//	}
//	
//	@Override
//	protected void callOnCollision(OnCollisionParams params, IActor actor, IGameObject otherObject) {
//		actor.onCollision(params, otherObject);
//		otherObject.onCollision(params, actor);		
//	}
	
	public static void main(String[] args) {
		GameEnviromentPG.run(25, 500, 500, "Game", new test.arkanoidPG.Arkanoid());
	}
}

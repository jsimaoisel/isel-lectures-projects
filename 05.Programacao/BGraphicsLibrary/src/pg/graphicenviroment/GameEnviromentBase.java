package pg.graphicenviroment;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import pg.bgraphics.BShape;
import pg.bgraphics.GraphicWindowBase;
import pg.graphicenviroment.interfaces.IActor;
import pg.graphicenviroment.interfaces.IGame;
import pg.graphicenviroment.interfaces.IGameObject;
import pg.graphicenviroment.interfaces.ILevel;
import pg.graphicenviroment.utils.Pair;

public abstract class GameEnviromentBase<E extends IGameObject> extends GraphicWindowBase {
	
	/** all objects in the game */
	protected HashMap<String, E> allObjects;
	/** objects that move */
	private TreeSet<String> movingActors;
	/** objects names */
	private HashMap<E, String> objectsNames;
	/** game levels */
	protected ArrayList<ILevel> levels;

	/** moving objects to add when all moving objects have moved */
	private ArrayList<Pair<E, String>> actorsToAdd;
	/** moving objects to add when all moving objects have moved */
	private ArrayList<E> actorsToRemove;
	/** list of keys pending to be processed */
	private List<KeyEvent> keys;
	
	private int width;
	private int height;
	private boolean mainActorAlive;
	protected boolean canChangeWorld;
	protected long timeBase;
	
	protected GameEnviromentBase(long timeBase, int width, int height, String title) {
		super(title, width, height);
		this.width = width;
		this.height = height;
		this.timeBase = timeBase;
		
		allObjects = new HashMap<String, E>();
		movingActors = new TreeSet<String>();
		objectsNames = new HashMap<E, String>();
		
		levels = new ArrayList<ILevel>();
		
		actorsToAdd = new ArrayList<Pair<E,String>>();
		actorsToRemove = new ArrayList<E>();
		keys = Collections.synchronizedList(new LinkedList<KeyEvent>());
		
		mainActorAlive = false;
		canChangeWorld = false;
		
		getCanvas().setFocusable(true);
		getCanvas().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) { keys.add(e); }
		});
	}
	
	protected void run(IGame game) {
		try {
			game.onLevelsSetup();
			for (int levelNumber=0; levelNumber<levels.size(); ++levelNumber) {
				ILevel level = levels.get(levelNumber);
				for (int numberOfLifes=level.onLevelLoad(); numberOfLifes>0; --numberOfLifes) {
					level.onNewLife();
					setMainActorAlive();
					waitLevelEnd();
					removeAllMovingObject();
				}
			}			
			game.onGameLost();
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param object
	 * @param name
	 */
	protected final void addSceneOject(E object, String name) {
		allObjects.put(name, object);
		objectsNames.put(object, name);
		getCanvas().addShape(object.getShape());
	}

	/**
	 * Remove static object from game.
	 * @param object
	 */
	protected void removeSceneObject(E object) {
		String name = objectsNames.get(object);
		if (name == null) return;
		E gameObject = allObjects.get(name);
		allObjects.remove(name);
		objectsNames.remove(object);
		getCanvas().removeShape(gameObject.getShape());
	}
	
	/**
	 * 
	 * @param object
	 * @param name
	 */
	protected void addMovingObject(E object, String name) {
		actorsToAdd.add(new Pair<E, String>(object, name));
		objectsNames.put(object, name);	
	}
	
	protected void removeMovingObject(E object) {
		actorsToRemove.add(object);
	}
	
	protected void removeAllMovingObject() {
		for(String actor : movingActors) {
			IGameObject object = allObjects.get(actor);
			objectsNames.remove(object);
			allObjects.remove(actor);
			getCanvas().removeShape(object.getShape());
		}
		movingActors.clear();
	}
	
	protected void processKey() {
		if (keys.isEmpty()) return;
		KeyEvent event = keys.remove(0);
		for (String actors : movingActors) {
			IActor actor = (IActor) allObjects.get(actors);
			actor.onKey(event.getKeyChar(), KeyEvent.getKeyModifiersText(event.getModifiers()));
			switch(event.getKeyCode()) {
				case KeyEvent.VK_LEFT: actor.onKeyLeft(); break;
				case KeyEvent.VK_RIGHT: actor.onKeyRight(); break;
			}
		}
	}
	
	protected void mainActorDied() {
		mainActorAlive = false;
	}	
	
	protected boolean mainActorAlive() {
		return mainActorAlive;
	}
	
	protected void setMainActorAlive() {
		mainActorAlive = true;
	}
	
	protected void waitLevelEnd() {
		long start, end;
		while (mainActorAlive()) {
			start = System.currentTimeMillis();
			moveAll();
			processKey();
			end = System.currentTimeMillis()-start;
			try {
//				if (end < timeBase)
					Thread.sleep( timeBase );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
	}
	
	protected void addGameLevel(ILevel level) {
		levels.add(level);
	}
	
	private void moveAll() {
		// Remove pending actors
		for (E object : actorsToRemove) {
			String name = objectsNames.get(object);
			E obj = allObjects.remove(name);
			if (obj == null) continue;
			movingActors.remove(name);
			objectsNames.remove(object);
			getCanvas().removeShape(object.getShape());
		}
		actorsToRemove.clear();
		
		// Add pending actors
		for (Pair<E, String> actor : actorsToAdd) {
			allObjects.put(actor.second, actor.first);
			movingActors.add(actor.second);
			getCanvas().addShape(actor.first.getShape());
		}
		actorsToAdd.clear();
		
		for(String name: movingActors) {
			IActor actor  = (IActor) allObjects.get(name);

			// Move according to actor moving strategy
			BShape actorShape = actor.getShape();
			GEPoint newPoint = new GEPoint(actorShape.getX(), actorShape.getY());
			actor.onMove(newPoint);			

			// Check for all collisions
			// TODO: detectar colisão com o método intersection
			ArrayList<Pair<Rectangle, ? extends IGameObject>> collisionsInfo = checkColision(actor, actorShape, newPoint);
			
			// Send collision event to the actor
			for (int i=0; i<collisionsInfo.size(); ++i) {
				Pair<Rectangle, ? extends IGameObject> collisionInfo = collisionsInfo.get(i);
				IntersectionInfo intersectionInfo = 
					new IntersectionInfo(
							(int)collisionInfo.first.getX(), 
							(int)collisionInfo.first.getY(), 
							(int)collisionInfo.first.getWidth(), 
							(int)collisionInfo.first.getHeight());
				IGameObject otherObject = collisionInfo.second;
				boolean isFinalCollision = i+1==collisionsInfo.size();
				OnCollisionParams paramsObject1 = new OnCollisionParams(newPoint, intersectionInfo, isFinalCollision);
				GEPoint otherObjectPoint = new GEPoint(otherObject.getShape().getX(), otherObject.getShape().getY());
				OnCollisionParams paramsObject2 = new OnCollisionParams(otherObjectPoint, intersectionInfo, isFinalCollision);
//				actor.onCollision(params,
//								  otherObject.getRealObject(), 
//								  collisionInfo.second.getRealObjectClass());
//				otherObject.onCollision(params,
//										actor.getRealObject(),
//										actor.getRealObjectClass());
//				actor.onCollision(params, otherObject.getRealObject());
//				otherObject.onCollision(params, actor.getRealObject());
//				actor.onCollision(params, otherObject);
//				otherObject.onCollision(params, actor);
				callOnCollision(actor, paramsObject1, otherObject, paramsObject2);
//				actor.onCollision(intersectionInfo, otherObject);
//				otherObject.onCollision(intersectionInfo, actor);
//				if (otherObject instanceof IActor)
//					((IActor)otherObject).getShape().move(otherObjectPoint.x, otherObjectPoint.y);
			}
			actorShape.move(newPoint.x, newPoint.y);
		}
	}
	
	//@Override
	protected ArrayList<Pair<Rectangle, ? extends IGameObject>> checkColision(IActor actor, BShape actorShape, GEPoint newPoint) 
	{
		ArrayList<Pair<Rectangle, ? extends IGameObject>> collisions = new ArrayList<Pair<Rectangle,? extends IGameObject>>();
		Rectangle actorRectangle = new Rectangle(newPoint.x, newPoint.y, actorShape.getWidth(), actorShape.getHeight());
		//if (objAndShape.shape.intersects(newPoint.x, newPoint.y, shape.getWidth(), shape.getHeight()))
		for (IGameObject gameObject: allObjects.values()) {
			if (actor.equals(gameObject)) 
				continue;
			BShape shape = gameObject.getShape();
			Rectangle objRectangle = 
				new Rectangle(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
			Rectangle intersection = actorRectangle.intersection(objRectangle);
			if (!intersection.isEmpty()) {
				collisions.add(new Pair<Rectangle, IGameObject>(intersection, gameObject));
				//break;
			}
		}
		return collisions;
	}

	protected void callOnCollision(IActor actor, OnCollisionParams paramsActor, IGameObject otherObject, OnCollisionParams paramsObject) {
		actor.onCollision(paramsActor, otherObject);
		otherObject.onCollision(paramsObject, actor);		
	}

//	protected abstract ArrayList<Pair<Rectangle, ? extends IGameObject>> checkColision(IActor actor, BShape actorShape, GEPoint newPoint);

}

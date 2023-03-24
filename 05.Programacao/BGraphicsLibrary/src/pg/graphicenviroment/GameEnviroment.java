package pg.graphicenviroment;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import pg.bgraphics.BGraphics;
import pg.bgraphics.BShape;
import pg.bgraphics.Canvas;
import pg.bgraphics.GraphicWindowBase;
import pg.graphicenviroment.interfaces.IActor;
import pg.graphicenviroment.interfaces.IGame;
import pg.graphicenviroment.interfaces.IGameObject;
import pg.reflectionutils.GenericObject;

@Deprecated
public final class GameEnviroment extends GraphicWindowBase {

	public static void run(int timeBase, int width, int height, String title, Object game) {
		GameEnviroment gameEnviroment = new GameEnviroment((int)timeBase, width, height, title);
		LevelProxy levelProxy;
		Levels levels = new Levels();
		try {
			GameProxy gameProxy = new GameProxy(game, IGame.class);
			gameProxy.onLevelsSetup(levels);
			for (int levelNumber=0; levelNumber<levels.levelsContainer.size(); ++levelNumber) {
				levelProxy = levels.levelsContainer.get(levelNumber);
				for (int numberOfLifes=levelProxy.onLevelLoad(gameEnviroment); numberOfLifes>0; --numberOfLifes) {
					levelProxy.onNewLife(gameEnviroment);
					gameEnviroment.setMainActorAlive();
					waitLevelEnd(timeBase, gameEnviroment);
					gameEnviroment.removeAllActors();
				}
			}			
			gameProxy.onGameLost();
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void removeAllActors() {
		for(String actor : movingActors) {
			GameObjectProxy proxy = allObjects.get(actor);
			objectsNames.remove(new Integer(proxy.getRealObject().hashCode()));
			allObjects.remove(actor);
			getCanvas().removeShape(proxy.getShape());
		}
		keyListeners.clear();
		movingActors.clear();
	}

	private static void waitLevelEnd(int timeBase, GameEnviroment gameEnviroment) {
		long start, end;
		while (gameEnviroment.mainActorAlive()) {
			start = System.currentTimeMillis();
			gameEnviroment.moveAll();
			gameEnviroment.processKey();
			end = System.currentTimeMillis()-start;
			try {
				Thread.sleep( end < timeBase ? timeBase : 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	private static class Pair<T,K> {
		public Pair(T first, K second) {
			this.first = first;
			this.second = second;
		}
		public T first;
		public K second;
	}
	
	private HashMap<String, GameObjectProxy> allObjects;
	private TreeSet<String> movingActors;
	private Set<String> keyListeners;
	private Map<Integer, String> objectsNames;

	private ArrayList<Pair<ActorProxy, String>> actorsToAdd;
	private ArrayList<Integer> actorsToRemove;
	private List<KeyEvent> keys;
	
	private int width;
	private int height;
	private boolean mainActorAlive;
	
	protected GameEnviroment(long timeBase, int width, int height, String title) {
		super(title, width, height);
		this.width = width;
		this.height = height;
		
		this.allObjects = new HashMap<String, GameObjectProxy>();
		this.movingActors = new TreeSet<String>();
		this.objectsNames = new HashMap<Integer, String>();
		this.keyListeners = new TreeSet<String>();//Collections.synchronizedSet(new TreeSet<String>());

		this.actorsToRemove = new ArrayList<Integer>();
		this.actorsToAdd = new ArrayList<Pair<ActorProxy,String>>();

		this.keys = Collections.synchronizedList(new LinkedList<KeyEvent>());
		
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
	
	protected void moveAll() {
		boolean actorsChanged = false;
		
		// Remove pending actors
		for (Integer idx : actorsToRemove) {
			int hashCode = idx.intValue();
			String name = objectsNames.get(hashCode);
			GameObjectProxy proxy = allObjects.remove(name);
			if (proxy == null) continue;
			movingActors.remove(name);
			keyListeners.remove(name);
			getCanvas().removeShape(proxy.getShape());
			actorsChanged = true;
		}
		actorsToRemove.clear();
		
		// Add pending actors
		for (Pair<ActorProxy, String> actor : actorsToAdd) {
			allObjects.put(actor.second, actor.first);
			movingActors.add(actor.second);
			getCanvas().addShape(actor.first.getShape());
			actorsChanged = true;
		}
		actorsToAdd.clear();
		
		// Give the chance for all actors to move
		for(String name: movingActors) {
			ActorProxy actor  = (ActorProxy) allObjects.get(name);
			BShape actorShape = actor.getShape();
			 // TODO: Adicionar ponto a shape?
			//actor.onMove(new Old_OnMoveParams(newPoint, GameEnviroment.this));
			
//			IMovingStrategy movingStrategy = actor.getMovingStrategy();
//			if (movingStrategy != null)
//				movingStrategy.onMove(new OnMoveParams(newPoint, GameEnviroment.this), 
//					                         actor.getRealObject(), 
//					                         actor.getRealObjectClass());
			// Move according to actor moving strategy
			actor.onMove();
			GEPoint newPoint  = new GEPoint(actorShape.getX(), actorShape.getY());

			// Check for all collisions
			// TODO: detectar colisão com o método intersection
			ArrayList<Pair<Rectangle, ? extends GameObjectProxy>> collisionsInfo = checkColision(actor, actorShape, newPoint);
				
			// Send collision event to the actor
			for (int i=0; i<collisionsInfo.size(); ++i) {
				Pair<Rectangle, ? extends GameObjectProxy> collisionInfo = collisionsInfo.get(i);
				IntersectionInfo intersectionInfo = 
					new IntersectionInfo(
							(int)collisionInfo.first.getX(), 
							(int)collisionInfo.first.getY(), 
							(int)collisionInfo.first.getWidth(), 
							(int)collisionInfo.first.getHeight());
				GameObjectProxy otherObject = collisionInfo.second;
				OnCollisionParams params = 
					new OnCollisionParams(newPoint, intersectionInfo, 
										  i+1==collisionsInfo.size(), GameEnviroment.this);
//				actor.onCollision(params,
//								  otherObject.getRealObject(), 
//								  collisionInfo.second.getRealObjectClass());
//				otherObject.onCollision(params,
//										actor.getRealObject(),
//										actor.getRealObjectClass());
//				actor.onCollision(params, otherObject.getRealObject());
//				otherObject.onCollision(params, actor.getRealObject());
				actor.onCollision(params, otherObject);
				otherObject.onCollision(params, actor);
			}
			//actorShape.move(newPoint.x, newPoint.y);
		}
	}
	
	protected void processKey() {
		if (keys.isEmpty()) return;
		KeyEvent event = keys.remove(0);
		for (String actors : movingActors) {
			ActorProxy gameObjectProxy = (ActorProxy) allObjects.get(actors);
			gameObjectProxy.onKey(this, event.getKeyChar(), KeyEvent.getKeyModifiersText(event.getModifiers()));
			switch(event.getKeyCode()) {
				case KeyEvent.VK_LEFT: gameObjectProxy.onKeyLeft(this); break;
				case KeyEvent.VK_RIGHT: gameObjectProxy.onKeyRight(this); break;
			}
		}
	}
	
	protected boolean mainActorAlive() {
		return mainActorAlive;
	}
	
	protected void setMainActorAlive() {
		mainActorAlive = true;
	}
	
	private void internalAddObject(Object object, String name) {
		GameObjectProxy gameObjectProxy;
		try {
			gameObjectProxy = new GameObjectProxy(object);
			allObjects.put(name, gameObjectProxy);
			objectsNames.put(object.hashCode(), name);
			getCanvas().addShape(gameObjectProxy.getShape());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actorDied() {
		mainActorAlive = false;
	}
	
	public void addSceneOject(Object object) {
		internalAddObject(object, String.valueOf(object.hashCode()));
	}
	
	public void addSceneOject(Object object, String name) {
		internalAddObject(object, name);
	}
	
	public void addSceneObject(IActor actor) {
		System.out.println("ok!");
	}
		
	/**
	 * Remove static object from game. Can only be called by onCollision methods.
	 * @param object
	 */
	public void removeSceneObject(Object object) {
		if (!GameObjectProxy.isGameObject(object))
			return;
		int hashCode = object.hashCode();
		String name = objectsNames.get(hashCode);
		if (name == null)
			return;
		GameObjectProxy gameObjectProxy = allObjects.get(name);
		allObjects.remove(name);
		objectsNames.remove(hashCode);
		getCanvas().removeShape(gameObjectProxy.getShape());
	}
	
	// Explicar este método
	@SuppressWarnings("unchecked")
	public <E extends Object> E getObjectByName(String name) {
		E ret = null;
		GameObjectProxy gameObjectProxy = allObjects.get(name);
		if (gameObjectProxy != null) {
			try {
				ret = (E) gameObjectProxy.getRealObject();
			} catch (ClassCastException ex) { }			
		}			
		return ret;
	}
		
	private void internalAddActor(Object object, String name) throws Exception {
		ActorProxy actorProxy = new ActorProxy(object);
		actorsToAdd.add(new Pair<ActorProxy, String>(actorProxy, name));
		objectsNames.put(object.hashCode(), name);
	}
	
	public void addActor(Object object) {
		try {
			internalAddActor(object, String.valueOf(object.hashCode()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addActor(Object object, String name) {
		try {
			internalAddActor(object, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
//	private void internalKeyControlledObject(Object object, String name) throws Exception {
//		KeyControlledActorProxy actorProxy = new KeyControlledActorProxy(object);
//		allObjects.put(name, actorProxy);
//		objectsNames.put(object.hashCode(), name);
//		keyListeners.add(name);
//		getCanvas().addShape(actorProxy.getShape());
//	}
//	
//	public void addKeyControlledObject(Object object) {
//		try {
//			internalKeyControlledObject(object, String.valueOf(object.hashCode()));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void addKeyControlledObject(Object object, String name) {
//		try {
//			internalKeyControlledObject(object, name);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}
	
	/**
	 * Remove moving actor from game. Can only be called by onCollision methods.
	 * @param object
	 */
	public void removeActor(Object object) {
		if (!ActorProxy.isActor(object))
			return;
		int hashCode = object.hashCode();
		actorsToRemove.add(hashCode);
	}
	
//	private Rectangle calculateIntersectionInfo(BShape actorShape, BShape collisionShape) {
//		Rectangle actorRectangle = 
//			new Rectangle(actorShape.getX(), actorShape.getY(), actorShape.getWidth(), actorShape.getHeight());
//		Rectangle collisionShapeRectangle = 
//			new Rectangle(collisionShape.getX(), collisionShape.getY(), collisionShape.getWidth(), collisionShape.getHeight());
//		Rectangle intersection = actorRectangle.intersection(collisionShapeRectangle);
////		IntersectionInfo intersectionInfo = 
////			new IntersectionInfo((int)intersection.getX(), (int)intersection.getY(), Math.max(1,(int)intersection.getWidth()), Math.max(1, (int)intersection.getHeight()));
//		return intersection;
//	}

	private ArrayList<Pair<Rectangle, ? extends GameObjectProxy>> checkColision(ActorProxy actor, BShape actorShape, GEPoint newPoint) {
		ArrayList<Pair<Rectangle, ? extends GameObjectProxy>> collisions = new ArrayList<Pair<Rectangle,? extends GameObjectProxy>>();
		Rectangle actorRectangle = new Rectangle(newPoint.x, newPoint.y, actorShape.getWidth(), actorShape.getHeight());
		//if (objAndShape.shape.intersects(newPoint.x, newPoint.y, shape.getWidth(), shape.getHeight()))
		for (GameObjectProxy gameObject: allObjects.values()) {
			if (actor.getRealObject() == gameObject.getRealObject()) 
				continue;
			BShape shape = gameObject.getShape();
			Rectangle objRectangle = 
				new Rectangle(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
			Rectangle intersection = actorRectangle.intersection(objRectangle);
			if (!intersection.isEmpty())
				collisions.add(new Pair<Rectangle, GameObjectProxy>(intersection, gameObject));
		}
//		for (Pair<ActorProxy, BShape> objAndShape: movingActors) {
//			if (actor == objAndShape.first) continue;
//			Rectangle objRectangle = 
//				new Rectangle(objAndShape.second.getX(), objAndShape.second.getY(), objAndShape.second.getWidth(), objAndShape.second.getHeight());
//			Rectangle intersection = actorRectangle.intersection(objRectangle);
//			if (!intersection.isEmpty())
//				collisions.add(new Pair<Rectangle, GameObjectProxy>(intersection, objAndShape.first));
//		}
		return collisions;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
//
//	public void start() {
//		if (timer == null) 
//			timer = new Timer(true);
//		timer.scheduleAtFixedRate(new ActorsAnimator(), 0, timeBase);
//	}

	public static void main(String[] args) {
		GameEnviroment ge = new GameEnviroment(0, 0, 0, "");
		ge.addSceneObject(new IActor() {
			
			@Override
			public void onCollision(OnCollisionParams params, IGameObject colisionObject) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public BShape getShape() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void onMove() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKeyTop(GameEnviroment ge) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKeyRight(GameEnviroment ge) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKeyLeft(GameEnviroment ge) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKeyDown(GameEnviroment ge) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onKey(GameEnviroment ge, char key, String modifiers) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}

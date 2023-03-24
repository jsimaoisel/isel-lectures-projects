package test.arkanoidPOO;

import pg.graphicenviroment.GameEnviromentPOO;
import pg.graphicenviroment.Message;
import pg.graphicenviroment.interfaces.ILevel;

public class LevelOne implements ILevel {
	
	@Override
	public int onLevelLoad() {
//		GameEnviromentPOO.addSceneOject(new VWall(0, 0, 25, 500), "LeftW");
//		gameEnviroment.addSceneOject(new VWall(476, 0, 25, 500), "RightW");
//		gameEnviroment.addSceneOject(new HWall(25, 0, 451, 25));
//		gameEnviroment.addSceneOject(new HWall(25, 476, 451, 25), "Floor");
//		final int BrickWidth = 60;
//		final int BrickHeight = 30;
//		for (int col=0; col<7; ++col)
//			for (int lin=0; lin<7; ++lin) { 
//				gameEnviroment.addSceneOject(new Brick(26+col*BrickWidth, 50+lin*BrickHeight, BrickWidth, BrickHeight));
//			}
		GameEnviromentPOO.addObjectToLevel(new VWall(0, 0, 25, 500), "LeftW");
		GameEnviromentPOO.addObjectToLevel(new VWall(476, 0, 25, 500), "RightW");
		GameEnviromentPOO.addObjectToLevel(new HWall(25, 0, 451, 25));
		GameEnviromentPOO.addObjectToLevel(new HWall(25, 476, 451, 25), "Floor");
		final int BrickWidth = 60;
		final int BrickHeight = 30;
		for (int col=0; col<7; ++col)
			for (int lin=0; lin<7; ++lin) { 
				GameEnviromentPOO.addObjectToLevel(new Brick(26+col*BrickWidth, 50+lin*BrickHeight, BrickWidth, BrickHeight));
			}
		return 3;
	}

	@Override
	public void onNewLife() {
		Message.show("Get Ready");
		GameEnviromentPOO.addObjectToLevel(new Racket(), "MyRacket");
		GameEnviromentPOO.addObjectToLevel(new Ball(250,400));	
	}
}

package test.arkanoidPG;

import pg.graphicenviroment.GameEnviromentPG;
import pg.graphicenviroment.Message;
import test.arkanoidPG.Brick;

public class LevelOne  {

	public int onLevelLoad() {
		GameEnviromentPG.addObjectToLevel(new Wall(0, 0, 25, 500), "LeftW");
		GameEnviromentPG.addObjectToLevel(new Wall(476, 0, 25, 500), "RightW");
		GameEnviromentPG.addObjectToLevel(new Wall(25, 0, 451, 25));
		GameEnviromentPG.addObjectToLevel(new Wall(25, 476, 451, 25), "Floor");
		int brickWidth = 64, brickHeight = 30, startX = 27, startY = 50;
		for (int lin=0; lin<5; ++lin)
			for (int col=0; col<7; ++col) { 
				GameEnviromentPG.addObjectToLevel(new HitBrick(startX+col*brickWidth, startY+lin*brickHeight, brickWidth, brickHeight));
			}
		return 3;
	}

	public void onNewLife() {
		Message.show("Get Ready!!");
		GameEnviromentPG.addObjectToLevel(new Racket(), "MyRacket");
		GameEnviromentPG.addObjectToLevel(new Ball(250,400));	
	}
}

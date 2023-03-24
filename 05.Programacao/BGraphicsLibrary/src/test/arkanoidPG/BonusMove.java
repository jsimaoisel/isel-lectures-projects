package test.arkanoidPG;

import pg.graphicenviroment.OnMoveParams;

public class BonusMove {
	public void onMove(OnMoveParams params, BonusBrick bonus) {
		params.actorPosition.translate(0, 5);
	}
}

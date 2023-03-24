package pg.graphicenviroment.interfaces;

import pg.reflectionutils.Any;
import pg.reflectionutils.MustImplement;
import pg.reflectionutils.Optional;
import pg.reflectionutils.Proxyable;

@Proxyable
public interface IGame {
	@MustImplement
	public void onLevelsSetup();
	@MustImplement
	public void onGameWin();
	@MustImplement
	public void onGameLost();
}

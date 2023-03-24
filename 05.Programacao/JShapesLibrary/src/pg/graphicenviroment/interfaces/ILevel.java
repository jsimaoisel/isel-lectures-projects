package pg.graphicenviroment.interfaces;

import pg.reflectionutils.MustImplement;
import pg.reflectionutils.Optional;
import pg.reflectionutils.Proxyable;

@Proxyable
public interface ILevel {
	@MustImplement
	public int onLevelLoad();

	@Optional(checkSignature=true)
	public void onNewLife();
}

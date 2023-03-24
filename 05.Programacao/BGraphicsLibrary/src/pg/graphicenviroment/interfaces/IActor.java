package pg.graphicenviroment.interfaces;

import pg.graphicenviroment.GEPoint;
import pg.reflectionutils.MustImplement;
import pg.reflectionutils.Optional;
import pg.reflectionutils.Proxyable;

@Proxyable
public interface IActor extends IGameObject {
	@MustImplement
	public void onMove(GEPoint newActorPosition);
	@Optional(checkSignature=true)
	public void onKey(char key, String modifiers);
	@Optional(checkSignature=true)
	public void onKeyLeft();
	@Optional(checkSignature=true)
	public void onKeyRight();
	@Optional(checkSignature=true)
	public void onKeyTop();
	@Optional(checkSignature=true)
	public void onKeyDown();
}

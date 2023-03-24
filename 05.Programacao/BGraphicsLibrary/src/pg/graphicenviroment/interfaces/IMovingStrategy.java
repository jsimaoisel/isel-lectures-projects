package pg.graphicenviroment.interfaces;

import pg.graphicenviroment.OnMoveParams;
import pg.reflectionutils.IgnoreParam;
import pg.reflectionutils.MustImplement;
import pg.reflectionutils.Proxyable;

@Proxyable
public interface IMovingStrategy {
	@MustImplement
	public void onMove(OnMoveParams params, Object movingActor, @IgnoreParam Class<?> movingActorClass);
}

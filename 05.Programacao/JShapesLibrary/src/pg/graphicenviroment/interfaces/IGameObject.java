package pg.graphicenviroment.interfaces;

import pg.bgraphics.BShape;
import pg.graphicenviroment.IntersectionInfo;
import pg.graphicenviroment.OnCollisionParams;
import pg.reflectionutils.Any;
import pg.reflectionutils.IgnoreParam;
import pg.reflectionutils.MustImplement;
import pg.reflectionutils.Optional;
import pg.reflectionutils.Proxyable;

@Proxyable
public interface IGameObject {
	@MustImplement
	public BShape getShape();
//	@Optional(checkSignature = true)
//	public void onCollision(OnCollisionParams params, Object colisionObject, @IgnoreParam Class<?> colisionObjectClass);
	@Optional(checkSignature = true)
	public void onCollision(OnCollisionParams params, @Any IGameObject colisionObject);
//	public void onCollision(IntersectionInfo intersectionInfo, @Any IGameObject colisionObject);
}

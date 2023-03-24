package pg.graphicenviroment.interfaces;

import pg.reflectionutils.Any;
import pg.reflectionutils.MustImplement;

public interface ILevels {
	@MustImplement
	public void addLevel(@Any ILevel level);
}

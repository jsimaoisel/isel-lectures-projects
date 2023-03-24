package pg.bgraphics;

public class BGraphicException extends RuntimeException {
	private Exception ex;
	
	public BGraphicException(Exception ex) {
		this.ex = ex;
	}
	
	@Override
	public String toString() {
		return ex.toString();
	}
}

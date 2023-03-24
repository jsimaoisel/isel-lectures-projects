package pg.bgraphics;

import java.awt.Color;

import pg.graphicenviroment.RandomNumbers;

public class BColor {
	
	public static BColor RED = new BColor(Color.RED);
	public static BColor GREEN = new BColor(Color.GREEN);
	public static BColor BLUE = new BColor(Color.BLUE);
	public static BColor None = new BColor(-1);
	
	public static BColor random() {
		return new BColor(RandomNumbers.generate(0, 255), RandomNumbers.generate(0, 255), RandomNumbers.generate(0, 255));
	}
	
	private Color color;
	
	protected BColor(Color color) {
		this.color = color;
	}
	protected Color getAWTColor() {
		return color;
	}
	
	/**
	 * Builds a BColor object with a user-defined red-green-blue value.
	 * @param color Red-Green-Blue components represented as an integer. Red: Bit0-7, Green: Bit8-15, Blue: Bit16-23.  				
	 */
	public BColor(int color) {
		try {
			this.color = Color.decode(String.valueOf(color));
		} catch (NumberFormatException ex) {
//			throw new BGraphicException(ex);
		}
	}	
	public BColor(int red, int green, int blue) {
		color = new Color(red, green, blue);
	}
	public int getRGB() {
		return (color.getRed()<<16) | (color.getGreen()<<8) | (color.getBlue());
	}
	public int getRed() {
		return color.getRed(); 
	}
	public int getGreen() {
		return color.getGreen();
	}
	public int getBlue() {
		return color.getBlue();
	}
}

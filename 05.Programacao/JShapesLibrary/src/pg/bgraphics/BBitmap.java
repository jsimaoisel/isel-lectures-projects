package pg.bgraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.ref.SoftReference;

import javax.swing.SwingUtilities;

import org.w3c.dom.css.RGBColor;

public class BBitmap extends BShape {
	private int x, y;
	private int columns;
	private int lines;
	private SoftReference<BColor[]> pixelsColors;
	private BufferedImage bufferedImage;
	
	protected BBitmap(Canvas owner, int x, int y, int columns, int lines) {
		super(owner);
		this.x = x;
		this.y = y;
		this.columns = columns;
		this.lines = lines;
		pixelsColors = new SoftReference<BColor[]>(new BColor[lines * columns]);
		bufferedImage = new BufferedImage(columns, lines, BufferedImage.TYPE_INT_ARGB);
	}
	
	public BBitmap(int x, int y, int columns, int lines) {
		this(null,x,y,columns,lines);
	}
	
	@Override
	public int getHeight() {
		return lines;
	}

	@Override
	public int getWidth() {
		return columns;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bufferedImage, x, y, null);
	}

	@Override
	protected void internalMove(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	public void setPixelColor(int x, int y, int color) {
		setPixelColor(x, y, new BColor(color));
	}
	
	public void setPixelColor(final int x, final int y, final BColor color) {
		owner.executeOnGUIThread(new Runnable() {
			@Override
			public void run() {
				pixelsColors.get()[y*columns + x] = color;
				bufferedImage.setRGB(x, y, color.getAWTColor().getRGB());
			};
		});
	}
	
	public BColor[] getPixels() {
		return pixelsColors.get();
	}
	
	public void setPixels(BColor[] pixels) {
		pixelsColors = new SoftReference<BColor[]>(pixels.clone());
	}
}

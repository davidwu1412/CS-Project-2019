import java.awt.Color;
import java.awt.Dimension;

public class Shape {
	protected int x = 0;
	protected int y = 0;
	protected int h = 0;
	protected int w = 0;
	protected Color colour;
	public Shape(int x, int y, int height, int width, Color colour) {
		this.x = x;
		this.y = y;
		this.h = height;
		this.w = width;
		this.colour = colour;
	}
	public int[] draw(int[] pixels, Dimension screen) {
		for(int i = x; i < x+w; ++i) {
			for(int j = y; j < y+h; ++j) {
				pixels[i+j*(int)screen.getWidth()] = this.colour.getRGB();
			}
		}
		return pixels;
	}
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("unused")
public class Sprite{
	private int x = 0;
	private int y = 0;
	private int h = 0;
	private int w = 0;
	private int[] pixels;
	private static Color transparent = new Color(0,0,0,0);
	private BufferedImage image;
	public Sprite(int x, int y, String image) {
		try {
			this.image = ImageIO.read(new File(image));
		} catch (IOException e) {
			System.out.println("Sprite missing, fatal error, exiting");
			System.exit(0);
		}
		this.x = x;
		this.y = y;
		this.h = this.image.getHeight();
		this.w = this.image.getWidth();
		pixels = new int[w*h];
		System.out.println(x);
		this.image.getRGB(0, 0, w, h, pixels, 0, w);
	}
	public int[] draw(int[] pixels, Dimension screen) {
		int width = 0;
		int height = 0;
		for(int i = x; i < x+w; ++i) {
			height = 0;
			for(int j = y; j < y+h; ++j) {
				if(this.pixels[width+height*this.w]!= transparent.getRGB()) {
					pixels[i+j*(int)screen.getWidth()] = this.pixels[width+height*this.w];
				}
				height += 1;
			}
			width += 1;
		}
		return pixels;
	}
}

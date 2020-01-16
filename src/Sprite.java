import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite {
	protected int x = 0;
	protected int y = 0;
	protected int h = 0;
	protected int w = 0;
	protected int[] pixels;
	protected boolean display = true;
	protected static Color transparent = new Color(0,0,0,0);
	protected BufferedImage image;
	public Sprite(int x, int y, String image) {
		try {
			if(!image.equalsIgnoreCase("none")) {
				this.image = ImageIO.read(new File(image));
				this.x = x;
				this.y = y;
				this.h = this.image.getHeight();
				this.w = this.image.getWidth();
				pixels = new int[w*h];
				this.image.getRGB(0, 0, w, h, pixels, 0, w);
			}
		} catch (IOException e) {
			System.out.println("Error 5: Sprite missing, fatal error, exiting");
			System.exit(0);
		}
	}
	public Sprite(String image) {
		try {
			if(!image.equalsIgnoreCase("none")) {
				this.image = ImageIO.read(new File(image));
				this.h = this.image.getHeight();
				this.w = this.image.getWidth();
				pixels = new int[w*h];
				this.image.getRGB(0, 0, w, h, pixels, 0, w);
			}
		} catch (IOException e) {
			System.out.println("Error 5: Sprite missing, fatal error, exiting");
			System.exit(0);
		}
	}
	public boolean setVisible(boolean visibility) {
		display = visibility;
		return visibility;
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int[] draw(int[] pixels, Dimension screen) {
		if(display) {
			int width = 0;
			int height = 0;
			for(int i = x; i < x+w; ++i) {
				height = 0;
				for(int j = y; j < y+h; ++j) {
					if(this.pixels[width+height*this.w] != transparent.getRGB()) {
						pixels[i+j*(int)screen.getWidth()] = this.pixels[width+height*this.w];
					}
					height += 1;
				}
				width += 1;
			}
		}
		return pixels;
	}
	public boolean getClicked(int x, int y) {
		if(this.x <= x && x <= this.x+w && this.y <= y && y <= this.y+h && display) return true;
		return false;
	}
}

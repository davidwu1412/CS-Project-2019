import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.*;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings({"unused"})
public class Game extends JFrame implements MouseListener, Runnable{
	public final Dimension screenSize;
	private static final long serialVersionUID = 1L;
	private static ArrayList<MinerObject> miners = new ArrayList<MinerObject>();
	private static ArrayList<Upgrade> upgrades = new ArrayList<Upgrade>();
	private boolean running = false;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	private Sprite s;
	public Game() {
		screenSize = new Dimension(500,500);
		setSize((int)screenSize.getWidth(),(int)screenSize.getHeight());
		setResizable(false);
		setTitle("Bitcoin Tycoon");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		setVisible(true);
		s = new Sprite(100,100,"resources/death_machine.png");
		thread = new Thread(this);
		image = new BufferedImage((int)screenSize.getWidth(),(int)screenSize.getHeight(),BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		start();
	}
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				clearScreen();
				pixels = s.draw(pixels, screenSize);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	public synchronized void start() {
		running = true;
		thread.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			System.out.print(e.getStackTrace());
		}
	}
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}
	public void clearScreen() {
		for(int i = 0; i < pixels.length; ++i) {
			pixels[i] = Color.WHITE.getRGB();
		}
	}
	public void mouseClicked(MouseEvent arg0) {
		System.out.println(arg0);
	}
	public void mouseEntered(MouseEvent arg0) {
		
	}
	public void mouseExited(MouseEvent arg0) {
		
	}
	public void mousePressed(MouseEvent arg0) {
		
	}
	public void mouseReleased(MouseEvent arg0) {
		
	}
	public static void main(String[] args) {
		new Game();
	}
}
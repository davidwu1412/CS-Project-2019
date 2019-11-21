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
public class Game extends JFrame implements MouseListener, Runnable {
	public final Dimension screenSize;
	private static final long serialVersionUID = 1L;
	private ArrayList<Device> devices;
	private int currentDevice = -1;
	private boolean running = false;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	private Sprite s;
	public Game() {
		devices = new ArrayList<Device>();
		devices.add(new Device("Arduino Nano", "It somehow works, it just does, don't question it.", "none"));
		devices.add(new Device("Arduino", "Only slightly better, at least it's larger.", "none"));
		devices.add(new Device("Arduino Mega", "Much larger, still not much better.", "none"));
		devices.add(new Device("Casual Laptop", "Finally, something real although sketchy.", "none"));
		devices.add(new Device("Business Laptop", "Wow! Actually capable of getting some BTC.", "none"));
		devices.add(new Device("Gaming Laptop", "A beast for it's size.", "none"));
		devices.add(new Device("Mini Desktop", "Marginally better than a laptop.", "none"));
		devices.add(new Device("Slightly Larger Desktop", "A little better.", "none"));
		devices.add(new Device("PC Desktop", "Wow! Can actually run Windows 10.", "none"));
		devices.add(new Device("High-End Desktop", "Wow! Can actually run Minecraft.", "none"));
		devices.add(new Device("Gaming Desktop", "Amazing top of the line performance.", "none"));
		devices.add(new Device("Mom's Basement", "First server bois.", "none"));
		devices.add(new Device("Ubisoft Server", "Practically non-existent", "none"));
		devices.add(new Device("Server Farm", "Finally, you've reached the end-game", "none"));
		readSaveFile();
		currentDevice = 0;
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
	public void readSaveFile() {
		
	}
	public void mouseClicked(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void mousePressed(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	public static void main(String[] args) {
		new Game();
	}
}
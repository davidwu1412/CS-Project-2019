import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings({"unused"})
public class Game extends JFrame implements MouseListener, Runnable{
	private static final long serialVersionUID = 1L;
	private static ArrayList<MinerObject> miners = new ArrayList<MinerObject>();
	private boolean running = false;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	public Game() {
		thread = new Thread(this);
		image = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	}
	public void run() {
		while(running) {
			
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

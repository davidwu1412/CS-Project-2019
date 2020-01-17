import java.util.concurrent.Semaphore;

public class RNG implements Runnable {
	public boolean running = false;
	private Semaphore sem;
	private long timeCounter;
	private long timerStartTime;
	public RNG(Semaphore s) {
		sem = s;
		timeCounter = 0;
		timerStartTime = 0;
	}
	public static boolean generateNumber(double chance) {
		double output = Math.random();
		if(output <= chance/100.0) {
			return true;
		} else {
			return false;
		}
	}
	public void run() {
		timerStartTime = System.currentTimeMillis();
		while(running) {
			try {
				timeCounter = System.currentTimeMillis()-timerStartTime;
				Game.timerFrame = (int)Math.round(100.0*timeCounter/(Player.period*1000));
				if(Game.timerFrame > 100) {
					Game.timerFrame = 0;
				}
				if(timeCounter >= Player.period*1000) {
					sem.acquire();
					boolean temp = generateNumber(Player.chance);
					if(temp) {
						Game.player.giveBTC(temp);
					}
					sem.release();
					timerStartTime = System.currentTimeMillis();
				} else {
					Thread.sleep(5);
				}
			} catch(InterruptedException e) {
				System.out.println("Error 6: RNG Thread unable to sleep");
				System.exit(0);
			}
		}
	}
}

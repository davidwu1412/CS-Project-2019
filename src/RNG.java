// Shawn Hu and David Wu
// Jan 17th, 2020
// This is the class for RNG objects and associated methods for putting things on the screen. 

import java.util.concurrent.Semaphore;

public class RNG implements Runnable { // Runnable for thread
	public boolean running = false; // flag for running or not
	private Semaphore sem; // semaphore for permit
	private long timeCounter; // the time variables
	private long timerStartTime;
	public RNG(Semaphore s) { // constructor
		sem = s;
		timeCounter = 0;
		timerStartTime = 0;
	}
	// This method generates a number and compares it to the chance value. 
	// Parameters: chance value
	// Returns true if it is smaller, false if not. (Basically checks if the bitcoin mined or not)
	public static boolean generateNumber(double chance) {
		double output = Math.random();
		if(output <= chance/100.0) {
			return true;
		} else {
			return false;
		}
	}
	// This method runs the thread.
	public void run() {
		timerStartTime = System.currentTimeMillis();	//grabs the initial start time
		while(running) {	//runs infinitely while running == true
			try {
				timeCounter = System.currentTimeMillis()-timerStartTime;	//checks how much time is passed since timer start
				Game.timerFrame = (int)Math.round(100.0*timeCounter/(Player.period*1000));	//sets the timer frame to much
				if(Game.timerFrame > 100) {	//just to prevent the image index exceeding the number of images
					Game.timerFrame = 0;
				}
				if(timeCounter >= Player.period*1000) {		//timer is triggered
					sem.acquire();	//acquires permit
					boolean temp = generateNumber(Player.chance);	//generates number
					if(temp) {
						Game.player.giveBTC(temp);	//give btc if successful trigger
					}
					sem.release();	//releases permit
					timerStartTime = System.currentTimeMillis();	//resets timer start time
				} else {	//time hasn't reached yet
					Thread.sleep(5);	//sleep for a bit
				}
			} catch(InterruptedException e) {
				System.out.println("Error 6: RNG Thread unable to sleep");
				System.exit(0);
			}
		}
	}
}

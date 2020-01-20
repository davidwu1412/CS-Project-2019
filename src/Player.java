// Shawn Hu and David Wu
// Jan 17th, 2020
// This is the class for Player and related methods. It contains the player information, game save information. 

public class Player {
	public static double chance = 20; // these are the stats of the player, the values here are default values.
	public static double period = 10;
	public static double value = 0.01;
	public static boolean giveBTC = false; // a boolean to check if the player successfully mined Bitcoin or not
	public static double conversionRate = 1026.35;
	public static double totalBTC = 0;
	private Device currentDevice; //Which device the player has and how much money the player has. 
	private double USD = 0;
	private double BTC = 0;
	private boolean blackMarketUnlock = false; // a boolean to see if the player has unlocked the black market or not.
	public Player() {
	}
	// getter/setter methods
	public void setDevice(Device d) {
		currentDevice = d;
	}
	public Device getDevice() {
		return currentDevice;
	}
	// these methods just add or set the money values. 
	public void addUSD(double USD) {
		this.USD += USD;
	}
	public void setUSD(double USD) {
		this.USD = USD;
	}
	public void setBTC(double BTC) {
		if(BTC > this.BTC) {
			totalBTC += BTC;
		}
		this.BTC = BTC;
	}
	public void setUpBTC(double BTC) {
		this.BTC = BTC;
	}
	public void addBTC(double BTC) {
		this.BTC += BTC;
		totalBTC += BTC;
	}
	public double getUSD() {
		return USD;
	}
	public double getBTC() {
		return BTC;
	}
	public void addCurrency(double money, int type) {
		if(type == 0) {
			this.BTC += money;
		} else if(type == 1) {
			this.USD += money;
		}
	}
	// sets the black market unlock flag
	public void unlockBlackMarket() {
		blackMarketUnlock = true;
	}
	public void lockBlackMarket() {
		blackMarketUnlock = false;
	}
	public boolean blackMarketStatus() {
		return blackMarketUnlock;
	}
	// This method makes sure the stats don't go past limits. 
	public void regulateValues() {
		if(chance < 5) {
			chance = 5;
		} else if(chance > 100) {
			chance = 100;
		}
		if(period <= 0) {
			period = 0.1;
		} else if(period > 20) {
			period = 20;
		}
		if(value <= 0) {
			value = 0.0001;
		}
	}
	// This methods adjusts the money values. 
	public void giveBTC(boolean input) {
		if(input) {
			this.BTC += Player.value;
			totalBTC += Player.value;
		}
	}
	// This method resets the player data. 
	public void reset() {
		blackMarketUnlock = false;
		chance = 20;
		value = 0.01;
		period = 10;
		BTC = 0.5;
		USD = 0;
		totalBTC = 0.5;
	}
}
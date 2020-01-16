
public class Player {
	public static double chance = 20;
	public static double period = 10;
	public static double value = 0.01;
	public static boolean giveBTC = false;
	public static double conversionRate = 1026.35;
	public static double totalBTC = 0;
	private Device currentDevice;
	private double USD = 0;
	private double BTC = 0;
	private boolean blackMarketUnlock = false;
	public Player() {
	}
	public void setDevice(Device d) {
		currentDevice = d;
	}
	public Device getDevice() {
		return currentDevice;
	}
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
	public void unlockBlackMarket() {
		blackMarketUnlock = true;
	}
	public void lockBlackMarket() {
		blackMarketUnlock = false;
	}
	public boolean blackMarketStatus() {
		return blackMarketUnlock;
	}
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
	public void giveBTC(boolean input) {
		if(input) {
			this.BTC += Player.value;
			totalBTC += Player.value;
		}
	}
}
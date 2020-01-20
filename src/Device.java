// Shawn Hu and David Wu
// Jan 17th, 2020
// This is the class for Device objects and related methods. 

import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Device {
	protected String name = ""; // name of device
	protected ArrayList<Upgrade> availableUpgrades; // list of avalible upgrades
	protected LinkedList<Upgrade> currentUpgrades; // list of current upgrades in the device
	protected JLabel image; // image of the device (left hand side image)
	protected JLabel image2;	//second image of the device (right hand side image)
	protected double price;	//price of the device
	private double baseChance;	//base stats of the device
	private double basePeriod;
	private double baseValue;
	private int tier; // basically tells the buying order
	//constructors
	public Device(String name, String image, double price, int tier, double chance, double period, double value) {
		this.image = new JLabel(new ImageIcon(image));	//sets the images
		this.image2 = new JLabel(new ImageIcon(image));
		this.image.setBounds(50,150,300,300);	//sets image bounds
		this.image2.setBounds(600,150,300,300);
		this.name = name;	//sets name
		availableUpgrades = new ArrayList<Upgrade>();	//inits both lists
		currentUpgrades = new LinkedList<Upgrade>();
		this.price = price;	//sets price and tier
		this.tier = tier;
		baseChance = chance;	//sets base stats
		basePeriod = period;
		baseValue = value;
	}
	//constructor used for binary search
	public Device(int tier) {
		this.tier = tier;
	}
	//getter/setter methods
	public double getBaseChance() {
		return baseChance;
	}
	public double getBasePeriod() {
		return basePeriod;
	}
	public double getBaseValue() {
		return baseValue;
	}
	public LinkedList<Upgrade> getUpgrades() {
		return currentUpgrades;
	}
	public ArrayList<Upgrade> availableUpgrades() {
		return availableUpgrades;
	}
	// These methods add to the different upgrades lists. 
	public void addUpgrade(Upgrade u) {
		currentUpgrades.add(u);
	}
	public void addAvailableUpgrade(Upgrade u) {
		availableUpgrades.add(u);
	}
	public int getTier() {
		return tier;
	}
	// buy(Player) -> boolean
	// checks if the player has enough money to buy the next device then buys it if true
	public boolean buy(Player player) {
		if(player.getUSD() >= this.price) {	//enough money
			player.setDevice(this);	//set device
			player.setUSD(player.getUSD()-this.price);	//subtract money
			return true;	//return true (success)
		}
		return false;	//return false (insufficient funds)
	}
	
}

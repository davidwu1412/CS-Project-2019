// Shawn Hu and David Wu
// Jan 17th, 2020
// This is the class for Upgrade objects and related methods. 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class Upgrade {
	public static final long serialVersionUID = 1L;
	public static final int BTC = 0; // enum to make it easy to determine which currency to use
	public static final int USD = 1;
	public static final Map<String,Integer> defaultPrices = new HashMap<>(); // map of default prices
	public static final ArrayList<ArrayList<String>> modifiers = new ArrayList<ArrayList<String>>(); // arraylist of all possible modifiers (mainly used for blackmarketitem)
	protected String name; // variables for the associated information
	protected String type;
	protected double price;
	protected double priceIncrease;	//what to multiply the price buy everytime a purchase occurs
	protected int moneyType;
	protected int numOf;	//numOf this purchase currently
	protected boolean blackMarketItem;	//is it a black market item?
	protected JButton image;	//The internal jbutton
	protected String[] effect;	//effect is stored in a 1d array of size 3, stores in this order: attribute to modify, how to modify, value to increase by (percentage)
	//Constructor (obsolete, but kept for clarity because although second constructor is used more often it is based off this constructor)
	public Upgrade(String name, String type, int price, int moneyType, boolean blackMarketItem, String[] effect, String imageLoc) {
		this.name = name;	//set basic stats
		this.type = type;
		this.price = price;
		if(moneyType < 2) {	//ensure that an invalid money type is not provided
			this.moneyType = moneyType;
		} else {
			System.out.println("Error 4: Invalid Money Type");
			System.exit(0);
		}
		numOf = 0;
		this.blackMarketItem = blackMarketItem;
		this.effect = effect;
		priceIncrease = 1.1;
		image = new JButton(new ImageIcon(imageLoc));	//sets the internal JButton
		image.setVisible(false);
		if(modifiers.isEmpty()) {	//checks if modifiers have been declared yet
			ArrayList<String> input = new ArrayList<String>();
			input.add("chance");
			input.add("period");
			input.add("value");
			modifiers.add(new ArrayList<String>(input));
			input = new ArrayList<String>();
			input.add("multiply");
			input.add("add");
			modifiers.add(new ArrayList<String>(input));
			setDefaultPrices();
		}
	}
	//Mainly used constructor
	public Upgrade(String name, String type, int moneyType, boolean blackMarketItem, String[] effect, StringTokenizer splitter) {
		this.name = name;	//Set basic stats
		this.type = type;
		this.price = Double.parseDouble(splitter.nextToken());
		if(moneyType < 2) {	//Ensure no invalid money types
			this.moneyType = moneyType;
		} else {
			System.out.println("Error 4: Invalid Money Type");
			System.exit(0);
		}
		numOf = Integer.parseInt(splitter.nextToken());	//How many?
		this.blackMarketItem = blackMarketItem;
		this.effect = effect;
		image = new JButton(new ImageIcon(splitter.nextToken()));	//Set internal JButton
		image.setVisible(false);
		priceIncrease = 1.1;
		if(modifiers.isEmpty()) {	//Set the modifiers if not already set
			ArrayList<String> input = new ArrayList<String>();
			input.add("chance");
			input.add("period");
			input.add("value");
			modifiers.add(new ArrayList<String>(input));
			input = new ArrayList<String>();
			input.add("multiply");
			input.add("add");
			modifiers.add(new ArrayList<String>(input));
			setDefaultPrices();
		}
	}
	//constructor for use with binarysearch
	public Upgrade(String type) {
		this.type = type;
	}
	// setter methods
	public static void setDefaultPrices() {
		defaultPrices.put("ProcessorUpgrade", 10);
		defaultPrices.put("GraphicsUpgrade", 20);
		defaultPrices.put("RAMUpgrade", 25);
		defaultPrices.put("BlackMarket", 5);
	}
	// This method resets the prices of the upgrades.
	public void resetPrices() {
		this.price = defaultPrices.get(this.type);
		this.numOf = 0;
	}
	// This method adds to the number of upgrades.
	public void addOne(Player player) {
		++numOf;
		applyEffects(player);
		this.price *= priceIncrease;
	}
	// This method checks to see if the player has enough money to purchase the upgrade. 
	// Returns true if they do have enough, false if not.
	// Player object parameter: player information
	public boolean makePurchase(Player player) {
		if(this.moneyType == BTC) {	//Money type == btc
			if(player.getBTC() > this.price) {	//checks if player has enough money
				player.setBTC(player.getBTC()-this.price);	//subtracts price from player money
				this.addOne(player);	//adds one to player
				return true;	//return true (success)
			} else {
				return false;	//return false (failure)
			}
		} else {	//money type == usd
			if(player.getUSD() > this.price) {	//checks if player has enough money
				player.setUSD(player.getUSD()-this.price);	//subtracts price from player money
				this.addOne(player);	//adds one to player
				return true;	//return true (success)
			} else {
				return false;	//return false (success)
			}
		}
	}
	// This method applies effects and checks the vlaues after a player makes a purchase. 
	// Return true if purchase made, false if not.
	// Player information is the parameter. 
	public boolean buy(Player player) {
		if(makePurchase(player)) {
			player.getDevice().addUpgrade(this);
			applyEffects(player);
			player.regulateValues();
			return true;
		}
		return false;
	}
	//This method applies the effect of the upgrade to the player
	//See above for how the effect is stored
	public void applyEffects(Player player) {
		if(effect[0].equalsIgnoreCase("chance")) {
			if(effect[1].equalsIgnoreCase("multiply")) {
				Player.chance *= 1+(Integer.parseInt(effect[2])/100.0);
			} else {
				System.out.println("Error 9: Invalid Modifier");
				System.exit(0);
			}
		} else if(effect[0].equalsIgnoreCase("period")) {
			if(effect[1].equalsIgnoreCase("multiply")) {
				Player.period *= 1-(Double.parseDouble(effect[2])/100.0);
			} else {
				System.out.println("Error 9: Invalid Modifier");
				System.exit(0);
			}
		} else if(effect[0].equalsIgnoreCase("value")) {
			if(effect[1].equalsIgnoreCase("multiply")) {
				Player.value *= 1+(Double.parseDouble(effect[2])/100.0);
			} else {
				System.out.println("Error 9: Invalid Modifier");
				System.exit(0);
			}
		} else {
			System.out.println("Error 8: Invalid attribute to modify");
			System.exit(0);
		}
	}
	@Override
	//Method to compare between the upgrades based on type (used in conjunction with indexOf)
	public boolean equals(Object o) {
		Upgrade i = (Upgrade) o;
		if(i.type.equalsIgnoreCase(this.type)) {
			return true;
		}
		return false;
	}
}

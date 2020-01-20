// Shawn Hu and David Wu
// Jan 17th, 2020
// This is the class for the Black Market upgrades. It extends the upgrade class.
// BlackMarketItem is a version of upgrade where you may not get the upgrade applied 100% of the time but it'll still waste your money anyways
// It's a gamble

import java.util.Random;
import java.util.StringTokenizer;

public class BlackMarketItem extends Upgrade {
	// Constructors
	public BlackMarketItem(String name, String[] effect, String imageLoc) {
		super(name, "BlackMarket", 0, Upgrade.BTC, true, effect, imageLoc);		//Call super to init class
		this.effect = generateEffect();		//Generate the first effect
		priceIncrease = 1.05;		//Set the rate at which the price inflates after each purchase
	}
	public BlackMarketItem(String name, String[] effect, StringTokenizer splitter) {
		super(name, "BlackMarket", Upgrade.BTC, true, effect, splitter);		//Call super to init class
		this.effect = generateEffect();	//Generate first effect
		priceIncrease = 1.05;	//Set rate at which price inflates after each purchase
	}
	// generateEffect(void) -> String[]
	// This method generates two effects and returns a string array of the effects that the BlackMarketItem will have.
	public String[] generateEffect() { 
		String[] output = new String[6];	//Creates new string arr
		Random r = new Random();	//Creates a random double generator
		output[0] = Upgrade.modifiers.get(0).get(r.nextInt(3));			//Picks a modifier at of the available modifiers
		output[1] = "multiply";											//Sets it to an multiplicative increase
		output[2] = Double.toString((r.nextDouble()*25)+5);				//Then choose a double between 5 and 30 as the percent to increase by
		do {
			output[3] = Upgrade.modifiers.get(0).get(r.nextInt(3));		//Now pick a modifier that was not the first one
			if(!output[3].equalsIgnoreCase(output[0])) {	//If the current one is not equal to the previous one, break
				break;
			}
		} while(true);
		output[4] = "divide";											//Set it to a multiplicative decrease
		output[5] = Double.toString((r.nextDouble()*10));				//Choose a value between 0 and 10 as the percent to decrease by
		return output;	//returns the string arr
	}
	// applyEffects(PlayeR) -> void
	// This method takes the currently generated effects and applies it to the player's stats
	public void applyEffects(Player player) {
		for(int i = 0; i < 2; ++i) {	//Loops twice to apply both effects
			if(effect[(3*i)].equalsIgnoreCase("chance")) {		//If the effect is supposed to modify chance of getting BTC on timer trigger
				if(effect[(3*i)+1].equalsIgnoreCase("multiply")) {		//Check if it's supposed to increase or decrease
					Player.chance *= 1+(Double.parseDouble(effect[(3*i)+2])/100);	//Apply the effect
				} else if(effect[(3*i)+1].equalsIgnoreCase("divide")){
					Player.chance *= 1-(Double.parseDouble(effect[(3*i)+2])/100);
				} else {	//It's not a valid modifier so print an error and exit
					System.out.println("Error 9: Invalid Modifier");
					System.exit(0);
				}
			} else if(effect[(3*i)].equalsIgnoreCase("period")) {	//If the effect is supposed to modify the period of each timer trigger
				if(effect[(3*i)+1].equalsIgnoreCase("multiply")) {		//Check if increasing or decreasing
					Player.period *= 1-(Double.parseDouble(effect[(3*i)+2])/100);	//Apply effect
				} else if(effect[(3*i)+1].equalsIgnoreCase("divide")) {		//For period it's flipped because you want to decrease the period when they say improve, key word is resused because i'm lazy
					Player.period *= 1+(Double.parseDouble(effect[(3*i)+2])/100);
				} else {
					System.out.println("Error 9: Invalid Modifier");
					System.exit(0);
				}
			} else if(effect[(3*i)].equalsIgnoreCase("value")) {	//if the effect is supposed to modify the value of BTC you can get on timer trigger
				if(effect[(3*i)+1].equalsIgnoreCase("multiply")) {
					Player.value *= 1+(Double.parseDouble(effect[(3*i)+2])/100);
				} else if(effect[(3*i)+1].equalsIgnoreCase("divide")) {
					Player.value *= 1-(Double.parseDouble(effect[(3*i)+2])/100);
				} else {
					System.out.println("Error 9: Invalid Modifier");
					System.exit(0);
				}
			} else {
				System.out.println("Error 8: Invalid attribute to modify");
				System.exit(0);
			}
		}
		this.effect = generateEffect();
	}
	// This method checks to see if the player has enough money to make the purchase they want to make. 
	// Parameters: the player object (player info)
	// Returns true if they have enough money, false if not. 
	public boolean makePurchase(Player player) {
		if(player.getBTC() > this.price && Math.random() < 0.5) {	//checks if the player has enough money and the randomness checks out
			player.setBTC(player.getBTC()-this.price);	//subtracts bitcoin
			this.addOne(player);	//adds one and applies effect
			return true;	//returns true (successful puchase)
		} else {
			return false;	//returns false (unsuccessful purchase)
		}
	}
}
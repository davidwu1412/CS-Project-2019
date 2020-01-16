import java.util.Random;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class BlackMarketItem extends Upgrade {
	public static final long serialVersionUID = 1L;
	public static ArrayList<String[]> allEffects = new ArrayList<String[]>();
	private int quality;
	public BlackMarketItem(String name, String[] effect, String imageLoc) {
		super(name, "BlackMarket", 0, Upgrade.BTC, true, effect, imageLoc);
		this.effect = generateEffect();
		quality = 2;
		priceIncrease = 1.05;
	}
	public BlackMarketItem(String name, String[] effect, StringTokenizer splitter) {
		super(name, "BlackMarket", Upgrade.BTC, true, effect, splitter);
		this.effect = generateEffect();
		quality = 2;
		priceIncrease = 1.05;
	}
	public String[] generateEffect() {
		String[] output = new String[6];
		Random r = new Random();
		output[0] = Upgrade.modifiers.get(0).get(r.nextInt(3));
		output[1] = "multiply";
		output[2] = Double.toString((r.nextDouble()*25)+25);
		do {
			output[3] = Upgrade.modifiers.get(0).get(r.nextInt(3));
			if(!output[3].equalsIgnoreCase(output[0])) {
				break;
			}
		} while(true);
		output[4] = "divide";
		output[5] = Double.toString((r.nextDouble()*10)+10);
		for(String i : output) {
			System.out.println(i);
		}
		return output;
	}
	public void setQuality(int newQuality) {
		this.quality = newQuality;
	}
	public void resetQuality() {
		this.quality = 2;
	}
	public void storeEffect() {
		allEffects.add(effect);
	}
	public void applyEffects(Player player) {
		for(int i = 0; i < quality; ++i) {
			if(effect[(3*i)].equalsIgnoreCase("chance")) {
				if(effect[(3*i)+1].equalsIgnoreCase("multiply")) {
					Player.chance *= 1+(Double.parseDouble(effect[(3*i)+2])/100);
				} else if(effect[(3*i)+1].equalsIgnoreCase("add")) {
					Player.chance += Double.parseDouble(effect[(3*i)+2]);
				} else if(effect[(3*i)+1].equalsIgnoreCase("divide")){
					Player.chance *= 1-(Double.parseDouble(effect[(3*i)+2])/100);
				} else {
					System.out.println("Error 9: Invalid Modifier");
					System.exit(0);
				}
			} else if(effect[(3*i)].equalsIgnoreCase("period")) {
				if(effect[(3*i)+1].equalsIgnoreCase("multiply")) {
					Player.period *= 1-(Double.parseDouble(effect[(3*i)+2])/100);
				} else if(effect[(3*i)+1].equalsIgnoreCase("add")) {
					Player.period -= Double.parseDouble(effect[(3*i)+2]);
				} else if(effect[(3*i)+1].equalsIgnoreCase("divide")) {
					Player.period *= 1+(Double.parseDouble(effect[(3*i)+2])/100);
				} else {
					System.out.println("Error 9: Invalid Modifier");
					System.exit(0);
				}
			} else if(effect[(3*i)].equalsIgnoreCase("value")) {
				if(effect[(3*i)+1].equalsIgnoreCase("multiply")) {
					Player.value *= 1+(Double.parseDouble(effect[(3*i)+2])/100);
				} else if(effect[(3*i)+1].equalsIgnoreCase("add")) {
					Player.value += Double.parseDouble(effect[(3*i)+2]);
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
		storeEffect();
		this.effect = generateEffect();
	}
	public boolean makePurchase(Player player) {
		if(this.moneyType == BTC) {
			if(player.getBTC() > this.price && Math.random() < 0.5) {
				player.setBTC(player.getBTC()-this.price);
				this.addOne(player);
				return true;
			} else {
				return false;
			}
		} else {
			if(player.getUSD() > this.price) {
				player.setUSD(player.getUSD()-this.price);
				this.addOne(player);
				return true;
			} else {
				return false;
			}
		}
	}
}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class Upgrade {
	public static final long serialVersionUID = 1L;
	public static final int BTC = 0;
	public static final int USD = 1;
	public static final Map<String,Integer> defaultPrices = new HashMap<>();
	public static final ArrayList<ArrayList<String>> modifiers = new ArrayList<ArrayList<String>>();
	protected String name;
	protected String type;
	protected double price;
	protected double priceIncrease;
	protected int moneyType = BTC;
	protected int numOf;
	protected boolean blackMarketItem;
	protected JButton image;
	protected String[] effect;
	public Upgrade(String name, String type, int price, int moneyType, boolean blackMarketItem, String[] effect, String imageLoc) {
		super();
		this.name = name;
		this.type = type;
		this.price = price;
		if(moneyType < 2) {
			this.moneyType = moneyType;
		} else {
			System.out.println("Error 4: Invalid Money Type");
			System.exit(0);
		}
		numOf = 0;
		this.blackMarketItem = blackMarketItem;
		this.effect = effect;
		priceIncrease = 1.2;
		if(modifiers.isEmpty()) {
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
		image = new JButton(new ImageIcon(imageLoc));
		image.setVisible(false);
	}
	public Upgrade(String name, String type, int moneyType, boolean blackMarketItem, String[] effect, StringTokenizer splitter) {
		super();
		this.name = name;
		this.type = type;
		this.price = Double.parseDouble(splitter.nextToken());
		if(moneyType < 2) {
			this.moneyType = moneyType;
		} else {
			System.out.println("Error 4: Invalid Money Type");
			System.exit(0);
		}
		numOf = Integer.parseInt(splitter.nextToken());
		this.blackMarketItem = blackMarketItem;
		this.effect = effect;
		image = new JButton(new ImageIcon(splitter.nextToken()));
		image.setVisible(false);
		priceIncrease = 1.2;
		if(modifiers.isEmpty()) {
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
	public Upgrade(String type) {
		this.type = type;
	}
	public static void setDefaultPrices() {
		defaultPrices.put("ProcessorUpgrade", 10);
		defaultPrices.put("GraphicsUpgrade", 20);
		defaultPrices.put("RAMUpgrade", 25);
		defaultPrices.put("BlackMarket", 5);
	}
	public void resetPrices() {
		this.price = defaultPrices.get(this.type);
		this.numOf = 0;
	}
	public void addOne(Player player) {
		++numOf;
		applyEffects(player);
		this.price *= priceIncrease;
	}
	public boolean makePurchase(Player player) {
		if(this.moneyType == BTC) {
			if(player.getBTC() > this.price) {
				player.setBTC(player.getBTC()-this.price);
				if(blackMarketItem) {
					if(Math.random() < 0.5) {
						return false;
					}
				}
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
	public boolean buy(Player player) {
		if(makePurchase(player)) {
			player.getDevice().addUpgrade(this);
			applyEffects(player);
			player.regulateValues();
			return true;
		}
		return false;
	}
	public void applyEffects(Player player) {
		if(effect[0].equalsIgnoreCase("chance")) {
			if(effect[1].equalsIgnoreCase("multiply")) {
				Player.chance *= 1+(Integer.parseInt(effect[2])/100.0);
			} else if(effect[1].equalsIgnoreCase("add")) {
				Player.chance += Integer.parseInt(effect[2]);
			} else {
				System.out.println("Error 9: Invalid Modifier");
				System.exit(0);
			}
		} else if(effect[0].equalsIgnoreCase("period")) {
			if(effect[1].equalsIgnoreCase("multiply")) {
				Player.period *= 1-(Double.parseDouble(effect[2])/100.0);
			} else if(effect[1].equalsIgnoreCase("add")) {
				Player.period -= Double.parseDouble(effect[2]);
			} else {
				System.out.println("Error 9: Invalid Modifier");
				System.exit(0);
			}
		} else if(effect[0].equalsIgnoreCase("value")) {
			if(effect[1].equalsIgnoreCase("multiply")) {
				Player.value *= 1+(Double.parseDouble(effect[2])/100.0);
			} else if(effect[1].equalsIgnoreCase("add")) {
				Player.value += Double.parseDouble(effect[2]);
			} else {
				System.out.println("Error 9: Invalid Modifier");
				System.exit(0);
			}
		} else {
			System.out.println("Error 8: Invalid attribute to modify");
			System.exit(0);
		}
	}
	public void loadUpgrade(Player player) {
		for(int i = 0; i < numOf; ++i) {
			this.price *= priceIncrease;
		}
	}
	@Override
	public boolean equals(Object o) {
		Upgrade i = (Upgrade) o;
		if(i.type.equalsIgnoreCase(this.type)) {
			return true;
		}
		return false;
	}
}

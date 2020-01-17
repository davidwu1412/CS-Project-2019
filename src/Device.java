import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Device {
	protected String name = "";
	protected ArrayList<Upgrade> availableUpgrades;
	protected LinkedList<Upgrade> currentUpgrades;
	protected JLabel image;
	protected JLabel image2;
	protected double priceWeighting;
	protected double price;
	private double baseChance;
	private double basePeriod;
	private double baseValue;
	private int tier;
	public Device(String name, String image, double price, int tier, double chance, double period, double value) {
		this.image = new JLabel(new ImageIcon(image));
		this.image2 = new JLabel(new ImageIcon(image));
		this.image.setBounds(50,150,300,300);
		this.image2.setBounds(600,150,300,300);
		this.name = name;
		availableUpgrades = new ArrayList<Upgrade>();
		currentUpgrades = new LinkedList<Upgrade>();
		priceWeighting = 1;
		this.price = price;
		this.tier = tier;
		baseChance = chance;
		basePeriod = period;
		baseValue = value;
	}
	public Device(String name) {
		this.name = name;
	}
	public Device(int tier) {
		this.tier = tier;
	}
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
	public void addUpgrade(Upgrade u) {
		currentUpgrades.add(u);
	}
	public void addAvailableUpgrade(Upgrade u) {
		availableUpgrades.add(u);
	}
	public boolean buy(Player player) {
		if(player.getUSD() >= this.price) {
			player.setDevice(this);
			player.setUSD(player.getUSD()-this.price);
			return true;
		}
		return false;
	}
	public int getTier() {
		return tier;
	}
}

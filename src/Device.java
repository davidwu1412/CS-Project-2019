import java.util.ArrayList;
import java.util.LinkedList;

@SuppressWarnings("unused")
public class Device {
	protected String name = "";
	protected ArrayList<Upgrade> availableUpgrades;
	protected LinkedList<Upgrade> currentUpgrades;
	protected Sprite sprite;
	protected String flavourText = "";
	public Device(String name, String flavourText, String image) {
		sprite = new Sprite(0,0,image);
		this.name = name;
		setUpgrades(this.name);
		availableUpgrades = new ArrayList<Upgrade>();
		currentUpgrades = new LinkedList<Upgrade>();
		this.flavourText = flavourText;
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
	public void setUpgrades(String name) {
		if(this.name.equalsIgnoreCase("Arduino Nano")) {
			
		}
	}
}

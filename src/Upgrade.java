
@SuppressWarnings("unused")
public abstract class Upgrade {
	public static final int BTC = 0;
	public static final int USD = 1;
	protected String name = "";
	protected String description = "";
	protected int price = 0;
	protected int moneyType = BTC;
	protected int numOf = 0;
	protected Sprite image;
	public Upgrade(String name, int price, int moneyType, String imageLoc) {
		this.name = name;
		this.price = price;
		this.moneyType = moneyType;
		numOf = 0;
		image = new Sprite(imageLoc);
	}
	public void setDescription(String newDescription) {
		description = newDescription;
	}
}

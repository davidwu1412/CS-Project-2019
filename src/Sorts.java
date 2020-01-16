import java.util.Comparator;

public class Sorts {

}

class deviceByName implements Comparator<Device> {
	public int compare(Device o1, Device o2) {
		return o2.name.compareToIgnoreCase(o1.name);
	}
}

class upgradeByType implements Comparator<Upgrade> {
	public int compare(Upgrade u1, Upgrade u2) {
		return u2.type.compareToIgnoreCase(u1.type);
	}
}

class deviceByTier implements Comparator<Device> {
	public int compare(Device d1, Device d2) {
		return d1.getTier()-d2.getTier();
	}
}
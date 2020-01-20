// Shawn Hu and David Wu
// Jan 17th, 2020
// This is the class for Sorts and related sorting methods methods. 

import java.util.Comparator;

public class Sorts {

}
// Sorting method that sorts device objects by name (obselete but still kept because i've used it before and for clarity)
class deviceByName implements Comparator<Device> {
	public int compare(Device o1, Device o2) {
		return o2.name.compareToIgnoreCase(o1.name);
	}
}
// Sorting method that sorts upgrade objects by the type
class upgradeByType implements Comparator<Upgrade> {
	public int compare(Upgrade u1, Upgrade u2) {
		return u2.type.compareToIgnoreCase(u1.type);
	}
}
// Sorting method that sorts the device objects by tier
class deviceByTier implements Comparator<Device> {
	public int compare(Device d1, Device d2) {
		return d1.getTier()-d2.getTier();
	}
}
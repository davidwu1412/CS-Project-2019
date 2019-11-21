
public class RNG {
	public RNG() {
	}
	public static boolean generateNumber(int chance) {
		double output = Math.random();
		if(output >= (double)chance/100.0) {
			return true;
		} else {
			return false;
		}
	}
}

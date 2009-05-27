package si.fri.spo.utils;

public class Utils {
	
	public static String razsiri(int stevilka, int size) {
		String ret = Integer.toHexString(stevilka);
		while(ret.length() < size) {
			ret = 0 + ret;
		}
		return ret;
	}

}

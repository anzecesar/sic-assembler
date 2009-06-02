package si.fri.spo.utils;

public class Utils {

	public static String razsiri(int stevilka, int size) {
		String ret = Integer.toHexString(stevilka);
		while (ret.length() < size) {
			ret = 0 + ret;
		}
		return ret;
	}

	// pretvarja operand v neko vrednost, ki spominja na integer :) zato da
	// lahko pozneje primerno povecamo lokSt
	public static int pretvoriOperand(String s) {
		if (s.charAt(0) == '#')
			return Integer.parseInt(s.substring(1));
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0)))
			return Integer.parseInt(s);
		if (s.startsWith("X'") && s.endsWith("'"))
			return Integer.parseInt(s.substring(2, s.length() - 1), 16);
		return 0;
	}

	public static int getStBajtov(String s) {
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0))) {
			s = Integer.toHexString(Integer.parseInt(s));
			int vred = s.length();
			return vred / 2 + (vred % 2);
		}
		if (s.startsWith("X'") && s.endsWith("'")) {
			int vred = s.length() - 3;
			return vred / 2 + (vred % 2);
		}
		return 0;
	}

	public static int getStBesed(String s) {
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0))) {
			s = Integer.toHexString(Integer.parseInt(s));
			int vred = s.length();
			return vred / 6 + (vred % 6);
		}
		if (s.startsWith("X'") && s.endsWith("'")) {
			int vred = s.length() - 3;
			return vred / 6 + (vred % 6);
		}
		return 0;
	}
}

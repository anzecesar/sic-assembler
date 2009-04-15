package si.fri.spo.utils;

import si.fri.spo.data.Vrstica;

public class Parser {
	private static final String REGEX = "\\s";
	
	/**
	 * Dobi neokrnjeno prebrano vrstico iz datoteke in jo obdela
	 * @param v
	 * @return
	 */
	public Vrstica parseLine(String v) {
		//najprej ocistimo vrstico komentarjev...
		int cIndex = v.indexOf(".");
		if(cIndex > 0)
			v = v.substring(0, cIndex);
		
		String[] stolpci = v.split(REGEX);
		
		return null;
	}
}

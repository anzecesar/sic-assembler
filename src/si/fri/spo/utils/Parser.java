package si.fri.spo.utils;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;

public class Parser {
	private static final String REGEX = "\\s";

	/**
	 * Dobi neokrnjeno prebrano vrstico iz datoteke in jo obdela
	 * 
	 * @param v
	 * @return
	 * @throws NapakaPriPrevajanju 
	 */
	public Vrstica parseLine(String v) throws NapakaPriPrevajanju {
		// najprej ocistimo vrstico komentarjev...
		int cIndex = v.indexOf(".");
		if (cIndex > 0)
			v = v.substring(0, cIndex);

		String[] stolpci = v.split(REGEX);
		
		Vrstica vrst = null;
		
		if (stolpci.length > 1 || stolpci[0].length() > 0) {
			int stevec = 0;
			MnetabManager mmgr = MnetabManager.getInstance();
			vrst = new Vrstica();
			
			for (String stolpec : stolpci) {
				if(stolpec.length() == 0)
					continue;
				if(!koncaneNavednice(stolpec)) {
					//operand tipa str'nek string'
					//zaradi presledka vmes sta se locila...
					stolpci[stevec] += " " + stolpci[stevec+1];
					stolpec = stolpci[stevec];
					
					stolpci[stevec+1] = "";
				}
				
				switch(stevec) {
					case 0:
						//prvi stolpec:
						//lahko je labela ali mnemonik
						if(mmgr.isMnemonik(stolpec)) {
							vrst.setMnemonik(stolpec);
						} else {
							vrst.setLabela(stolpec);
						}
						break;
					case 1:
						//drugi stolpec:
						//lahko je mnemonik ali operand
						if(vrst.hasMnemonik()) {
							//Preveri, ce je bil mnemonik najden v prvem stolpcu
							//Ce je bil, je v tem stolpcu ze operand...
							vrst.setOperand(stolpec);
						} else {
							//Vrstica je tipa LABELA MNEMONIK [OPERAND]
							vrst.setMnemonik(stolpec);
						}
						break;
					case 2:
						//Tu nam ne ostane nic drugega kot operand...
						vrst.setOperand(stolpec);
						break;
					default:
						//Ce je slucajno vec stolpcev kot 3: napaka
						throw new NapakaPriPrevajanju(null, "Neveljavno število elementov v vrstici!\n");
				}
				stevec++;
//				System.out.print(" [" + stolpec + "] ");
			}
//			System.out.println();
		}

		return vrst;
	}
	
	/**
	 * Vrace true, ce so navednice zakljucene (npr: 'lalala') ali ce jih sploh ni
	 * in false, ce navednice niso zakljucene (npr: 'lala)
	 * @param s
	 * @return
	 */
	private boolean koncaneNavednice(String s) {
		int i = s.indexOf("'");
		if(i < 0) 
			//Sploh navednic
			return true;
		if(s.lastIndexOf("'") == i)
			//prva in zadnja navednica sta ista navednica
			return false;
		else
			//niz vsebuje 2 navednici
			return true;
	}
}

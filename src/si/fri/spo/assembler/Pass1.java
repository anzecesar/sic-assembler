package si.fri.spo.assembler;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.SimtabManager;

public class Pass1 {
	private int zacetniNaslovOP, stariLokSt;
	private String imePrograma;
	
	public Vrstica pass1(Vrstica v) throws NapakaPriPrevajanju {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();

		if ("START".equals(v.getMnemonik())) {
			zacetniNaslovOP = Integer.parseInt(v.getOperand(), 16);
			stariLokSt = zacetniNaslovOP;
			v.setLokSt(zacetniNaslovOP);
			imePrograma = v.getLabela();
			return v;
		}
		
		int lokSt = stariLokSt;
		
		if (!"END".equals(v.getMnemonik())) {
			String labela = v.getLabela();
			String trenutniMnemonik = v.getMnemonik();
			
			if("EQU".equals(trenutniMnemonik)) {
				System.out.println("jebise");
				simTab.dodajEqu(labela, Integer.parseInt(v.getOperand()));
			} else if (labela != null) {
				simTab.dodajLabelo(labela, lokSt);
			}
			stariLokSt = lokSt;
			// TODO: preveri vrednosti :)
			if ("RESW".equals(trenutniMnemonik))
				lokSt = lokSt + 3 * (pretvoriOperand(v.getOperand()));
			else if ("RESB".equals(trenutniMnemonik))
				lokSt = lokSt + pretvoriOperand(v.getOperand());
			else if ("BYTE".equals(trenutniMnemonik))
				// lokst + št. bajtov v operandu
				lokSt = lokSt + getStBajtov(v.getOperand()); // *
																// (pretvoriOperand(v.getOperand()));
			else if ("WORD".equals(trenutniMnemonik))
				lokSt = lokSt + 3; // * (pretvoriOperand(v.getOperand()));
			else if (mneTab.isMnemonik(trenutniMnemonik) == true) {
				if (mneTab.getFormat(trenutniMnemonik) != -1) {
					lokSt = lokSt + mneTab.getFormat(trenutniMnemonik);
					if (v.isExtended()) {
						lokSt += 1;
					}
				}
			} else {
				// napaka
				throw new NapakaPriPrevajanju("Napaka: Prebran mnemonik "
						+ trenutniMnemonik + " ni veljaven mnemonik!");
			}
		} else {
			//stariLokSt = lokSt;
		}
		stariLokSt = lokSt;
		v.setLokSt(lokSt);

		System.out.println("Pass 1: " + v.getMnemonik() + " " + Integer.toHexString(v.getLokSt()));

		return v;

	}
	
	// pretvarja operand v neko vrednost, ki spominja na integer :) zato da
	// lahko pozneje primerno povecamo lokSt
	private int pretvoriOperand(String s) {
		if (s.charAt(0) == '#')
			return Integer.parseInt(s.substring(1));
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0)))
			return Integer.parseInt(s);
		if (s.startsWith("X'") && s.endsWith("'"))
			return Integer.parseInt(s.substring(3, s.length() - 1), 16);
		return 0;
	}
	
	private int getStBajtov(String s) {
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0))) {
			s = Integer.toHexString(Integer.parseInt(s));
			int vred = s.length() - 3;
			return vred / 2 + (vred % 2);
		}
		if (s.startsWith("X'") && s.endsWith("'")) {
			int vred = s.length() - 3;
			return vred / 2 + (vred % 2);
		}
		return 0;
	}
	
	public int getDolzina() {
		return stariLokSt - zacetniNaslovOP;
	}

	public int getZacetniNaslovOP() {
		return zacetniNaslovOP;
	}

	public String getImePrograma() {
		return imePrograma;
	}
}

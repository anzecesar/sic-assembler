package si.fri.spo.assembler;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.LittabManager;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.SimtabManager;
import si.fri.spo.utils.Utils;

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
				simTab.dodajEqu(labela, Integer.parseInt(v.getOperand()));
			} else if (labela != null) {
				simTab.dodajLabelo(labela, lokSt);
			} else if(v.isOperandJeLiteral())
				LittabManager.getInstance().dodajLiteral(labela, v.getOperand());
			stariLokSt = lokSt;
			// TODO: preveri vrednosti :)
			if ("RESW".equals(trenutniMnemonik))
				lokSt = lokSt + 3 * (Utils.pretvoriOperand(v.getOperand()));
			else if ("RESB".equals(trenutniMnemonik))
				lokSt = lokSt + Utils.pretvoriOperand(v.getOperand());
			else if ("BYTE".equals(trenutniMnemonik))
				// lokst + št. bajtov v operandu
				lokSt = lokSt + Utils.getStBajtov(v.getOperand()); // *
																// (pretvoriOperand(v.getOperand()));
			else if ("WORD".equals(trenutniMnemonik))
				lokSt = lokSt + 3; // * (pretvoriOperand(v.getOperand()));
			
			else if ("LTORG".equals(trenutniMnemonik)) {
				lokSt = LittabManager.getInstance().obdelajLtorg(lokSt);
				
			} else if (mneTab.isMnemonik(trenutniMnemonik) == true) {
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
			lokSt = LittabManager.getInstance().obdelajLtorg(lokSt);
		}
		stariLokSt = lokSt;
		v.setLokSt(lokSt);

		System.out.println("Pass 1: " + v.getMnemonik() + " " + Integer.toHexString(v.getLokSt()));

		return v;

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

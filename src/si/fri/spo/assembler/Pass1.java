package si.fri.spo.assembler;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.managers.LittabManager;
import si.fri.spo.managers.MnetabManager;
import si.fri.spo.managers.SimtabManager;
import si.fri.spo.utils.Utils;

public class Pass1 {
	private int zacetniNaslovOP = 0;
	private int stariLokSt;
	private String imePrograma;
	
	public Vrstica pass1(Vrstica v) throws NapakaPriPrevajanju {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();

		if ("START".equals(v.getMnemonik())) {
			//Ce najde start, nastavi zacetni naslov, sicer je zac. naslov 0...
			//Naslov v izvorni kodi je podan sesnajstisko.
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
				//Doda labelo v simtab z oznacbo, da gre za equ (ker je konstanta)
				simTab.dodajEqu(labela, Integer.parseInt(v.getOperand()));
			} else if (labela != null) {
				//ce ni EQU, vendar ima labelo jo doda v tabelo simTab.
				simTab.dodajLabelo(labela, lokSt);
			} else if(v.isOperandJeLiteral())
				//Ce je operand literal (to je ugotovil ze parser), ga dodamo v littab.
				LittabManager.getInstance().dodajLiteral(labela, v.getOperand());
			
			if ("RESW".equals(trenutniMnemonik))
				//rezervira (poveca lokSt) za st. wordov ki ga dobi kot operand
				lokSt = lokSt + 3 * (Utils.pretvoriOperand(v.getOperand()));
			else if ("RESB".equals(trenutniMnemonik))
				//rezervira (poveca lokSt) za st. bajtov, ki ga dobi kot operand
				lokSt = lokSt + Utils.pretvoriOperand(v.getOperand());
			else if ("BYTE".equals(trenutniMnemonik))
				// lokSt + št. bajtov v operandu
				lokSt = lokSt + Utils.getStBajtov(v.getOperand());
			else if ("WORD".equals(trenutniMnemonik))
				lokSt = lokSt + Utils.getStBesed(v.getOperand());
			
			else if ("LTORG".equals(trenutniMnemonik)) {
				//ozanci vse neoznacene literale do sedaj (v littab), da pripadajo temu ltorg ukazu
				//-za 2. prehod
				lokSt = LittabManager.getInstance().obdelajLtorg(lokSt);
			} else if (mneTab.isMnemonik(trenutniMnemonik) == true) {
				if (mneTab.getFormat(trenutniMnemonik) != -1) {
					//-1 format sva prevzela za psevdoukaze...
					lokSt = lokSt + mneTab.getFormat(trenutniMnemonik);
					if (v.isExtended()) {
						//4 format
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

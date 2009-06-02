package si.fri.spo.assembler;

import si.fri.spo.data.Mnemonic;
import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.managers.LittabManager;
import si.fri.spo.managers.MnetabManager;
import si.fri.spo.managers.ModTabManager;
import si.fri.spo.managers.SimtabManager;
import si.fri.spo.utils.Registers;
import si.fri.spo.utils.Utils;

public class Pass2 {
	int zacetniNaslov;
	
	int baseNaslov;
	boolean isBase = false;
	
	public Pass2(int zacetniNaslov) {
		this.zacetniNaslov = zacetniNaslov;
	}

	public Vrstica pass2(Vrstica v) throws NapakaPriPrevajanju {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();
		Registers regs = Registers.getInstance();
			
		String operand = v.getOperand();
		
		//Dobi pravilno zamaknjeno op. kodo ukaza (uposteva velikost)
		int ukaz = mneTab.getShiftedOpCode(v.getMnemonik());

		if ("BASE".equals(v.getMnemonik())) {
			// Če naletimo na direktivo BASE, omogoči bazno relativno
			// naslavljanje
			isBase = true;
			baseNaslov = simTab.getVrednostOperanda(operand);
			return v;
		}

		if ("NOBASE".equals(v.getMnemonik())) {
			// Onemogočimo bazno relativno naslavljanje.
			isBase = false;
			return v;
		}

		if ("BYTE".equals(v.getMnemonik())) {
			//Pise kar operand na trenutno (rezervirano) lokacijo.
			String bajt = pretvoriOperand(operand);
			v.setObjektnaKoda(bajt);
			return v;
		}

		if ("WORD".equals(v.getMnemonik())) {
			String word = pretvoriOperand(operand);
			v.setObjektnaKoda(word);
			return v;
		}
		
		if ("LTORG".equals(v.getMnemonik())) {
			//izpise vse literale, ki pripadajo temu LTORG na trenutno lokacijo.
			v.setObjektnaKoda(LittabManager.getInstance().flushLtorg());
			return v;
		}

		// Format 1 je ze kar OpCode.
		
		if (mneTab.getFormat(v.getMnemonik()) == 2) {
			//Format 2  - kot operande ima 1 ali 2 registra

			if (operand.contains(",")) {
				//Ima 2 registra
				String[] o = operand.split(",");
				ukaz += regs.getRegOpCode(o[0]) << 4;
				ukaz += regs.getRegOpCode(o[1]);
			} else {
				//Ima 1 register
				ukaz += regs.getRegOpCode(operand) << 4;
			}

		} else if (mneTab.getFormat(v.getMnemonik()) == 3) {
			if (!v.isExtended())
				//Format 3
				ukaz = obdelajFormat3(v, simTab, ukaz);
			else {
				//Format 4
				ukaz = obdelajFormat4(v, simTab, ukaz);
			}

		}
		
		if("END".equals(v.getMnemonik())) {
			//Pri end je potrebno vseeno izpisati vse preostale literale (ce jih je kaj ostalo).
			v.setObjektnaKoda(LittabManager.getInstance().flushLtorg());
			return v;
		}
		
		if(ukaz == 0xff)
			//0xff so psevdoukazi; pri nprimer BYTE se objektna koda v vrstico ze prej zapise
			return v;
		v.setObjektnaKoda(ukaz);
		return v;
	}

	private int obdelajFormat4(Vrstica v, SimtabManager simTab,
			int ukaz) throws NapakaPriPrevajanju {
		
		String operand = v.getOperand();
		
		//Format 4 je 1 bajt daljsi
		ukaz = ukaz << 8;
		
		//Nastavi bit E.
		ukaz |= Mnemonic.BIT_E_4;
		
		int naslov = 0;

		if (operand != null && operand.contains(",X")) {
			// indeksno naslavljanje
			
			ukaz |= Mnemonic.BIT_X_4;
			
			operand = operand.substring(0, operand.indexOf(","));
			
			//Lahko gre za vrednost ali simbol, spodnja metoda vrne vedno
			//neko vrednost, ali pa napako
			naslov = simTab.getVrednostOperanda(operand);
			
			int pc = v.getLokSt();
			naslov -= pc + 4; //V najinem primeru je to kazalec na naslednjega, zato ga zmanjsava
			//za 4
			
			ukaz += naslov;

		} else if (operand != null && operand.startsWith("#")) {
			//Direktno naslavljanje
			
			ukaz |= Mnemonic.BIT_I_4; // nastavi bit i na 1
			naslov = simTab.getVrednostOperanda(operand.substring(1));

			ukaz += naslov;
			//Bazno ali PC ni potrebno, ker lahko direktno naslovimo celoten pomnilnik
		} else if (operand != null && !operand.startsWith("#")) {
			//Neposredno naslavljanje
			
			ukaz |= Mnemonic.BIT_N_4;
			
			if (!v.isPosrednoNaslavljanje()) {
				ukaz |= Mnemonic.BIT_I_4;
			} 

			naslov = simTab.getVrednostOperanda(operand);
			
			if(zacetniNaslov == 0) {
				//relokacija
				String mod = "M" + Utils.razsiri(v.getNaslov() + 1, 6) + "05";
				ModTabManager.getInstance().addMod(mod);
			}

			ukaz += naslov;
		}
		return ukaz;
	}

	private int obdelajFormat3(Vrstica v, SimtabManager simTab,
			int ukaz) throws NapakaPriPrevajanju {
		
		String operand = v.getOperand();
		
		int naslov = 0;

		if (operand != null && operand.contains(",X")) {
			// Indexno naslavljanje
			
			ukaz |= Mnemonic.BIT_X_3;

			// Bita N in I sta lahko tudi 0.
			ukaz |= Mnemonic.BIT_N_3;
			ukaz |= Mnemonic.BIT_I_3;

			operand = operand.substring(0, operand.indexOf(","));

			naslov = simTab.getVrednostOperanda(operand);
			int pc = v.getLokSt();
			naslov = pc - naslov;

			ukaz = pcAliBazno(simTab, operand, ukaz, naslov);

		} else if (operand != null && !operand.startsWith("#")) {
			// Neposredno naslavljanje.

			int dn;
			int pc = v.getLokSt();
			
			if(v.isOperandJeLiteral()) {
				dn = LittabManager.getInstance().naslovLiterala(operand);
			} else {
				dn = simTab.getVrednostOperanda(operand);
			}

			int odmik = dn - pc;

			ukaz |= Mnemonic.BIT_N_3;

			if (!v.isPosrednoNaslavljanje()) {
				ukaz |= Mnemonic.BIT_I_3;
			}

			ukaz = pcAliBazno(simTab, operand, ukaz, odmik);

		} else if (operand != null && operand.startsWith("#")) { 
			// neposredno(takojsnje) naslavljanje
			
			ukaz |= Mnemonic.BIT_I_3; // nastavi bit i na 1

			operand = operand.substring(1);
			
			//Tu še ne vemo ali je bil simbol shranjen z EQU:
			naslov = simTab.getVrednostOperanda(operand);
			
			if (simTab.isLabela(operand) && !simTab.isEqu(operand)) {
				int pc = v.getLokSt();
				int odmik = naslov - pc;
				
				ukaz = pcAliBazno(simTab, operand, ukaz, odmik);
			} else
				ukaz += naslov;
		}
		return ukaz;
	}

	private int pcAliBazno(SimtabManager simTab, String operand, int ukaz,
			int odmik) throws NapakaPriPrevajanju {
		int dn;

		if (odmik >= -2048 && odmik <= 2047) {
			// PC-relativno
			ukaz |= Mnemonic.BIT_P_3;

			ukaz += skrajsajInt(odmik);

		} else {
			if (!isBase) {
				// Napaka
				throw new NapakaPriPrevajanju(
						"Neveljaven odmik, bazno naslavljanje ni omogočeno.\n");
			}

			dn = simTab.getVrednostOperanda(operand);

			odmik = dn - baseNaslov;

			ukaz |= Mnemonic.BIT_B_3;

			ukaz += skrajsajInt(odmik);

			if (!(odmik >= 0 && odmik <= 4095)) {
				throw new NapakaPriPrevajanju(
						"Neveljaven odmik, bazno naslavljanje omogočeno.\n");
			}
		}
		return ukaz;
	}

	private int skrajsajInt(int i) {
		//Skrajsa binarni zapis na primerno dolzino bitov
		if (i >= 0)
			return i;

		String s = Integer.toBinaryString(i);
		s = s.substring(s.length() - 12);
		return Integer.parseInt(s, 2);
	}

	private String pretvoriOperand(String s) {
		//Vrne hex vrednost operanda
		String ret = "";
		if (s.charAt(0) == 'C') {
			for (int i = 2; i < s.length() -1; ++i) {
				char c = s.charAt(i);
				ret += Integer.toHexString((int) c);
			}
			return ret;
		}
		if (Character.isDigit(s.charAt(0))) {
			return Integer.toHexString(Integer.parseInt(s));
		}
		if (s.startsWith("X'") && s.endsWith("'")) {
			s = s.replace("'", "");
			s = s.replace("X", "");
			return s.toLowerCase();
		}
		return ret;
	}

}

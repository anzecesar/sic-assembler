package si.fri.spo.assembler;

import si.fri.spo.data.Mnemonic;
import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.ModTabManager;
import si.fri.spo.utils.Registers;
import si.fri.spo.utils.SimtabManager;
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
		int ukaz = mneTab.getShiftedOpCode(v.getMnemonik());

		// System.out.println("Shifted OpCode: " +
		// Integer.toHexString(mneTab.getShiftedOpCode(v.getMnemonik())));

		if ("BASE".equals(v.getMnemonik())) {
			// Če naletimo na direktivo BASE, omogoči bazno relativno
			// naslavljanje
			isBase = true;
			baseNaslov = simTab.getVrednostOperanda(operand);
			// System.out.println("SET BASE: " + baseNaslov + " operand " +
			// operand);
			return v;
		}

		if ("NOBASE".equals(v.getMnemonik())) {
			// Onemogočimo bazno relativno naslavljanje.
			isBase = false;
			return v;
		}

		if ("BYTE".equals(v.getMnemonik())) {
			String bajt = pretvoriOperand(operand);
			//System.out.println("BYTE: " + bajt);
			v.setObjektnaKoda(bajt);
			return v;
			//todo: string in neki
		}

		if ("WORD".equals(v.getMnemonik())) {
			
		}

		// Format 1 je ze kar OpCode.
		if (mneTab.getFormat(v.getMnemonik()) == 2) {
			//System.out.print("A-Fromat 2: " + Integer.toHexString(ukaz) + " "
			//		+ v.getMnemonik() + " ");

			//int op;

			if (operand.contains(",")) {
				String[] o = operand.split(",");
				ukaz += regs.getRegOpCode(o[0]) << 4;
				ukaz += regs.getRegOpCode(o[1]);
			} else {
				ukaz += regs.getRegOpCode(operand) << 4;
			}

		} else if (mneTab.getFormat(v.getMnemonik()) == 3) {
			if (!v.isExtended())
				ukaz = obdelajFormat3(v, simTab, ukaz);
			else {
				ukaz = obdelajFormat4(v, simTab, ukaz);
			}

		}
		//System.out.println(Integer.toHexString(ukaz));
		if(ukaz == 0)
			return v;
		v.setObjektnaKoda(ukaz);
		return v;
	}

	private int obdelajFormat4(Vrstica v, SimtabManager simTab,
			int ukaz) throws NapakaPriPrevajanju {
		
		String operand = v.getOperand();
		ukaz = ukaz << 8;
		ukaz |= Mnemonic.BIT_E_4;
		int naslov = 0;

		if (operand != null && operand.contains(",X")) {
			// indeksno
			ukaz |= Mnemonic.BIT_X_4;
			operand = operand.substring(0, operand.indexOf(","));

			naslov = simTab.getVrednostOperanda(operand);
			int pc = v.getLokSt();
			naslov -= pc + 4;
			
			ukaz += naslov;

		} else if (operand != null && operand.startsWith("#")) {
			//Direktno naslavljanje
			
			ukaz |= Mnemonic.BIT_I_4; // nastavi bit i na 1
			naslov = simTab.getVrednostOperanda(operand.substring(1));

			ukaz += naslov;
		} else if (operand != null && !operand.startsWith("#")) {
			//Neposredno naslavljanje
			
			ukaz |= Mnemonic.BIT_I_4; // nastavi bit i na 1
			
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
		//System.out.print("A-Fromat 3: " + Integer.toHexString(ukaz) + " "
		//		+ v.getMnemonik() + " ");
		
		String operand = v.getOperand();
		
		int naslov = 0;

		if (operand != null && operand.contains(",X")) {
			//System.out.print("Indexno. ");
			// Indexno naslavljanje
			ukaz |= Mnemonic.BIT_X_3;

			// Bita N in I sta lahko tudi 0.
			ukaz |= Mnemonic.BIT_N_3;
			ukaz |= Mnemonic.BIT_I_3;

			operand = operand.substring(0, operand.indexOf(","));

			naslov = simTab.getVrednostOperanda(operand);
			int pc = v.getLokSt();
			naslov = pc - naslov;

			// System.out.print("naslov: " + naslov + " " + pc + " " +
			// Integer.toHexString(ukaz) + " ");

			// ukaz += naslov;

			ukaz = pcAliBazno(simTab, operand, ukaz, naslov);

		} else if (operand != null && !operand.startsWith("#")) {
			// System.out.print("Pc/Bazno ");
			// Najprej poskuša z Pc-relativnim, če je odmik, če je odmik
			// izven meja, pa z baznim.

			int dn = simTab.getVrednostOperanda(operand);
			int pc = v.getLokSt();

			// System.out.println(dn + " - " + pc);

			int odmik = dn - pc;

			// Bita N in I sta lahko tudi 0.
			ukaz |= Mnemonic.BIT_N_3;

			if (!v.isPosrednoNaslavljanje()) {
				ukaz |= Mnemonic.BIT_I_3;
			}

			// ukaz += skrajsajInt(odmik);

			ukaz = pcAliBazno(simTab, operand, ukaz, odmik);

			// System.out.println("Odmik: " + odmik);

		} else if (operand != null && operand.startsWith("#")) { // neposredno(takojsnje)
			// naslavljanje
			//System.out.print("Direktno ");
			ukaz |= Mnemonic.BIT_I_3; // nastavi bit i na 1

			operand = operand.substring(1);
			
			naslov = simTab.getVrednostOperanda(operand);
			
			if (simTab.isLabela(operand) && !simTab.isEqu(operand)) {
				//int dn = simTab.getVrednostOperanda(operand.substring(1));
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

		// System.out.print(" odmik: " + odmik + " " +
		// Integer.toHexString(odmik) + " ");

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
		// System.out.println("odmik " + odmik);
		return ukaz;
	}

	private int skrajsajInt(int i) {
		if (i >= 0)
			return i;

		String s = Integer.toBinaryString(i);
		s = s.substring(s.length() - 12);
		return Integer.parseInt(s, 2);
	}

	private String pretvoriOperand(String s) {
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

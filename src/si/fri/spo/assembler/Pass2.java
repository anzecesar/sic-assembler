package si.fri.spo.assembler;

import si.fri.spo.data.Mnemonic;
import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.Registers;
import si.fri.spo.utils.SimtabManager;

public class Pass2 {
	int baseNaslov;
	boolean isBase = false;

	public void pass2(Vrstica v) throws NapakaPriPrevajanju {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();
		Registers regs = Registers.getInstance();

		String operand = v.getOperand();
		int ukaz = mneTab.getShiftedOpCode(v.getMnemonik());

		// System.out.println("Shifted OpCode: " +
		// Integer.toHexString(mneTab.getShiftedOpCode(v.getMnemonik())));
		
		if("BASE".equals(v.getMnemonik())) {
			//Če naletimo na direktivo BASE, omogoči bazno relativno naslavljanje
			isBase = true;
			baseNaslov = simTab.getVrednostOperanda(operand);
			return;
		}
		
		if("NOBASE".equals(v.getMnemonik())) {
			//Onemogočimo bazno relativno naslavljanje.
			isBase = false;
		}
		
		// Format 1 je ze kar OpCode.
		if (mneTab.getFormat(v.getMnemonik()) == 2) {
			System.out.print("A-Fromat 2: " + Integer.toHexString(ukaz) + " "
					+ v.getMnemonik() + " ");

			int op;

			if (operand.contains(",")) {
				String[] o = operand.split(",");
				ukaz += regs.getRegOpCode(o[1]) << 12;
				ukaz += regs.getRegOpCode(o[2]) << 8;
			} else {
				ukaz += regs.getRegOpCode(operand) << 12;
			}

		} else if (mneTab.getFormat(v.getMnemonik()) == 3) {
			if(!v.isExtended())
				ukaz = obdelajFormat3in4(v, simTab, operand, ukaz);
			else 
				System.out.println("Format 4 se ni implementiran :]");

		} 
		System.out.println(Integer.toHexString(ukaz));
	}

	private int obdelajFormat3in4(Vrstica v, SimtabManager simTab, String operand,
			int ukaz) throws NapakaPriPrevajanju {
		System.out.print("A-Fromat 3: " + Integer.toHexString(ukaz) + " "
				+ v.getMnemonik() + " ");
		
		int naslov = 0;
		
		if (operand != null && operand.contains(",X")) {
			System.out.print("Indexno. ");
			// Indexno naslavljanje
			ukaz |= Mnemonic.BIT_X_3;
			
			//Bita N in I sta lahko tudi 0.
			ukaz |= Mnemonic.BIT_N_3;
			ukaz |= Mnemonic.BIT_I_3;
			
			naslov = simTab.getVrednostOperanda(operand.substring(0, operand.indexOf(",")));
			int pc = v.getLokSt();
			naslov -= pc + 3;
			
			System.out.print("naslov: " + Integer.toHexString(naslov) + " ");
			
			ukaz += naslov;
			
			ukaz = pcAliBazno(simTab, operand, ukaz, naslov);
			
		} else if(operand != null && !operand.startsWith("#")) {
			System.out.print("Pc/Bazno ");
			//Najprej poskuša z Pc-relativnim, če je odmik, če je odmik
			//izven meja, pa z baznim.
			
			int dn = simTab.getVrednostOperanda(operand);
			int pc = v.getLokSt();
			
			//System.out.println(dn + " - " + pc);
			
			int odmik = dn - pc;
			
			//Bita N in I sta lahko tudi 0.
			ukaz |= Mnemonic.BIT_N_3;
			ukaz |= Mnemonic.BIT_I_3;
			
			ukaz += skrajsajInt(odmik);
			
			ukaz = pcAliBazno(simTab, operand, ukaz, odmik);
			
			//System.out.println("Odmik: " + odmik);
			
		} else if (operand != null
				&& operand.startsWith("#")) { // neposredno(takojsnje)
			// naslavljanje
			System.out.print("Direktno ");
			ukaz |= Mnemonic.BIT_I_3; // nastavi bit i na 1
			
			naslov = simTab.getVrednostOperanda(operand.substring(1));
			
			ukaz += naslov;
		}
		return ukaz;
	}

	private int pcAliBazno(SimtabManager simTab, String operand, int ukaz,
			int odmik) throws NapakaPriPrevajanju {
		int dn;
		
		//System.out.println(odmik + " " + Integer.toHexString(odmik));
		
		if (odmik >= -2048 && odmik <= 2047) {
			//PC-relativno
			ukaz |= Mnemonic.BIT_P_3;
			
			//System.out.println("Binary: " + Integer.toBinaryString(ukaz) + ", odmik: " + odmik + " - " + Integer.toBinaryString(odmik));
			//System.out.println("Skrajsan odmik: " + skrajsajInt(odmik) + " " + Integer.toBinaryString(skrajsajInt(odmik)));
		} else {
			if(!isBase) {
				//Napaka
				throw new NapakaPriPrevajanju("Neveljaven odmik, bazno naslavljanje ni omogočeno.\n");
			}
			dn = simTab.getVrednostOperanda(operand);
			
			odmik = dn - baseNaslov;
			
			ukaz |= Mnemonic.BIT_B_3;
			
			ukaz += skrajsajInt(odmik);
			
			if(!(odmik >= 0 && odmik <= 4095)) {
				throw new NapakaPriPrevajanju("Neveljaven odmik, bazno naslavljanje omogočeno.\n");
			}
		}
		//System.out.println("odmik " + odmik);
		return ukaz;
	}
	
	private int skrajsajInt(int i) {
		if(i > 0)
			return i;
		
		String s = Integer.toBinaryString(i);
		s = s.substring(s.length()-12);
		return Integer.parseInt(s, 2);
	}

}

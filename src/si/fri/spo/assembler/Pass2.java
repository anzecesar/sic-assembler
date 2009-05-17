package si.fri.spo.assembler;

import si.fri.spo.data.Mnemonic;
import si.fri.spo.data.Vrstica;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.Registers;
import si.fri.spo.utils.SimtabManager;

public class Pass2 {

	public void pass2(Vrstica v) {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();
		Registers regs = Registers.getInstance();

		String operand = v.getOperand();
		int ukaz = mneTab.getShiftedOpCode(v.getMnemonik());

		// System.out.println("Shifted OpCode: " +
		// Integer.toHexString(mneTab.getShiftedOpCode(v.getMnemonik())));

		// Format 1 je ze kar OpCode.

		if (mneTab.getFormat(v.getMnemonik()) == 2) {
			System.out.println("2: " + Integer.toHexString(ukaz) + " "
					+ v.getMnemonik());

			int op;

			if (operand.contains(",")) {
				String[] o = operand.split(",");
				ukaz += regs.getRegOpCode(o[1]) << 8;
				ukaz += regs.getRegOpCode(o[2]);
			} else {
				ukaz += regs.getRegOpCode(operand) << 8;
			}

		} else if (mneTab.getFormat(v.getMnemonik()) == 3) {
			System.out.println("3: " + Integer.toHexString(ukaz) + " "
					+ v.getMnemonik());
			
			int naslov = 0;	
			
			if (operand != null && v.getOperand().contains(",X")) {
				// Indexno naslavljanje
				ukaz |= Mnemonic.BIT_X_3;
			}

			if (operand != null
					&& operand.startsWith("#")) { // neposredno(takojsnje)
				// naslavljanje
				ukaz |= Mnemonic.BIT_I_3; // nastavi bit i na 1
				
				if (simTab.isLabela(operand.substring(1))) {
					// ce obstaja v tabeli simbol, ki je definiran po znaku
					// za takojsnje naslavljanje
					naslov = simTab.getLokSt(operand.substring(1));
				} else {
					try {
						ukaz = ukaz
								+ Integer.parseInt(v.getOperand().substring(1));

					} catch (NumberFormatException e) {
						// postavi bit napake, oh joy. Labela ne obstaja.
					}
				}

			}

		}
		System.out.println(Integer.toHexString(ukaz));
	}

}

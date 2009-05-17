package si.fri.spo.assembler;

import si.fri.spo.data.Vrstica;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.Registers;
import si.fri.spo.utils.SimtabManager;

public class Pass2 {
	
	public void pass2(Vrstica v) {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();
		Registers regs = Registers.getInstance();
		String[] operand;
		boolean vecParametrov = false;
		int ukaz = mneTab.getShiftedOpCode(v.getMnemonik());
		
		//System.out.println("Shifted OpCode: " + Integer.toHexString(mneTab.getShiftedOpCode(v.getMnemonik())));
		
		if (v.getOperand() != null) {
			if (v.getOperand().contains(","))
				vecParametrov = true;
			else
				vecParametrov = false;
			operand = v.getOperand().split(",");

		} else {
			operand = new String[2];
			operand[0] = null;
			operand[1] = null;
		}
		
		//Format 1 je ze kar OpCode.
		
		if (mneTab.getFormat(v.getMnemonik()) == 2) {
			System.out.println("2: " + Integer.toHexString(ukaz) + " " + v.getMnemonik());
			int op = regs.getRegOpCode(operand[0]);
			op = op << 8;
			ukaz += op;
			if (vecParametrov)
				ukaz += regs.getRegOpCode(operand[1]);
			else
				ukaz += 0x0;
		} else if (mneTab.getFormat(v.getMnemonik()) == 3) {
			System.out.println("3: " + Integer.toHexString(ukaz));
			try {
				if (v.getOperand().substring(0, 1).equals("#")) { // neposredno(takojsnje)
																	// naslavljanje
					ukaz = mneTab.getOpCode(v.getMnemonik());
					ukaz = ukaz +  1;   // da lahko mirno dodamo bita n in i, in
										// sicer na 0 in 1, torej pristejemo v
										// bistvu samo 1
					ukaz = ukaz << 16; // preskocimo ostale bite [x,b,p,e] 4b in
										// preostali odmik 12
					
					if (simTab.isLabela(v.getOperand().substring(1))) {
						// ce obstaja v tabeli simbol, ki je definiran po znaku
						// za takojsnje naslavljanje
						// vzamemo lokSt in ga damo kot operand, drugace je ali
						// nedefiniran simbol oz. stevilo
					} else {
						try {
							ukaz = ukaz
									+ Integer.parseInt(v.getOperand()
											.substring(1));

						} catch (NumberFormatException e) {
							// postavi bit napake, oh joy. Labela ne obstaja.
						}
					}

				}

			} catch (NullPointerException e) {
				ukaz = mneTab.getOpCode(v.getMnemonik());

				// format 3 brez operanda
			}
		}
		System.out.println(Integer.toHexString(ukaz));
	}
	
}

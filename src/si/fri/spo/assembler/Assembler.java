package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.Parser;
import si.fri.spo.utils.SimtabManager;
import si.fri.spo.utils.VmesnaDatoteka;
import si.fri.spo.utils.Registers;

public class Assembler {
	private int zacetniNaslovOP, stariLokSt;

	public Assembler() {
		this(true);
	}

	public Assembler(boolean im) {
		try {
			VmesnaDatoteka.init(im);
		} catch (IOException e) {
			System.err
					.println("Napaka: Vmesne datoteke ni bilo mogoče odpreti!");
			// e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void assemble(String source, String destination)
			throws NapakaPriPrevajanju {
		doAssemble(source);

	}

	public void assemble(String source) throws NapakaPriPrevajanju {
		assemble(source, "saf.r");
	}

	// pretvarja operand v neko vrednost, ki spominja na integer :) zato da
	// lahko pozneje primerno povecamo lokSt
	private int pretvoriOperand(String s) {
		int operand = 0;
		if (s.charAt(0) == '#')
			operand = Integer.parseInt(s.substring(1));
		if (s.charAt(0) == 'C')
			operand = s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0)))
			operand = Integer.parseInt(s, 16);
		return operand;
	}

	private void doAssemble(String source) throws NapakaPriPrevajanju {
		String vrstica = "";
		Parser p = new Parser();

		int stVrstice = 1;
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			Vrstica v;
			int lokSt = 0;

			VmesnaDatoteka vmes = VmesnaDatoteka.getInstance();

			while ((vrstica = input.readLine()) != null) {
				v = p.parseLine(vrstica);

				// Izognimo se praznim vrsticam.
				if (v == null)
					continue;

				// System.out.println(stVrstice);
				// dat.dodajVrstico(v);

				v = pass1(v, lokSt);
				lokSt = v.getLokSt();

				vmes.pisi(v);

				System.out.println(" " + Integer.toHexString(stariLokSt));

				stVrstice++;
			}
			
			while((v = vmes.beri()) != null) {
				System.out.println(v.toString());
			}
		} catch (FileNotFoundException e) {
			// Ce nam ne uspe odpreti datoteke...
			e.printStackTrace();
		} catch (IOException e) {
			// Ce pride do napake pri branju...
			e.printStackTrace();
		} catch (NapakaPriPrevajanju e) {
			e.setMessage(e.getMessage() + "Št. vrstice: " + stVrstice
					+ " (Vrstica: " + vrstica + ")\n");
			throw (e);
		}

	}

	private Vrstica pass1(Vrstica v, int lokSt) throws NapakaPriPrevajanju {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();

		if ("START".equals(v.getMnemonik())) {
			zacetniNaslovOP = pretvoriOperand(v.getOperand());
			v.setLokSt(zacetniNaslovOP);
			return v;
		}

		if (!"END".equals(v.getMnemonik())) {
			String labela = v.getLabela();
			if (labela != null) {
				simTab.dodajLabelo(labela, lokSt);
			}
			stariLokSt = lokSt;
			String trenutniMnemonik = v.getMnemonik();
			// TODO: preveri vrednosti :)
			if ("RESW".equals(trenutniMnemonik))
				lokSt = lokSt + 3 * (pretvoriOperand(v.getOperand()));
			else if ("RESB".equals(trenutniMnemonik))
				lokSt = lokSt + 4 * (pretvoriOperand(v.getOperand()));
			else if ("BYTE".equals(trenutniMnemonik))
				lokSt = lokSt + 4 * (pretvoriOperand(v.getOperand()));
			else if ("WORD".equals(trenutniMnemonik))
				lokSt = lokSt + 3 * (pretvoriOperand(v.getOperand()));
			else if (mneTab.isMnemonik(trenutniMnemonik) == true) {
				mneTab.getFormat(trenutniMnemonik);
				lokSt = lokSt + mneTab.getFormat(trenutniMnemonik);
			} else {
				// napaka
				throw new NapakaPriPrevajanju("Napaka: Prebran mnemonik "
						+ trenutniMnemonik + " ni veljaven mnemonik!");
			}
		} else {
			stariLokSt = lokSt;
		}

		v.setLokSt(lokSt);

		return v;

	}

	private void pass2(Vrstica v) {
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();
		Registers regs = Registers.getInstance();
		String[] operand;
		boolean vecParametrov = false;
		int ukaz = mneTab.getOpCode(v.getMnemonik());

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

		if (mneTab.getFormat(v.getMnemonik()) == 1) {
			ukaz = mneTab.getOpCode(v.getMnemonik());
			ukaz = ukaz << 16;
		} else if (mneTab.getFormat(v.getMnemonik()) == 2) {
			ukaz = mneTab.getOpCode(v.getMnemonik());
			ukaz = ukaz << 4;
			ukaz += regs.getRegOpCode(operand[0]);
			ukaz = ukaz << 4;
			if (vecParametrov)
				ukaz += regs.getRegOpCode(operand[1]);
			else
				ukaz += 0x0;
			ukaz = ukaz << 8;
		} else if (mneTab.getFormat(v.getMnemonik()) == 3) {
			try {
				if (v.getOperand().substring(0, 1).equals("#")) { // neposredno(takojsnje)
																	// naslavljanje
					ukaz = mneTab.getOpCode(v.getMnemonik());
					ukaz = ukaz + 1; // da lahko mirno dodamo bita n in i, in
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

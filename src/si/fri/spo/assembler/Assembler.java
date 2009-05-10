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

public class Assembler {
	private int zacetniNaslovOP;

	private boolean inMemory;

	public Assembler() {
		this(true);
	}

	public Assembler(boolean im) {
		inMemory = im;
	}

	public void assemble(String source, String destination)
			throws NapakaPriPrevajanju {
		doAssemble(source);

	}

	public void assemble(String source) throws NapakaPriPrevajanju {
		assemble(source, "saf.r");
	}

	private void pisivVmesnoDat() {
		// TODO: write me
	}

	// pretvarja operand v neko vrednost, ki spominja na integer :) zato da
	// lahko pozneje primerno povecamo lokSt
	private int pretvoriOperand(String s) {
		int operand = 0;
		if (s.charAt(0) == '#')
			operand = Integer.parseInt(s.substring(1, s.length() - 1));
		return operand;
	}

	private void doAssemble(String source) throws NapakaPriPrevajanju {
		String vrstica = "";
		PrebranaDatoteka dat = new PrebranaDatoteka();
		Parser p = new Parser();

		int stVrstice = 1;
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			Vrstica v;
			int lokSt = 0;
			while ((vrstica = input.readLine()) != null) {
				v = p.parseLine(vrstica);

				// Izognimo se praznim vrsticam.
				if (v == null)
					continue;

				//System.out.println(stVrstice);
				dat.dodajVrstico(v);
				lokSt = pass1(v, lokSt);

				if (!inMemory) {
					// TODO: pisi v vmesno datoteko.
				}
				stVrstice++;
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

		// debug:
		System.out.println("Konca prebrana datoteka: ");
		for (Vrstica v : dat.getVrstice()) {
			System.out.println(v.toString());
		}

	}

	private int pass1(Vrstica v, int lokSt) throws NapakaPriPrevajanju {
		int stariLokSt = 0;
		SimtabManager simTab = SimtabManager.getInstance();
		MnetabManager mneTab = MnetabManager.getInstance();

		if ("START".equals(v.getMnemonik())) {
			zacetniNaslovOP = Integer.parseInt(v.getOperand());
			return zacetniNaslovOP;
		}

		if (!"END".equals(v.getMnemonik())) {
			String labela = v.getLabela();
			if (labela != null) {
				simTab.dodajLabelo(labela, lokSt);
				stariLokSt = lokSt;
				String trenutniMnemonik = v.getMnemonik();

				if ("RESW".equals(trenutniMnemonik))
					lokSt = lokSt + 3 * (pretvoriOperand(v.getOperand()));
				else if ("RESB".equals(trenutniMnemonik))
					lokSt = lokSt + 4 * (pretvoriOperand(v.getOperand()));
				else if ("BYTE".equals(trenutniMnemonik)) {
					//do something
				}
				else if (mneTab.isMnemonik(trenutniMnemonik) == true) {
					mneTab.getFormat(trenutniMnemonik);
					lokSt = lokSt + mneTab.getFormat(trenutniMnemonik);
				} else {
					// napaka
					throw new NapakaPriPrevajanju("Napaka: Prebran mnemonik "
							+ trenutniMnemonik + " ni veljaven mnemonik!");
				}
			}
		}

		return lokSt;

	}

	private void pass2() {
		// TODO: write me
	}
}

package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import si.fri.spo.data.Mnemonic;
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
		if (s.charAt(0) == '#')
			return Integer.parseInt(s.substring(1));
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0)))
			return Integer.parseInt(s);
		if(s.startsWith("X'") && s.endsWith("'"))
			return Integer.parseInt(s.substring(3,s.length()-1), 16);
		return 0;
	}
	
	private int getStBajtov(String s) {
		if (s.charAt(0) == 'C')
			return s.length() - 3; // prvi znak in ''
		if (Character.isDigit(s.charAt(0))) {
			s = Integer.toHexString(Integer.parseInt(s));
			int vred =  s.length() - 3;
			return vred/2 + (vred % 2);
		} if(s.startsWith("X'") && s.endsWith("'")) {
			int vred =  s.length() - 3;
			return vred/2 + (vred % 2);
		}
		return 0;
	}

	private void doAssemble(String source) throws NapakaPriPrevajanju {
		String vrstica = "";
		Parser p = new Parser();

		int stVrstice = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			Vrstica v;
			int lokSt = 0;

			VmesnaDatoteka vmes = VmesnaDatoteka.getInstance();
			
			Pass2 p2 = new Pass2();

			while ((vrstica = input.readLine()) != null) {
				stVrstice++;
				v = p.parseLine(vrstica);

				// Izognimo se praznim vrsticam.
				if (v == null) {
					v = new Vrstica();
					v.setVeljavna(false);
					vmes.pisi(v);
					continue;
				}

				// System.out.println(stVrstice);
				// dat.dodajVrstico(v);

				v = pass1(v, lokSt);
				lokSt = v.getLokSt();

				vmes.pisi(v);

				//System.out.println(" " + Integer.toHexString(stariLokSt));

			}
			stVrstice = 0;
			while((v = vmes.beri()) != null) {
				//System.out.println(v.toString());
				stVrstice++;
				if(v.isVeljavna())
					p2.pass2(v);
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
			zacetniNaslovOP = Integer.parseInt(v.getOperand(), 16);
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
				lokSt = lokSt + pretvoriOperand(v.getOperand());
			else if ("BYTE".equals(trenutniMnemonik))
				//lokst + št. bajtov v operandu
				lokSt = lokSt + getStBajtov(v.getOperand()); //* (pretvoriOperand(v.getOperand()));
			else if ("WORD".equals(trenutniMnemonik))
				lokSt = lokSt + 3; //* (pretvoriOperand(v.getOperand()));
			else if (mneTab.isMnemonik(trenutniMnemonik) == true) {
				mneTab.getFormat(trenutniMnemonik);
				if(mneTab.getFormat(trenutniMnemonik) == -1) 
					return v;	
				lokSt = lokSt + mneTab.getFormat(trenutniMnemonik);
				if(v.isExtended()) {
					lokSt += 3;
				}
			} else {
				// napaka
				throw new NapakaPriPrevajanju("Napaka: Prebran mnemonik "
						+ trenutniMnemonik + " ni veljaven mnemonik!");
			}
		} else {
			stariLokSt = lokSt;
		}

		v.setLokSt(lokSt);
		
		System.out.println("Pass 1: " + Integer.toHexString(v.getLokSt()));

		return v;

	}
}

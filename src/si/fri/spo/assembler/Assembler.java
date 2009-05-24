package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.Parser;
import si.fri.spo.utils.VmesnaDatoteka;

public class Assembler {
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

	private void doAssemble(String source) throws NapakaPriPrevajanju {
		String vrstica = "";
		Parser p = new Parser();

		int stVrstice = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			Vrstica v;
			//int lokSt = 0;

			VmesnaDatoteka vmes = VmesnaDatoteka.getInstance();

			Pass2 p2 = new Pass2();
			Pass1 p1 = new Pass1();

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

				v = p1.pass1(v);
				//lokSt = v.getLokSt();

				vmes.pisi(v);

				// System.out.println(" " + Integer.toHexString(stariLokSt));

			}
			//System.out.println("Pass 2");
			stVrstice = 0;
			while ((v = vmes.beri()) != null) {
				// System.out.println(v.toString());
				stVrstice++;
				if (v.isVeljavna()) {
					
					String ukaz = p2.pass2(v);
					
					if(ukaz.length() > 0)
						System.out.println(ukaz);
				}
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
}

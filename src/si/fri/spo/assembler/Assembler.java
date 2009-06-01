package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.io.ObjektnaDatoteka;
import si.fri.spo.io.VmesnaDatoteka;
import si.fri.spo.managers.LittabManager;
import si.fri.spo.managers.ModTabManager;
import si.fri.spo.managers.SimtabManager;
import si.fri.spo.utils.Parser;

public class Assembler {
	private String obj;
	
	public Assembler() {
		this(true);
	}

	public Assembler(boolean im) {
		// konstruktor, nic ekstravangantnega...
		try {
			VmesnaDatoteka.init(im);
			ObjektnaDatoteka.init(im);
		} catch (IOException e) {
			System.err
					.println("Napaka: Vmesne datoteke ni bilo mogoče odpreti!");
			// e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void assemble(String source, String destination)
			throws NapakaPriPrevajanju {
		// Sprememba koncne datoteke se ni omogocena :]
		assemble(source);

	}

	public void assemble(String source) throws NapakaPriPrevajanju {
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			assemble(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void assemble(Reader source) throws NapakaPriPrevajanju {
		BufferedReader input = new BufferedReader(source);
		assemble(input);

	}

	public void assemble(BufferedReader input) throws NapakaPriPrevajanju {
		String vrstica = "";
		Parser p = new Parser();

		int stVrstice = 0;
		try {
			Vrstica v;
			// int lokSt = 0;

			VmesnaDatoteka vmes = VmesnaDatoteka.getInstance();

			Pass1 p1 = new Pass1();

			while ((vrstica = input.readLine()) != null) {
				// Beri iz datoteke, vrstico za vrstico
				stVrstice++;

				// obdelaj vsako vrstico posebej
				v = p.parseLine(vrstica);

				// Izognimo se praznim vrsticam. - ce je prazna ali komentar
				if (v == null) {
					v = new Vrstica();
					v.setVeljavna(false);
					vmes.pisi(v);
					// Ohranimo jih zaradi porocila o napaki (da sporoci pravo
					// vrstico)
					continue;
				}

				// Izvedi dejanski prvi prehod
				v = p1.pass1(v);
				// Pisi v vmesno datoteko
				vmes.pisi(v);
			}

			// To je potrebno zaradi pravilnega delovanja LTORG ukazov.
			LittabManager.getInstance().resetirajStevec();

			// Incializacija drugega prehoda (potrebuje zacetni naslov - zaradi
			// premescanja.
			Pass2 p2 = new Pass2(p1.getZacetniNaslovOP());

			ObjektnaDatoteka objDat = ObjektnaDatoteka.getInstance();
			// Zapisi zaglavje v objektno datoteko.
			objDat.pisiZaglavje(p1.getDolzina(), p1.getImePrograma(), p1
					.getZacetniNaslovOP());

			stVrstice = 0;
			while ((v = vmes.beri()) != null) {
				// Obdela vsako vrstico iz vmesne datoteke posebej.
				vrstica = v.toString(); // samo zaradi porocila o napaki
				stVrstice++;
				if (v.isVeljavna()) {
					// Ce ni komentar ali prazna
					// izvedi drugi prehod
					v = p2.pass2(v);

					if (v.getObjektnaKoda() != null)
						// Nekateri psevdoukazi ne vplivajo na objektno kodo
						// (BASE, NOBASE)
						objDat.pisi(v.getObjektnaKoda(), v.getNaslov());
					else if ("RESB".equals(v.getMnemonik())
							|| "RESW".equals(v.getMnemonik())) {
						// Ce naletimo na RESB ali RESW, je potrebno zapisati
						// nov ZapisOP
						objDat.flush(v.getNaslov());
					}
				}
				//Listing:
				System.out.println(v.toString());
			}
			// Na koncu napisemo se zaglavje
			objDat.pisiKonecOP(p1.getZacetniNaslovOP());
			// zapri datoteko + dejansko pisanje.
			objDat.close();
			obj = objDat.toString();
			System.out.println("a " + obj);
			vmes.close();
		} catch (IOException e) {
			// Ce pride do napake pri branju...
			e.printStackTrace();
		} catch (NapakaPriPrevajanju e) {
			// Porocio o napaki
			e.setMessage(e.getMessage() + "Št. vrstice: " + stVrstice
					+ " (Vrstica: " + vrstica + ")\n");
			throw (e);
		} finally {
			SimtabManager.getInstance().clear();
			ModTabManager.getInstance().clear();
			LittabManager.getInstance().clear();
			ObjektnaDatoteka.getInstance().clear();
		}

	}
	
	public String getObj() {
		return obj;
	}
}

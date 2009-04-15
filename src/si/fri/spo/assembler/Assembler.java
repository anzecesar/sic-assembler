package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import si.fri.spo.data.Vrstica;
import si.fri.spo.utils.Parser;

public class Assembler {

	public void assemble(String source, String destination) {
		String vrstica;
		PrebranaDatoteka dat = new PrebranaDatoteka();
		Parser p = new Parser();
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			while ((vrstica = input.readLine()) != null) {
				dat.dodajVrstico(p.parseLine(vrstica));
			}
		} catch (FileNotFoundException e) {
			//Ce nam ne uspe odpreti datoteke...
			e.printStackTrace();
		} catch (IOException e) {
			//Ce pride do napake pri branju...
			e.printStackTrace();
		}
	}

	public void assemble(String source) {
		assemble(source, "saf.r");
	}

	private void pisivVmesnoDat() {
		// must add
	}

	private void pass1() {
		int zacetniNaslovOP;
		int lokst;
		Vrstica vrstica = new Vrstica();
		// read and parse first line -> ideja je da vrne nek objekt s tremi
		// stringi, pac labela, mnemonik in operand
//		vrstica = parseLine("");
		if (vrstica.getMnemonik() == "START") {
			zacetniNaslovOP = Integer.parseInt(vrstica.getOperand());
			lokst = zacetniNaslovOP;
			pisivVmesnoDat();
//			parseLine("");
		} else {
			lokst = 0;
		}
		while (vrstica.getMnemonik() != "END") {
			if (!vrstica.isComment()) {
			}
		}
	}

	private void pass2() {

	}
}

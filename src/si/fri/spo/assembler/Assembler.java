package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.Parser;

public class Assembler {
	private boolean inMemory;
	
	public Assembler() {
		this(true);
	}
	
	public Assembler(boolean im) {
		inMemory = im;
	}

	public void assemble(String source, String destination) throws NapakaPriPrevajanju {
		pass1(source);
		
	}

	public void assemble(String source) throws NapakaPriPrevajanju {
		assemble(source, "saf.r");
	}

	private void pisivVmesnoDat() {
		// must add
	}

	private void pass1(String source) throws NapakaPriPrevajanju {
		String vrstica = "";
		PrebranaDatoteka dat = new PrebranaDatoteka();
		Parser p = new Parser();
		
		int stVrstice = 1;
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
			while ((vrstica = input.readLine()) != null) {
				if(inMemory)
					dat.dodajVrstico(p.parseLine(vrstica));
				stVrstice++;
			}
		} catch (FileNotFoundException e) {
			//Ce nam ne uspe odpreti datoteke...
			e.printStackTrace();
		} catch (IOException e) {
			//Ce pride do napake pri branju...
			e.printStackTrace();
		} catch (NapakaPriPrevajanju e) {
			e.setMessage(e.getMessage() + "Št. vrstice: " + stVrstice + "\n Vrstica: " + vrstica + "\n");
			throw(e);
		}
		
		//debug:
		System.out.println("Konca prebrana datoteka: ");
		for (Vrstica v : dat.getVrstice()) {
			System.out.println(v.toString());
		}
		
		
//		int zacetniNaslovOP;
//		int lokst;
//		Vrstica vrstica = new Vrstica();
//		// read and parse first line -> ideja je da vrne nek objekt s tremi
//		// stringi, pac labela, mnemonik in operand
////		vrstica = parseLine("");
//		if (vrstica.getMnemonik() == "START") {
//			zacetniNaslovOP = Integer.parseInt(vrstica.getOperand());
//			lokst = zacetniNaslovOP;
//			pisivVmesnoDat();
////			parseLine("");
//		} else {
//			lokst = 0;
//		}
//		while (vrstica.getMnemonik() != "END") {
//		}
	}

	private void pass2() {

	}
}

package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import si.fri.spo.data.Vrstica;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import si.fri.spo.utils.MnetabManager;
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

	// pretvarja operand v neko vrednost, ki spominja na integer :) zato da lahko pozneje primerno povecamo lokSt
	private int pretvoriOperand(String s) {
		int operand = 0; 
		if(s.charAt(0) == '#')
			operand = Integer.parseInt(s.substring(1, s.length()-1));
		return operand;
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
		
		
		int zacetniNaslovOP;
		int lokSt;
		int stariLokSt = 0;
		HashMap<String, Integer> SimTab = new HashMap<String, Integer>();
		MnetabManager mneTab = new MnetabManager();
		// read and parse first line -> ideja je da vrne nek objekt s tremi
		// stringi, pac labela, mnemonik in operand
//		vrstica = parseLine("");
		for (Vrstica v : dat.getVrstice()) {
			if (v.getMnemonik() == "START") {
				zacetniNaslovOP = Integer.parseInt(v.getOperand());
				lokSt = zacetniNaslovOP;
				pisivVmesnoDat();
			} else {
				lokSt = 0;
			}
			while (v.getMnemonik() != "END") {
				String labela = v.getLabela();
				if(labela != null) {
					if(SimTab.containsKey(labela) == true) 
						pisivVmesnoDat(); //pride do napake, postavimo bit napake!
					else 
						SimTab.put(labela, lokSt);
					stariLokSt = lokSt;
					String trenutniMnemonik = v.getMnemonik();
					if(mneTab.isMnemonik(trenutniMnemonik) == true) {
						mneTab.getFormat(trenutniMnemonik);
						if(trenutniMnemonik == "RESW")
							lokSt = lokSt + 3*(pretvoriOperand(v.getOperand()));
						else if (trenutniMnemonik == "RESB")
							lokSt = lokSt + 4*(pretvoriOperand(v.getOperand()));
						else 
						    lokSt = lokSt + mneTab.getFormat(trenutniMnemonik); 
					} else {
						//napaka
						System.out.println("error");
					}
					//stari lokSt
					pisivVmesnoDat();
					v = dat.getVrstice();
				}
			}
		}
	}

	private void pass2() {

	}
}

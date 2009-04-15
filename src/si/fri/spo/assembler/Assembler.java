package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import si.fri.spo.data.fileLine;

public class Assembler {
	
	public void assemble(String source, String destination) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(source));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void assemble(String source) {
		assemble(source, "saf.r");
	}
	private fileLine readAndParseLine() {
		fileLine vrstica = new fileLine();
		vrstica.setComment(false);
		vrstica.setLabela("");
		vrstica.setMnemonik("");
		vrstica.setOperand("");
		return vrstica;
		// bere iz datoteke iz argumenta, po vrstico in vrstico, mogoce globalna spremenljivka 
		// za datoteko in katero vrstico? ali pa za readerje? ali pa se vse prebere v eno tabelo 
		// in se sparsa takoj na zacetku? I dunno :P
	}
	private void pisivVmesnoDat() {
		// must add
	}
		int zacetniNaslovOP;
		int lokst;
		fileLine vrstica = new fileLine(); 
		//read and parse first line -> ideja je da vrne nek objekt s tremi stringi, pac labela, mnemonik in operand
		vrstica = readAndParseLine();
		if( vrstica.getMnemonik() == "START" ) {
				zacetniNaslovOP = Integer.parseInt(vrstica.getOperand());
				lokst = zacetniNaslovOP; 
				pisivVmesnoDat(); 
				readAndParseLine();				
		} else {
			lokst = 0;
		}
		while (vrstica.getMnemonik() != "END") {
			if(!vrstica.isComment()) {
				
        try {
			while (( line = input.readLine()) != null){
			  //Read lines :]
			}
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}
		}
	}
	
	private void pass2() {
		
	}
}

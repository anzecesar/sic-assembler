package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
	
	private void pass1() {
		
	}
	
	private void pass2() {
		
	}
}

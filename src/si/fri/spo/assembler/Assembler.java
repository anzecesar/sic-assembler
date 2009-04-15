package si.fri.spo.assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	
	private void pass1(BufferedReader input) {
		String line = null; //not declared within while loop
        
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
	
	private void pass2() {
		
	}
}

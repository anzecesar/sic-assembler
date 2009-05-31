package si.fri.spo;

import si.fri.spo.assembler.Assembler;
import si.fri.spo.exceptions.NapakaPriPrevajanju;

public class Main {
	private static final String  VERSION = "0.3";
	
	//ce je IN_MEMORY enak true, potem ne uporablja vmesne datoteke
	//vse se hrani v pomnilniku.
	private static final boolean IN_MEMORY = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("SIC/XE Assembler " + VERSION + "\n Copyleft 2009 Anton Perc & Anže Cesar");
		Assembler a = new Assembler(IN_MEMORY);
		switch(args.length) {
		
			case 0:
				System.out.println("Usage: asic <input file> or asic <input file> <output file>");
				break;
			case 1:
				//Got only the input file.
				System.out.println("Compiling your source file...");
				try {
					a.assemble(args[0]);
				} catch (NapakaPriPrevajanju e) {
//					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				break;
			case 2:
				//Got input and output files.
				System.out.println("Compiling your source file to specified output file...");
				break;
		}

	}

}

package si.fri.spo;

import si.fri.spo.assembler.Assembler;

public class Main {
	private static final String  VERSION = "0.1";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("SIC/XE Assembler " + VERSION);
		Assembler a = new Assembler();
		switch(args.length) {
		
			case 0:
				System.out.println("Usage: asic <input file> or asic <input file> <output file>");
				break;
			case 1:
				//Got only the input file.
				System.out.println("Compiling your source file. If you imagine it really really hard :]");
				a.assemble(args[0]);
				break;
			case 2:
				//Got input and output files.
				System.out.println("Compiling your source file to specified output file. If you imagine it really really hard :]");
				break;
		}

	}

}

package si.fri.spo.utils;

import java.util.HashMap;
import java.util.Map;

import si.fri.spo.data.Mnemonic;

public class MnetabManager {
	private static MnetabManager m_manager;
	
	private Map<String,Mnemonic> mnetab;
	
	private MnetabManager() {
		makeTable();
	}
	
	public static MnetabManager getInstance() {
		if(m_manager == null)
			m_manager = new MnetabManager();
		return m_manager;
	}
	
	// tabela mnemonikov 
	// referencna tabela je iz http://www-rohan.sdsu.edu/faculty/gleonard/html/530/SicInstructions.html
	// format 0, rabi obrazlozitve, ni se implementiran
	// format 4 je oznacen s +, in ni jemljen kot osnovni
	// dolzina se izracuna preko formata, tako da ni potrebna
	private void makeTable() {
		mnetab = new HashMap<String, Mnemonic>();
		mnetab.put("ADD", new Mnemonic(0x18, 3));
		mnetab.put("ADDF", new Mnemonic(0x58, 3));
		mnetab.put("ADDR", new Mnemonic(0x90, 2));
		mnetab.put("AND", new Mnemonic(0x40, 3));
		mnetab.put("CLEAR", new Mnemonic(0xb4, 2));
		mnetab.put("COMP", new Mnemonic(0x28, 3));
		mnetab.put("COMPF", new Mnemonic(0x88, 3));
		mnetab.put("COMPR", new Mnemonic(0xa0, 2));
		mnetab.put("DIV", new Mnemonic(0x24, 3));
		mnetab.put("DIVF", new Mnemonic(0x64, 3));
		mnetab.put("DIVR", new Mnemonic(0x9c, 2));
		mnetab.put("FIX", new Mnemonic(0xc4, 1));
		mnetab.put("FLOAT", new Mnemonic(0xc0, 1));
		mnetab.put("HIO", new Mnemonic(0xf4, 1));
		mnetab.put("J", new Mnemonic(0x3c, 3));
		mnetab.put("JEQ", new Mnemonic(0x30, 3));
		mnetab.put("JGT", new Mnemonic(0x34, 3));
		mnetab.put("JLT", new Mnemonic(0x38, 3));
		mnetab.put("JSUB", new Mnemonic(0x48, 3));
		mnetab.put("LDA", new Mnemonic(0x00, 3));
		mnetab.put("LDB", new Mnemonic(0x68, 3));
		mnetab.put("LDCH", new Mnemonic(0x50, 3));
		mnetab.put("LDF", new Mnemonic(0x70, 3));
		mnetab.put("LDL", new Mnemonic(0x08, 3));
		mnetab.put("LDS", new Mnemonic(0x6c, 3));
		mnetab.put("LDT", new Mnemonic(0x74, 3));
		mnetab.put("LDX", new Mnemonic(0x04, 3));
		mnetab.put("LPS", new Mnemonic(0xd0, 3));
		mnetab.put("MUL", new Mnemonic(0x20, 3));
		mnetab.put("MULF", new Mnemonic(0x60, 3));
		mnetab.put("MULR", new Mnemonic(0x98, 2));
		mnetab.put("NORM", new Mnemonic(0xc8, 1));
		mnetab.put("OR", new Mnemonic(0x44, 3));
		mnetab.put("RD", new Mnemonic(0xd8, 3));
		mnetab.put("RMO", new Mnemonic(0xac, 2));
		mnetab.put("RSUB", new Mnemonic(0x4c, 3));
		mnetab.put("SHIFTL", new Mnemonic(0xa4, 2));
		mnetab.put("SHIFTR", new Mnemonic(0xa8, 2));
		mnetab.put("SIO", new Mnemonic(0xf0, 1));
		mnetab.put("SSK", new Mnemonic(0xec, 3));
		mnetab.put("STA", new Mnemonic(0x0c,3));
		mnetab.put("STB", new Mnemonic(0x78, 3));
		mnetab.put("STCH", new Mnemonic(0x54, 3));
		mnetab.put("STF", new Mnemonic(0x80, 3));
		mnetab.put("STI", new Mnemonic(0xd4, 3));
		mnetab.put("STL", new Mnemonic(0x14, 3));
		mnetab.put("STS", new Mnemonic(0x7c, 3));
		mnetab.put("STSW", new Mnemonic(0xe8,3));
		mnetab.put("STT", new Mnemonic(0x84, 3));
		mnetab.put("STX", new Mnemonic(0x10, 3));
		mnetab.put("SUB", new Mnemonic(0x1c, 3));
		mnetab.put("SUBF", new Mnemonic(0x5c, 3));
		mnetab.put("SUBR", new Mnemonic(0x94, 2));
		mnetab.put("SVC", new Mnemonic(0xb0, 2));
		mnetab.put("TD", new Mnemonic(0xe0, 3));
		mnetab.put("TIO", new Mnemonic(0xf8, 1));
		mnetab.put("TIX", new Mnemonic(0x2c, 3));
		mnetab.put("TIXR", new Mnemonic(0xb8, 2));
		mnetab.put("WD", new Mnemonic(0xdc, 3));
		
		//Naslednji ukazi so ubistvu zloraba mnetaba :]
		//Gre za psevdo ukaze in so tu zgolj zaradi prebavljanja
		//izvorne kode :].
		mnetab.put("RESW", new Mnemonic(0x0, -1));
		mnetab.put("RESB", new Mnemonic(0x0, -1));
		mnetab.put("WORD", new Mnemonic(0x0, -1));
		mnetab.put("BYTE", new Mnemonic(0x0, -1));
		mnetab.put("START", new Mnemonic(0x0, -1));
		mnetab.put("END", new Mnemonic(0x0, -1));
		
	}
	
	public boolean isMnemonik(String mnemonik) {
		return mnetab.containsKey(mnemonik);
	}
	
	public int getOpCode(String s) {
		return mnetab.get(s).getOpcode();
	}
	
	public int getShiftedOpCode(String s) {
		return mnetab.get(s).getShiftedOpCode();
	}
	
	public int getFormat(String s) {
		return mnetab.get(s).getFormat();		
	}
}
